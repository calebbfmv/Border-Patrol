package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.dpajd.ProtectionPlugin.Protections.*;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.ChunkData;
import com.dpajd.ProtectionPlugin.Regions.Member;
import com.dpajd.ProtectionPlugin.Regions.Owner;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class Main extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdf;
	static final String default_prefix = ChatColor.RED + "[BorderPatrol] " + ChatColor.GOLD;
	HashSet<String> toolEnabled = new HashSet<String>();
	HashSet<String> bypassEnabled = new HashSet<String>();
	HashMap<String,Location> toolUse = new HashMap<String,Location>();
	BPConfig settings = null;
	private ArrayList<Region> regions = new ArrayList<Region>();
	public enum MsgType{
		NORMAL(ChatColor.RED + "[BorderPatrol] " + ChatColor.GOLD),
		DENIED(ChatColor.RED + "[BorderPatrol] " + ChatColor.RED),
		ERROR(ChatColor.RED + "[BorderPatrol] " + ChatColor.BOLD);
		
		private String prefix;
		@SuppressWarnings("serial")
		private static HashMap<MsgType,ChatColor> color = new HashMap<MsgType,ChatColor>(){{
			put(NORMAL, ChatColor.GOLD);
			put(DENIED, ChatColor.RED);
			put(ERROR, ChatColor.BOLD);
		}};
		
		MsgType(String prefix){
			this.prefix = prefix;
		}
		
		@Override
		public String toString(){
			return this.prefix;
		}
		
		public ChatColor getColor(){
			return color.get(this);
		}
		
	}
	
	public boolean isProtected(Chunk chunk){
		for (Region r : regions){
			for (ChunkData cd : r.getChunks()){
				//if (cd.getChunkX() == chunk.getX() && cd.getChunkZ() == chunk.getZ() && cd.getWorld().getName().equals(chunk.getWorld().getName())) return true;
				if (cd.getChunk().equals(chunk)) return true;
				//System.out.println("Chunk:" + chunk.getX() + "," + chunk.getZ() + ", ChunkData:" + cd.getChunkX() + "," + cd.getChunkZ());
			}
			//System.out.println("RegionList[0]: " + regions.get(0).getChunks().size());
		}
		return false;
	}
	
	public Region getRegion(Chunk chunk){
		for (Region r : regions){
			for (ChunkData cd : r.getChunks()){
				if (cd.getChunk().equals(chunk)) return r;
			}
		}
		return null;
	}
	
	public void addRegion(Region region){
		regions.add(region);
	}
	
	public void removeRegion(Region region){
		Region remove = null;
		for (Region r : regions){
			if (r.getName().equals(region.getName())){
				remove = r;
				r.deleteRegion();
				break;
			}
		}
		if (remove != null) regions.remove(remove);
	}
	
	public void reloadRegions(){
		regions = Region.loadRegions();
	}
	
	public void setRegions(ArrayList<Region> regions){
		this.regions = regions;
	}
	public final ArrayList<Region> getRegions(){
		return regions;
	}

	private void toggle(HashSet<String> set,String key){
		if (set.contains(key)) set.remove(key);
		else set.add(key);
	}
	
	public void deleteRegion(Region region){
		
		removeRegion(region);
		region.deleteRegion();
	}
	
	public BPConfig getSettings(){
		return settings;
	}
	
	@Override
	public void onEnable() {
		pdf = this.getDescription();
		log.info("You have now enabled " + pdf.getName() + " Version "
				+ pdf.getVersion() + " Made by " + pdf.getAuthors());
		PluginManager pm = getServer().getPluginManager();
		settings = new BPConfig(this);
		
		//pm.registerEvents(new BPListener(this), this);
		
		regions = Region.loadRegions();
		
		pm.registerEvents(new ElectricFence		(this), this);
		pm.registerEvents(new NoBuild			(this), this);
		pm.registerEvents(new NoEndermanGrief	(this), this);
		pm.registerEvents(new NoEntry			(this), this);
		pm.registerEvents(new NoExplosion		(this), this);
		pm.registerEvents(new NoFire			(this), this);
		pm.registerEvents(new NoLavaFlow		(this), this);
		pm.registerEvents(new NoPistonGrief		(this), this);
		pm.registerEvents(new NoWaterFlow		(this), this);
		pm.registerEvents(new NoInteract		(this), this);
		
		this.getCommand("bp").setExecutor(new CommandExecutor(){
			@SuppressWarnings("serial") // Because I'm OCD and if i see these 1 more time while this is incomplete...
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
				String argsString = StringUtils.join(args, " ").toLowerCase();
				Player player = (sender instanceof Player) ? (Player)sender: null;
				if (args.length > 0){
					if (args[0].equalsIgnoreCase("see")){
						if (player != null){
							Region r = getRegion(player.getLocation().getChunk());
							if (r != null){
								sendMessage(player, new String[]{
										"Chunk '"+r.getName()+"' is owned by: " + r.getOwner().getName(),
										"Protections: " + ChatColor.GRAY + r.getProtections(),
										"Members: " + ChatColor.GRAY + r.getMembers()
										});
							}else{
								sendMessage(player, "This chunk is empty.");
							}
						}
					}else if(args[0].equalsIgnoreCase("help")){
						// TODO: List all commands.
					}else if (args[0].equalsIgnoreCase("create")){
						// TODO: Create method to parse args for flags
						// TODO: Add protections to region from flags
						// XXX: Add members to region from flag
						if (player != null){
							if (BPPerms.canCreate(player)){
								Region r = new Region(player,1,player.getLocation().getChunk(),new ArrayList<ProtectionType>(){{add(ProtectionType.NO_BUILD);}});
								if (argsString.contains("-f")){
									r.generateFence();
								}
								r.saveRegion();
								addRegion(r);
								sendMessage(player, "Created protection id: '" + r.getName()+"'");
							}
						}else return false;
					}else if (args[0].equalsIgnoreCase("tool")){
						if (player != null){
							toggle(toolEnabled,player.getName());
							sendMessage(player, "Toggled tool: " + ((toolEnabled.contains(player.getName()))?"On":"Off"));
						}
					}else if (args[0].equalsIgnoreCase("bypass")){
						if (player != null){
							if (BPPerms.isAdmin(player)){
								toggle(bypassEnabled,player.getName());
							}
						}
					}else if (args[0].equalsIgnoreCase("remove")){
						// XXX: Remove generated fence?
						if (args.length == 1){
							// remove chunk player is standing in
							if (player != null){
								Chunk ch = player.getLocation().getChunk();
								Region r = getRegion(ch);
								if (r != null){
									if (r.getOwner().getName().equals(player.getName()) || BPPerms.isAdmin(player)){
										deleteRegion(r);
										sendMessage(player, "Deleted chunk region");
									}
								}
							}
						}
					}else if (args[0].equalsIgnoreCase("count")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							sendMessage(target, target.getName() + " has a total of " + new Owner(target).getRegionCount() + " region protections.");
						}else if (args.length == 1){
							Player target = sender instanceof Player? (Player)sender : null;
							if (target != null){
								sendMessage(target, target.getName() + " has a total of " + new Owner(target).getRegionCount() + " region protections.");
							}else return false;
						}
					}else if (args[0].equalsIgnoreCase("faith")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							if (target != null){
								Region r = getRegion(player.getLocation().getChunk());
								r.addMember(new Member(target));
								r.saveRegion();
								sendMessage(player, target.getName() + " added to the region("+r.getName()+").");
								sendMessage(target, "You have been added to the region("+r.getName()+") by " + player.getName());
							}else{
								player.sendMessage(default_prefix + "Player not found.");
							}
													
						}
					}else if (args[0].equalsIgnoreCase("unfaith")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							if (target != null){
								Region r = getRegion(player.getLocation().getChunk());
								r.removeMember(new Member(target));
								r.saveRegion();								
								sendMessage(player, target.getName() + " removed from the region("+r.getName()+").");
								sendMessage(target, "You have been removed from the region("+r.getName()+")");
							}else{
								sendMessage(player, "Player not found.");
							}
						}
					}
				}else {
					// No arguments, sending version info
					sender.sendMessage(default_prefix + pdf.getVersion());
				}
				return true;
			}});
	}
	
	public void sendMessage(Player player, MsgType type, String message){
		player.sendMessage(type + message);
	}
	
	public void sendMessage(Player player, String message){
		sendMessage(player, MsgType.NORMAL, message);
	}
	
	public void sendMessage(Player player, String[] lines){
		sendMessage(player,MsgType.NORMAL,lines);
	}
	public void sendMessage(Player player, MsgType type, String[] lines){
		if (lines != null){
			sendMessage(player, type, lines[0]);
			if (lines.length > 1){
				for (int i = 1; i < lines.length; i++){
					player.sendMessage("    " + type.getColor() + lines[i]);
				}
			}
		}
	}

	@Override
	public void onDisable() {
		log.info(ChatColor.GOLD + "You have now disabled " + pdf.getName()
				+ " Version " + pdf.getVersion() + " Made by "
				+ pdf.getAuthors());
	}
	
}