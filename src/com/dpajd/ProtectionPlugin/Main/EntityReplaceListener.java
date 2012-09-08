package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import com.dpajd.ProtectionPlugin.CustomEntities.*;

public class EntityReplaceListener implements Listener{
	private Main plugin;
	
	public EntityReplaceListener(Main plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent e){
		Location location = e.getLocation();
		Entity entity = e.getEntity();
		EntityType creatureType = e.getEntityType();
		World world = location.getWorld();
		
		net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity).getHandle();
		
		switch (creatureType){
			case BLAZE:
				if (!(mcEntity instanceof BPBlaze)){
					BPBlaze blaze = new BPBlaze(mcWorld,plugin);
					blaze.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(blaze, SpawnReason.CUSTOM);
				}
				break;
			case CREEPER:
				if (!(mcEntity instanceof BPCreeper)){
					BPCreeper creeper = new BPCreeper(mcWorld,plugin);
					creeper.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(creeper, SpawnReason.CUSTOM);
				}
				break;
			case ENDERMAN:
				if (!(mcEntity instanceof BPEnderman)){
					BPEnderman enderman = new BPEnderman(mcWorld,plugin);
					enderman.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(enderman, SpawnReason.CUSTOM);
				}
				break;
			case GIANT:
				if (!(mcEntity instanceof BPGiant)){
					BPGiant giant = new BPGiant(mcWorld,plugin);
					giant.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(giant, SpawnReason.CUSTOM);
				}
				break;
			case SILVERFISH:
				if (!(mcEntity instanceof BPSilverfish)){
					BPSilverfish silverfish = new BPSilverfish(mcWorld,plugin);
					silverfish.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(silverfish, SpawnReason.CUSTOM);
				}
				break;
			case SKELETON:
				if (!(mcEntity instanceof BPSkeleton)){
					BPSkeleton skeleton = new BPSkeleton(mcWorld,plugin);
					skeleton.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(skeleton, SpawnReason.CUSTOM);
				}
				break;
			case SPIDER:
				if (!(mcEntity instanceof BPSpider)){
					BPSpider spider = new BPSpider(mcWorld,plugin);
					spider.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(spider, SpawnReason.CUSTOM);
				}
				break;
			case CAVE_SPIDER:
				if (!(mcEntity instanceof BPCaveSpider)){
					BPCaveSpider caveSpider = new BPCaveSpider(mcWorld,plugin);
					caveSpider.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(caveSpider, SpawnReason.CUSTOM);
				}
				break;
			case ZOMBIE:
				if (!(mcEntity instanceof BPZombie)){
					BPZombie zombie = new BPZombie(mcWorld,plugin);
					zombie.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(zombie, SpawnReason.CUSTOM);
				}
				break;
			case PIG_ZOMBIE:
				if (!(mcEntity instanceof BPPigZombie)){
					BPPigZombie pigZombie = new BPPigZombie(mcWorld,plugin);
					pigZombie.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(pigZombie, SpawnReason.CUSTOM);
				}
				break;
			case GHAST:
				if (!(mcEntity instanceof BPGhast)){
					BPGhast ghast = new BPGhast(mcWorld,plugin);
					ghast.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(ghast, SpawnReason.CUSTOM);
				}
				break;
			case SLIME:
				if (!(mcEntity instanceof BPSlime)){
					BPSlime slime = new BPSlime(mcWorld,plugin);
					slime.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(slime, SpawnReason.CUSTOM);
				}
				break;
			case MAGMA_CUBE:
				if (!(mcEntity instanceof BPMagmaCube)){
					BPMagmaCube magmaCube = new BPMagmaCube(mcWorld,plugin);
					magmaCube.setPosition(location.getX(), location.getY(), location.getZ());
					
					mcWorld.removeEntity(mcEntity);
					mcWorld.addEntity(magmaCube, SpawnReason.CUSTOM);
				}
				break;
				
			default: return;
		}
	}
	
}
