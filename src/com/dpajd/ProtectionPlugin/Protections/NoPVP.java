package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.dpajd.ProtectionPlugin.Main.Main;

public class NoPVP extends Protection{

	public NoPVP(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.PVP;
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (e.getEntity() instanceof Player){
				if (e.getDamager() instanceof Player){
					e.setCancelled(true);
				}else if (e.getDamager() instanceof Projectile){
					Projectile projectile = (Projectile) e.getDamager();
					if (projectile.getShooter() != null){
						if (projectile.getShooter() instanceof Player){
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
}
