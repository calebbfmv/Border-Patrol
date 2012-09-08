package com.dpajd.ProtectionPlugin.Protections;

import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntitySlime;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import com.dpajd.ProtectionPlugin.CustomEvents.BPEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class ElectricFence extends Protection{
	
	public ElectricFence(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.ELECTRIC;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				if (plugin.isProtected(e.getTo().getChunk())){
					Region rTo = plugin.getRegion(e.getTo().getChunk());
					Region rFrom = plugin.getRegion(e.getFrom().getChunk());
					if (!rTo.equals(rFrom) && rTo.hasProtection(this.getType())){
						Player p = e.getPlayer();
						if (!rTo.hasAccess(p.getName())){
							
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 5));
							p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 15, 1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 1));
							
							p.getWorld().strikeLightningEffect(e.getPlayer().getLocation());
							
							p.setHealth((int) Math.floor(e.getPlayer().getHealth()/2));
							p.teleport(e.getFrom());
							
							Vector v = p.getLocation().getDirection();
							v.multiply(-1).add(new Vector(0,0.2,0));
							p.setVelocity(v);
							plugin.sendMessage(e.getPlayer(), MsgType.DENIED, "You aren't permitted entry! Ask " + rTo.getOwner().getName() + " to permit you.");
						}
					}
				}
				
			}
		}
	}
	
	@EventHandler
	public void onBPEntityMoveEvent(BPEntityMoveEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (plugin.isProtected(e.getTo().getChunk())){
				Region rTo = plugin.getRegion(e.getTo().getChunk());
				Region rFrom = plugin.getRegion(e.getFrom().getChunk());
				if (!rTo.equals(rFrom)){
					if (rTo.hasProtection(this.getType())){
						LivingEntity entity = e.getCreature();
						entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 5));
						entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 5));
						
						entity.getWorld().strikeLightningEffect(entity.getLocation());
						
						if (entity instanceof EntityCreeper){
							((EntityCreeper)entity).setPowered(true);
						}
						
						if (!(entity instanceof EntitySlime)){ // Prevent slime splitting by not damaging it
							entity.setHealth((int) Math.floor(entity.getHealth()/4));
						}
						entity.teleport(e.getFrom());
						
						Vector v = entity.getLocation().getDirection();
						v.multiply(-3).add(new Vector(0,0.5,0));
						entity.setVelocity(v);
					}
				}
			}
		}
	}
	
}
