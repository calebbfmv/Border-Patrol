package com.dpajd.ProtectionPlugin.Main;

import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public final Logger log = Logger.getLogger("Minecraft");
	PluginDescriptionFile pdf;
	static final String default_prefix = ChatColor.RED + "[BorderPatrol] " + ChatColor.GOLD;
	HashSet<String> toolEnabled = new HashSet<String>();
	HashSet<String> bypassEnabled = new HashSet<String>();
	BPConfig settings;
	
	private void toggle(HashSet<String> set,String key){
		if (set.contains(key)) set.remove(key);
		else set.add(key);
	}
	
	@Override
	public void onEnable() {
		pdf = this.getDescription();
		log.info("You have now enabled " + pdf.getName() + " Version "
				+ pdf.getVersion() + " Made by " + pdf.getAuthors());
		PluginManager pm = getServer().getPluginManager();
		settings = new BPConfig(this);
		
		pm.registerEvents(new BPListener(this), this);
		
		this.getCommand("bp").setExecutor(new CommandExecutor(){

			@SuppressWarnings("unused") // Because I'm OCD and if i see unused 1 more time while this is incomplete...
			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
				Player player = (sender instanceof Player) ? (Player)sender: null;
				if (args.length > 0){
					if (args[0].equalsIgnoreCase("see")){
						if (player != null){
							ChunkRegion cr = ChunkRegion.getRegionAt(player.getLocation());
							if (cr != null){
								player.sendMessage(default_prefix + "Chunk '"+cr.getChunkX()+cr.getChunkZ()+"' is owned by: " + cr.getOwner());
								player.sendMessage(ChatColor.GOLD + "Protections: " + cr.getProtections());
							}else{
								player.sendMessage(default_prefix + "This chunk is empty.");
							}
						}
					}else if (args[0].equalsIgnoreCase("create")){
						if (player != null){
							if (BPPerms.canCreate(player)){
								ChunkRegion.saveRegion(new ChunkRegion(player.getLocation(),player.getName()));
							}
						}else return false;
					}else if (args[0].equalsIgnoreCase("tool")){
						if (player != null){
							toggle(toolEnabled,player.getName());
							player.sendMessage(default_prefix + "Toggled tool: " + ((toolEnabled.contains(player.getName()))?"On":"Off"));
						}
					}else if (args[0].equalsIgnoreCase("bypass")){
						if (player != null){
							if (BPPerms.isAdmin(player)){
								toggle(bypassEnabled,player.getName());
							}
						}
					}else if (args[0].equalsIgnoreCase("remove")){
						ChunkRegion cr;
						if (args.length == 2){
							// remove chunk by ID
							String targetID = args[1];
							ChunkRegion.deleteRegion(targetID);
						}else if (args.length == 1){
							// remove chunk player is standing in
							if (player != null){
								Chunk ch = player.getLocation().getChunk();
								if ((cr = ChunkRegion.getRegionAt(player.getLocation())) != null){
									if (cr.getOwner().equals(player.getName()) || BPPerms.isAdmin(player)){
										player.sendMessage(default_prefix + "Deleted chunk region");
										ChunkRegion.deleteRegion(player.getWorld().getName() + ch.getX() + ch.getZ());
									}
								}
							}
						}
					}else if (args[0].equalsIgnoreCase("count")){
						if (args.length == 2){
							// count the amount of protections given player has
							Player target = Bukkit.getPlayer(args[1]);
						}else if (args.length == 1){
							Player target = sender instanceof Player? (Player)sender : null;
							if (target != null){
								// count the amount of protections sender has
							}else return false;
						}
					}else if (args[0].equalsIgnoreCase("faith")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							if (target != null){
								ChunkRegion cr = ChunkRegion.getRegionAt(player.getLocation());
								cr.addAccess(target.getName());
								player.sendMessage(default_prefix + target.getName() + " added to chunk("+cr.getId()+").");
								target.sendMessage(default_prefix + "You have been added to chunk("+cr.getId()+")");
							}else{
								player.sendMessage(default_prefix + "Player not found.");
							}
													
						}
					}else if (args[0].equalsIgnoreCase("unfaith")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
							if (target != null){
								ChunkRegion cr = ChunkRegion.getRegionAt(player.getLocation());
								cr.removeAccess(target.getName());
								player.sendMessage(default_prefix + target.getName() + " removed from chunk("+cr.getId()+").");
								target.sendMessage(default_prefix + "You have been removed from chunk("+cr.getId()+")");
							}else{
								player.sendMessage(default_prefix + "Player not found.");
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

	@Override
	public void onDisable() {
		log.info(ChatColor.GOLD + "You have now disabled " + pdf.getName()
				+ " Version " + pdf.getVersion() + " Made by "
				+ pdf.getAuthors());
	}
	
}