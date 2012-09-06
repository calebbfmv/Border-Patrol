package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.CommandExecutor;

import com.dpajd.ProtectionPlugin.Main.Main;

public abstract class BPCommand implements CommandExecutor{
	Main plugin;
	
	public BPCommand(Main plugin){
		this.plugin = plugin;
	}
	
}
