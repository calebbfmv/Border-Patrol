package com.dpajd.ProtectionPlugin.CustomEntities;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import net.minecraft.server.World;

public class BPZombie extends net.minecraft.server.EntityZombie implements BPCustomEntity{

	public BPZombie(World world) {
		this(world, plugin);
	}
	
	public BPZombie(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		Zombie zombie = (Zombie) this.getBukkitEntity();
		
		Location from = new Location(zombie.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(zombie.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(zombie,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !zombie.isDead()){
			return;
		}
		
		super.h_();
	}

}
