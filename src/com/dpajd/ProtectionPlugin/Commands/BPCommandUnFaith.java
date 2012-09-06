package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Member;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandUnFaith extends BPCommand{

	public BPCommandUnFaith(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length > 0){
				Region r = plugin.getRegion(player.getLocation().getChunk());
				if (r != null){
					if (r.getOwner().getName().equals(player.getName()) || BPPerms.isAdmin(player)){
						Player target = Bukkit.getPlayer(args[0]);
						if (target != null){
							r.removeMember(new Member(target));
							r.saveRegion();								
							plugin.sendMessage(player, target.getName() + " removed from the region("+r.getName()+").");
							plugin.sendMessage(target, "You have been removed from the region("+r.getName()+")");
						}else{
							OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(args[0]);
							if (targetOffline != null){
								r.removeMember(new Member(target));
								r.saveRegion();								
								plugin.sendMessage(player, targetOffline.getName() + " removed from the region("+r.getName()+").");
							}else{
								plugin.sendMessage(player, MsgType.ERROR, "Player doesn't exist.");
							}
						}
					}else{
						plugin.sendMessage(player, MsgType.DENIED, "You cannnot modify someone elses region!");
					}
				}else{
					plugin.sendMessage(player, MsgType.ERROR, "No region found to modify!");
				}
			}else{
				plugin.sendMessage(player, MsgType.ERROR, "You must provide a player name!");
			}
		}
		return true;
	}

}
