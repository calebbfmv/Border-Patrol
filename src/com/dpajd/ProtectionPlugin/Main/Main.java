package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.dpajd.ProtectionPlugin.Commands.*;
import com.dpajd.ProtectionPlugin.Protections.*;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.ChunkData;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class Main extends JavaPlugin{
	public final Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdf;
	static final String default_prefix = ChatColor.RED + "[BorderPatrol] " + ChatColor.GOLD;
	public HashSet<String> toolEnabled = new HashSet<String>();
	public HashSet<String> bypassEnabled = new HashSet<String>();
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
				if (cd.getChunk().equals(chunk)) return true;
			}
		}
		return false;
	}
	
	public boolean isProtected(Chunk chunk, ProtectionType type){
		for (Region r : regions){
			for (ChunkData cd : r.getChunks()){
				if (cd.getChunk().equals(chunk) && r.getProtections().contains(type)) return true;
			}
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
	public ArrayList<Region> getRegions(){
		return regions;
	}

	public void toggle(HashSet<String> set,String key){
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
	
	public boolean isBypass(String player){
		return bypassEnabled.contains(player);
	}
	
	public boolean isBypass(Player player){
		return isBypass(player.getName());
	}
	
	@Override
	public void onEnable() {
		pdf = this.getDescription();
		log.info("You have now enabled " + pdf.getName() + " Version "
				+ pdf.getVersion() + " Made by " + pdf.getAuthors());
		PluginManager pm = getServer().getPluginManager();
		settings = new BPConfig(this);
		
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
		pm.registerEvents(new NoMonsterSpawning	(this), this);
		pm.registerEvents(new NoMobDamage		(this), this);
		pm.registerEvents(new NoPVP				(this), this);
		pm.registerEvents(new NoVehicles		(this), this);
		pm.registerEvents(new NoChestAccess		(this), this);
		pm.registerEvents(new NoUse				(this), this);
		pm.registerEvents(new EntryMessage		(this), this);
		pm.registerEvents(new ExitMessage		(this), this);
		
		getCommand("bpbypass").setExecutor(		new BPCommandBypass		(this));
		getCommand("bpcount").setExecutor(		new BPCommandCount		(this));
		getCommand("bpcreate").setExecutor(		new BPCommandCreate		(this));
		getCommand("bpfaith").setExecutor(		new BPCommandFaith		(this));
		getCommand("bpremove").setExecutor(		new BPCommandRemove		(this));
		getCommand("bpsee").setExecutor(		new BPCommandSee		(this));
		getCommand("bptool").setExecutor(		new BPCommandTool		(this));
		getCommand("bpunfaith").setExecutor(	new BPCommandUnFaith	(this));
		getCommand("bpaddflag").setExecutor(	new BPCommandAddFlag	(this));
		getCommand("bpremoveflag").setExecutor(	new BPCommandRemoveFlag	(this));
		getCommand("bphelp").setExecutor(		new BPCommandHelp		(this));
		getCommand("bpflags").setExecutor(		new BPCommandFlags		(this));
		getCommand("bp").setExecutor(			new BPCommandBP			(this));
		
	}
	
	public void sendMessage(String player, MsgType type, String message){
		sendMessage(Bukkit.getPlayerExact(player), type, message);
	}
	
	public void sendMessage(String player, String message){
		sendMessage(Bukkit.getPlayerExact(player), message);
	}
	
	public void sendMessage(String player, String[] lines){
		sendMessage(Bukkit.getPlayerExact(player), lines);
	}
	
	public void sendMessage(String player, MsgType type, String[] lines){
		sendMessage(Bukkit.getPlayerExact(player), type, lines);
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