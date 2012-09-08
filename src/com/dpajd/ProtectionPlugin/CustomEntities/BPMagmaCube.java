package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.MagmaCube;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPMagmaCube extends net.minecraft.server.EntityMagmaCube implements BPCustomEntity{
	public BPMagmaCube(World world) {
		this(world, plugin);
	}
	
	public BPMagmaCube(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		MagmaCube magmaCube = (MagmaCube) this.getBukkitEntity();
		
		Location from = new Location(magmaCube.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(magmaCube.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPEntityMoveEvent e = new BPEntityMoveEvent(magmaCube,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !magmaCube.isDead()){
			return;
		}
		
		super.h_();
	}
}
