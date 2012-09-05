package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class ElectricFence extends Protection{
	
	public ElectricFence(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.ELECTRIC_FENCE;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
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
					plugin.sendMessage(e.getPlayer(), MsgType.DENIED, "You aren't permitted entry!");
				}
			}
		}
	}
	
}
