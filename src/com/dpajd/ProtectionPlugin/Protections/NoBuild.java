package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoBuild extends Protection{

	public NoBuild(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.BUILD;
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				Region r = plugin.getRegion(e.getBlock().getChunk());
				if (r != null){
					if (!r.hasAccess(e.getPlayer().getName()) && r.hasProtection(this.getType())){
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				Region r = plugin.getRegion(e.getBlock().getChunk());
				if (r != null){
					if (!r.hasAccess(e.getPlayer().getName()) && r.hasProtection(this.getType())){
						e.setCancelled(true);
					}
				}
			}	
		}
	}
}
