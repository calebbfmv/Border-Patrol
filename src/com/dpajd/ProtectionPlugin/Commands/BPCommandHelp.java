package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPCommandHelp extends BPCommand{

	public BPCommandHelp(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length == 0){
				plugin.sendMessage(player, new String[]{
						"Command listing:",
						ChatColor.DARK_RED + "----------------------------------------------",
						"bp: "+ChatColor.GRAY+"Returns the running Border Patrol version",
						"bphelp: "+ChatColor.GRAY+"Gives a command listing of Border Patrol",
						"bpcreate: "+ChatColor.GRAY+"Creates a region protection",
						"bpflags: "+ChatColor.GRAY+"Lists all flags available to you.",
						"bpaddflag: "+ChatColor.GRAY+"Adds a protection flag to a region",
						"bpremoveflag: "+ChatColor.GRAY+"Removes a protection flag from a region",
						"bpbypass: "+ChatColor.GRAY+"Bypasses region protection",
						"bpcount: "+ChatColor.GRAY+"Counts player region count",
						"bpfaith: "+ChatColor.GRAY+"Adds player to region members",
						"bpunfaith: "+ChatColor.GRAY+"Removes player from region members",
						"bpsee: "+ChatColor.GRAY+"See if a chunk is protected",
						"bptool: "+ChatColor.GRAY+"Toggles use of the tool",
						ChatColor.DARK_RED + "----------------------------------------------",
						"Type "+ChatColor.GRAY+"/bphelp <command>" + ChatColor.GOLD + " for command syntax and information."
				});
			}else if (args.length > 0){
				if (args[0].equalsIgnoreCase("bp")){
					plugin.sendMessage(player, new String[]{
							"/bp: "+ChatColor.GRAY+"Returns the running Border Patrol version",
							"Syntax: "+ChatColor.GRAY+"/bp",
					});
				}else if (args[0].equalsIgnoreCase("bphelp")){
					plugin.sendMessage(player, new String[]{
							"/bphelp: "+ChatColor.GRAY+"Gives a command listing of Border Patrol",
							"Syntax: "+ChatColor.GRAY+"/bphelp",
							"        "+ChatColor.GRAY+"/bphelp <command>"
					});
				}else if (args[0].equalsIgnoreCase("bpcreate")){
					plugin.sendMessage(player, new String[]{
							"/bpcreate: "+ChatColor.GRAY+"Creates a region protection",
							"Syntax: "+ChatColor.GRAY+"/bpcreate",
							"        "+ChatColor.GRAY+"/bpcreate [size] [-f] [-p <protection>]"
					});
				}else if (args[0].equalsIgnoreCase("bpflags")){
					plugin.sendMessage(player, new String[]{
							"/bpflags: "+ChatColor.GRAY+"Lists all flags available to you.",
							"Syntax: "+ChatColor.GRAY+"/bpflags",
					});
				}else if (args[0].equalsIgnoreCase("bpaddflag")){
					plugin.sendMessage(player, new String[]{
							"/bpaddflag: "+ChatColor.GRAY+"Adds a protection flag to a region",
							"Syntax: "+ChatColor.GRAY+"/bpaddflag <protection>",
					});
				}else if (args[0].equalsIgnoreCase("bpremoveflag")){
					plugin.sendMessage(player, new String[]{
							"/bpremoveflag: "+ChatColor.GRAY+"Removes a protection flag from a region",
							"Syntax: "+ChatColor.GRAY+"/bpremoveflag <protection>",
					});
				}else if (args[0].equalsIgnoreCase("bpbypass")){
					plugin.sendMessage(player, new String[]{
							"/bpbypass: "+ChatColor.GRAY+"Bypasses region protection",
							"Syntax: "+ChatColor.GRAY+"/bpbypass <protection>"
					});
				}else if (args[0].equalsIgnoreCase("bpcount")){
					plugin.sendMessage(player, new String[]{
							"/bpcount: "+ChatColor.GRAY+"Counts player region count",
							"Syntax: "+ChatColor.GRAY+"/bpcount",
							"		 "+ChatColor.GRAY+"/bpcount [player]"
					});
				}else if (args[0].equalsIgnoreCase("bpfaith")){
					plugin.sendMessage(player, new String[]{
							"/bpfaith: "+ChatColor.GRAY+"Adds player to region members",
							"Syntax: "+ChatColor.GRAY+"/bpfaith <player>"
					});
				}else if (args[0].equalsIgnoreCase("bpunfaith")){
					plugin.sendMessage(player, new String[]{
							"/bpunfaith: "+ChatColor.GRAY+"Removes player from region members",
							"Syntax: "+ChatColor.GRAY+"/bpunfaith <player>"
					});
				}else if (args[0].equalsIgnoreCase("bpsee")){
					plugin.sendMessage(player, new String[]{
							"/bpsee: "+ChatColor.GRAY+"See if a chunk is protected",
							"Syntax: "+ChatColor.GRAY+"/bpsee"
					});
				}else if (args[0].equalsIgnoreCase("bptool")){
					plugin.sendMessage(player, new String[]{
							"/bptool: "+ChatColor.GRAY+"Toggles use of the tool",
							"Syntax: "+ChatColor.GRAY+"/bptool"
					});
				}
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return CommandType.HELP;
	}

}
