package com.dpajd.ProtectionPlugin.Protections;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoVehicles extends Protection{

	public NoVehicles(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.VEHICLES;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (e.getClickedBlock() != null){
				Region r = plugin.getRegion(e.getClickedBlock().getLocation().getChunk());
				if (r != null){
					if (r.hasProtection(this.getType())){
						if (new ArrayList<Material>(Arrays.asList(
								Material.BOAT, Material.MINECART
								)).contains(e.getPlayer().getItemInHand().getType())){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
}
