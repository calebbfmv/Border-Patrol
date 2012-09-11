package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoFire extends Protection{

	public NoFire(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.FIRE;
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
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				if (e.getClickedBlock() != null){
					Region r = plugin.getRegion(e.getClickedBlock().getChunk());
					if (r != null){
						if (r.hasProtection(this.getType())){
							if (e.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL){
								if (!r.hasAccess(e.getPlayer())){
									e.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}
}
