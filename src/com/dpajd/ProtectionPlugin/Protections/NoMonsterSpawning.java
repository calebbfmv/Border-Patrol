package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoMonsterSpawning extends Protection{
	
	public NoMonsterSpawning(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.NO_MONSTER_SPAWNING;
	}
	
	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getEntity().getLocation().getChunk());
			if (r != null){
				if (r.hasProtection(this.getType())){
					if (e.getEntity() instanceof Monster) e.setCancelled(true);
				}
			}
		}
	}

}
