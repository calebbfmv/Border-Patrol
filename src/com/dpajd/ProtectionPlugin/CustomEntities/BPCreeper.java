package com.dpajd.ProtectionPlugin.CustomEntities;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

import net.minecraft.server.World;

public class BPCreeper extends net.minecraft.server.EntityCreeper implements BPCustomEntity{

	public BPCreeper(World world) {
		this(world,plugin);
	}

	public BPCreeper(World world, Plugin plugin){
		super(world);
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
	}
	
	@Override
	public void h_(){
		Creeper creeper = (Creeper) this.getBukkitEntity();
		
		Location from = new Location(creeper.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(creeper.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(creeper,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !creeper.isDead()){
			return;
		}
		
		super.h_();
	}
}
