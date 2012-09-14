package com.dpajd.ProtectionPlugin.Commands;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;

public class BPCommandFlags extends BPCommand{

	public BPCommandFlags(Main plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length == 0){
				
				ArrayList<String> protections = new ArrayList<String>();
				for (ProtectionType t : ProtectionType.values()){
					if (BPPerms.hasFlag(player, t)) protections.add(t.name());
				}
								
				plugin.sendMessage(player, new String[]{
						"Protection Flags available to you:",
						ChatColor.DARK_RED + "----------------------------------------------",
						ChatColor.GRAY + protections.toString(),
						ChatColor.DARK_RED + "----------------------------------------------",
						"Type "+ChatColor.GRAY+"/bpflags <flag>" + ChatColor.GOLD + " for descriptions."
				});
			}else if (args.length > 0){
				if (args[0].equalsIgnoreCase("ELECTRIC")){
					plugin.sendMessage(player, "ELECTRIC: "+ChatColor.GRAY+"Prevents non-members and hostile mobs access and shocks them.");
				}else if (args[0].equalsIgnoreCase("ENTRY")){
					plugin.sendMessage(player, "ENTRY: "+ChatColor.GRAY+"Prevents non-members and hostile mobs access.");
				}else if (args[0].equalsIgnoreCase("FIRE")){
					plugin.sendMessage(player, "FIRE: "+ChatColor.GRAY+"Prevents block burning and spread of fire.");
				}else if (args[0].equalsIgnoreCase("ENDERMAN")){
					plugin.sendMessage(player, "ENDERMAN: "+ChatColor.GRAY+"Prevents Enderman from picking-up or placing blocks.");
				}else if (args[0].equalsIgnoreCase("WATER_FLOW")){
					plugin.sendMessage(player, "WATER_FLOW: "+ChatColor.GRAY+"Prevents water flowing in the region.");
				}else if (args[0].equalsIgnoreCase("LAVA_FLOW")){
					plugin.sendMessage(player, "LAVA_FLOW: "+ChatColor.GRAY+"Prevents lava flowing in the region.");
				}else if (args[0].equalsIgnoreCase("PISTON")){
					plugin.sendMessage(player, "PISTON: "+ChatColor.GRAY+"Prevents pistons altering region unless the piston originates from within.");
				}else if (args[0].equalsIgnoreCase("BUILD")){
					plugin.sendMessage(player, "BUILD: "+ChatColor.GRAY+"Prevents non-members building in region.");
				}else if (args[0].equalsIgnoreCase("MONSTER_SPAWNING")){
					plugin.sendMessage(player, "MONSTER_SPAWNING: "+ChatColor.GRAY+"Prevents monsters spawning within region.");
				}else if (args[0].equalsIgnoreCase("PVP")){
					plugin.sendMessage(player, "PVP: "+ChatColor.GRAY+"Prevents PVP in region.");
				}else if (args[0].equalsIgnoreCase("MOB_DAMAGE")){
					plugin.sendMessage(player, "MOB_DAMAGE: "+ChatColor.GRAY+"Prevents monsters causing damage within region.");
				}else if (args[0].equalsIgnoreCase("VEHICLES")){
					plugin.sendMessage(player, "VEHICLES: "+ChatColor.GRAY+"Prevents vehicle placement within region.");
				}else if (args[0].equalsIgnoreCase("CHEST_ACCESS")){
					plugin.sendMessage(player, "CHEST_ACCESS: "+ChatColor.GRAY+"Prevent access to chests, furnaces, etc in region.");
				}else if (args[0].equalsIgnoreCase("USE")){
					plugin.sendMessage(player, "USE: "+ChatColor.GRAY+"Prevent use of buttons, switches, doors, trapdoors, gates, noteblock, jukebox, and bed.");
				}else if (args[0].equalsIgnoreCase("WELCOME")){
					plugin.sendMessage(player, new String[]{"WELCOME: "+ChatColor.GRAY+"Sets the welcome message to the region.",
															"Syntax: "+ChatColor.GRAY+"/bpaddflag welcome <message>"});
				}else if (args[0].equalsIgnoreCase("FAREWELL")){
					plugin.sendMessage(player, new String[]{"FAREWELL: "+ChatColor.GRAY+"Sets the farewell message to the region.",
															"Syntax: "+ChatColor.GRAY+"/bpaddflag farewell <message>"});
				}else{
					plugin.sendMessage(player, MsgType.ERROR, "Protection Flag not recognized!");
				}
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return CommandType.FLAGS;
	}

}
