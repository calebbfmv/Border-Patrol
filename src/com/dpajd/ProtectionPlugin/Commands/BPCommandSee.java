package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandSee extends BPCommand{

	public BPCommandSee(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			Region r = plugin.getRegion(player.getLocation().getChunk());
			if (r != null){
				plugin.sendMessage(player, new String[]{
						"Chunk '"+ ChatColor.GRAY + r.getName()+ ChatColor.GOLD + "' is owned by: " + ChatColor.GRAY + r.getOwner().getName(),
						"Width: " + ChatColor.GRAY + (int) Math.sqrt(r.getChunks().size()),
						"Created: " + ChatColor.GRAY + r.getDateCreated(),
						"Protections: " + ChatColor.GRAY + r.getProtections(),
						"Members: " + ChatColor.GRAY + r.getMembers()
						});
			}else{
				plugin.sendMessage(player, "This chunk is empty.");
			}
		}
		return true;
	}

}
