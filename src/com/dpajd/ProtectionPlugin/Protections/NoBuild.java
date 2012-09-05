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
		return ProtectionType.NO_BUILD;
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		Region r = plugin.getRegion(e.getBlock().getChunk());
		if (r != null){
			if (!r.hasAccess(e.getPlayer().getName()) && r.hasProtection(this.getType())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e){
		Region r = plugin.getRegion(e.getBlock().getChunk());
		if (r != null){
			if (!r.hasAccess(e.getPlayer().getName()) && r.hasProtection(this.getType())){
				e.setCancelled(true);
			}
		}
	}
}
