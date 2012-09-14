package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandRemove extends BPCommand{

	public BPCommandRemove(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			Chunk ch = player.getLocation().getChunk();
			Region r = plugin.getRegion(ch);
			if (r != null){
				if (r.getOwner().getName().equals(player.getName()) || BPPerms.isAdmin(player)){
					plugin.deleteRegion(r);
					plugin.sendMessage(player, "Deleted chunk region");
				}
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return CommandType.REMOVE;
	}

}
