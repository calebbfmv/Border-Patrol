package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoInteract extends Protection{

	public NoInteract(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.INTERACT;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (e.getClickedBlock() != null){
				Region r = plugin.getRegion(e.getClickedBlock().getChunk());
				if (r != null){
					if (r.hasProtection(this.getType())){
						if (!r.hasAccess(e.getPlayer().getName())) e.setCancelled(true);
					}
				}
			}
		}

	}

}
