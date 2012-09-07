package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.dpajd.ProtectionPlugin.Main.Main;

public class NoUse extends Protection{

	public NoUse(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.USE;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		// TODO: Prevent switch,button,door,trapdoor,gate activation/opening
	}

	// XXX: Hook redstone change event for external sources?
}
