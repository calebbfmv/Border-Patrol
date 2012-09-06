package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Member;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandFaith extends BPCommand{

	public BPCommandFaith(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args != null){
				Region r = plugin.getRegion(player.getLocation().getChunk());
				if (r != null){
					if (r.getOwner().getName().equals(player.getName()) || BPPerms.isAdmin(player)){
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null){
							r.addMember(new Member(target));
							r.saveRegion();								
							plugin.sendMessage(player, target.getName() + " added to the region("+r.getName()+").");
							plugin.sendMessage(target, "You have been added to the region("+r.getName()+")");
						}else{
							plugin.sendMessage(player, MsgType.ERROR, "Player not found.");
						}
					}else{
						plugin.sendMessage(player, MsgType.DENIED, "You cannnot modify someone elses region!");
					}
				}else{
					plugin.sendMessage(player, MsgType.ERROR, "No region found to modify!");
				}
			}
		}
		return true;
	}

}
