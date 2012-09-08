package com.dpajd.ProtectionPlugin.CustomEvents;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPEntityMoveEvent extends Event implements Cancellable{
	
	static Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Patrol");
	static final HandlerList handlers = new HandlerList();
	
	boolean isCancelled = false;
	
	LivingEntity entity;
	Location from;
	Location to;
	
	public BPEntityMoveEvent(LivingEntity entity, Location from, Location to){
		this.entity = entity;
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean canceled) {
		this.isCancelled = canceled;
	}

	public LivingEntity getCreature(){
		return this.entity;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	public Location getFrom(){
		return from;
	}
	
	public Location getTo(){
		return this.to;
	}
	
	public Region getFromRegion(){
		return plugin.getRegion(from.getChunk());
	}
	
	public Region getToRegion(){
		return plugin.getRegion(to.getChunk());
	}
}
