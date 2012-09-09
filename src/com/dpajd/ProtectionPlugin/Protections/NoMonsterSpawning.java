package com.dpajd.ProtectionPlugin.Protections;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.entity.EntityType;
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
		return ProtectionType.MONSTER_SPAWNING;
	}
	
	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getEntity().getLocation().getChunk());
			if (r != null){
				if (r.hasProtection(this.getType())){
					if (e.getEntity() instanceof Monster){
						e.setCancelled(true);
					}else{
						ArrayList<EntityType> undesirables = new ArrayList<EntityType>(
								Arrays.asList(EntityType.SLIME,EntityType.MAGMA_CUBE,EntityType.GHAST));
						if (undesirables.contains(e.getEntityType())) e.setCancelled(true);
					}
				}
			}
		}
	}

}
