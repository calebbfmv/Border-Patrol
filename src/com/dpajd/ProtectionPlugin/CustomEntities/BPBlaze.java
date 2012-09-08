package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.Blaze;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPBlaze extends net.minecraft.server.EntityBlaze implements BPCustomEntity{

	public BPBlaze(World world) {
		this(world,plugin);
	}
	
	public BPBlaze(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
	}

	@Override
	public void h_(){
		Blaze blaze = (Blaze) this.getBukkitEntity();
		
		Location from = new Location(blaze.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(blaze.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(blaze,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !blaze.isDead()){
			return;
		}
		
		super.h_();
	}
}
