package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.dpajd.ProtectionPlugin.Main.Main;

public class BPCommandFlags extends BPCommand{

	public BPCommandFlags(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// TODO: Send player list of enabled flags
		return true;
	}

}
