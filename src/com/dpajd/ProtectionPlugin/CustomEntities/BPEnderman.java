package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.plugin.Plugin;
import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPEnderman extends net.minecraft.server.EntityEnderman implements BPCustomEntity{
	public BPEnderman(World world) {
		this(world,plugin);
	}
	
	public BPEnderman(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
	}

	@Override
	public void h_(){
		Enderman enderman = (Enderman) this.getBukkitEntity();
		
		Location from = new Location(enderman.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(enderman.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(enderman,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !enderman.isDead()){
			return;
		}
		
		super.h_();
	}
}
