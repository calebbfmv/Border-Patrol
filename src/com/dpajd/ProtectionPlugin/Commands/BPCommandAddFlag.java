package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandAddFlag extends BPCommand{

	public BPCommandAddFlag(Main plugin) {
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
						ProtectionType type = ProtectionType.getTypeFromName(args[0]);
						if (type != null){
							r.addProtection(type);
							r.saveRegion();
							plugin.sendMessage(player, "Added " + type.name() + " protection to the region.");
						}else{
							plugin.sendMessage(player, MsgType.ERROR, "Not a valid Protection Type!");
						}	
					}
				}else{
					plugin.sendMessage(player, MsgType.ERROR, "Cannot add a protection to this chunk!");
				}
			}else{
				plugin.sendMessage(player, MsgType.ERROR, "You must provide a flag name!");
			}
		}
		return true;
	}

}
