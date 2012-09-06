package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoMobDamage extends Protection{

	public NoMobDamage(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.MOB_DAMAGE;
	}

	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getEntity().getLocation().getChunk());
			if (r != null){
				if (r.hasProtection(this.getType())){
					// If an animal is under attack in region
					if (e.getEntity() instanceof Animals){
						if (e.getDamager() instanceof Player){
							e.setCancelled(true);
						}else if (e.getDamager() instanceof Projectile){
							Projectile p = (Projectile) e.getDamager();
							if (p.getShooter() != null){
								if (p.getShooter() instanceof Player) e.setCancelled(true);
							}
						}else if (e.getDamager() instanceof Monster){
							if (Math.random() > 0.2d){
								e.setCancelled(true);
							}
						}
					// If a player is under attack in region
					}else if (e.getEntity() instanceof Player){
						if (e.getDamager() instanceof Monster){
							e.setCancelled(true);
						}else if(e.getDamager() instanceof Projectile){
							Projectile p = (Projectile) e.getDamager();
							if (p.getShooter() != null){
								if (p.getShooter() instanceof Monster) e.setCancelled(true);
							}
						}else if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)){
							e.setCancelled(true);
						}
					}
				}
			}

		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (e.getEntity() instanceof Player){
				Region r = plugin.getRegion(e.getEntity().getLocation().getChunk());
			}
		}
	}
}
