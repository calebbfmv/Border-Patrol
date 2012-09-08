package com.dpajd.ProtectionPlugin.CustomEvents;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.Cancellable;

public class BPHostileEntityMoveEvent extends BPEntityMoveEvent implements Cancellable{
	
	private Monster monster;
	
	public BPHostileEntityMoveEvent(Monster monster, Location from, Location to){
		super(monster,from,to);
		this.monster = monster;
	}
	
	public Monster getMonster(){
		return this.monster;
	}
	
	public LivingEntity getTarget(){
		return monster.getTarget();
	}
	
	public void setTarget(LivingEntity entity){
		monster.setTarget(entity);
	}
	
}
