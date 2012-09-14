package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Owner;

public class BPCommandCount extends BPCommand{

	public BPCommandCount(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length > 0){
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null){
					OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);
					if (targetOffline != null){
						plugin.sendMessage(player, targetOffline.getName() + " has a total of " + new Owner(targetOffline.getName()).getRegionCount() + " region protections.");
					}else{
						plugin.sendMessage(player, MsgType.ERROR, "Player doesn't exist.");
					}
				}else{
					plugin.sendMessage(player, target.getName() + " has a total of " + new Owner(target).getRegionCount() + " region protections.");
				}
			}else{
				plugin.sendMessage(player, player.getName() + " has a total of " + new Owner(player).getRegionCount() + " region protections.");
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return CommandType.COUNT;
	}

}
