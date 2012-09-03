package com.dpajd.ProtectionPlugin.Main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	public PluginDescriptionFile pdf;
	public static final String default_prefix = ChatColor.RED + "[BorderPatrol] " + ChatColor.GOLD;
	public BPConfig settings;
	
	@Override
	public void onEnable() {
		pdf = this.getDescription();
		log.info("You have now enabled " + pdf.getName() + " Version "
				+ pdf.getVersion() + " Made by " + pdf.getAuthors());
		PluginManager pm = getServer().getPluginManager();
		settings = new BPConfig(this);
		
		pm.registerEvents(new BPListener(this), this);
		
		this.getCommand("bp").setExecutor(new CommandExecutor(){

			@Override
			public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
				/*
				 * see, create, bypass, remove [id], count [player]
				 * 
				 * faith <player>, unfaith <player> Thaz it, no more
				 * 
				 * */
				Player player = (sender instanceof Player) ? (Player)sender: null;
				if (args != null){
					if (args[0].equalsIgnoreCase("see")){
						
					}else if (args[0].equalsIgnoreCase("create")){
						if (BPPerms.canCreate((Player)sender)){
							
						}
					}else if (args[0].equalsIgnoreCase("bypass")){
						if (player != null){
							if (BPPerms.isAdmin(player)){
								
							}
						}
					}else if (args[0].equalsIgnoreCase("remove")){
						if (args.length == 2){
							// remove chunk by ID
							String targetID = args[1];
							
						}else if (args.length == 1){
							// remove chunk player is standing in
							
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
						}
					}else if (args[0].equalsIgnoreCase("unfaith")){
						if (args.length == 2){
							Player target = Bukkit.getPlayer(args[1]);
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