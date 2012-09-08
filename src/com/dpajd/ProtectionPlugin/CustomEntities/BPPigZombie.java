package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;
import org.bukkit.Location;
import org.bukkit.entity.PigZombie;
import org.bukkit.plugin.Plugin;
import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPPigZombie extends net.minecraft.server.EntityPigZombie implements BPCustomEntity{
	public BPPigZombie(World world) {
		this(world, plugin);
	}
	
	public BPPigZombie(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		PigZombie pigZombie = (PigZombie) this.getBukkitEntity();
		
		Location from = new Location(pigZombie.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(pigZombie.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(pigZombie,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !pigZombie.isDead()){
			return;
		}
		
		super.h_();
	}
}
