package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.Main;

public class BPCommandTool extends BPCommand{

	public BPCommandTool(Main plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			plugin.toggle(plugin.toolEnabled,player.getName());
			plugin.sendMessage(player, "Toggled tool: " + ((plugin.toolEnabled.contains(player.getName()))?"On":"Off"));
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return null;
	}

}
