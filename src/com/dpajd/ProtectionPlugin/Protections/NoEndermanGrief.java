package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoEndermanGrief extends Protection{

	public NoEndermanGrief(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.ENDERMAN_GRIEF;
	}

	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getBlock().getChunk());
			if (r != null){
				if (e.getEntity() instanceof Enderman){
					if (r.hasProtection(this.getType())){
						e.setCancelled(true);
					}
				}
			}
		}
	}
}
