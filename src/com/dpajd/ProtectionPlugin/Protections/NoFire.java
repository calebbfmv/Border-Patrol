package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoFire extends Protection{

	public NoFire(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.NO_FIRE;
	}

	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getBlock().getChunk());
			if (r != null){
				if (r.hasProtection(this.getType())) e.setCancelled(true);
			}
		}

	}
	
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getBlock().getChunk());
			if (r != null){
				if (r.hasProtection(this.getType())) e.setCancelled(true);
			}
		}
	}
}
