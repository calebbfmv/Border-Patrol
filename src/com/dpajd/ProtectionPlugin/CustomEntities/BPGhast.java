package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;
import org.bukkit.Location;
import org.bukkit.entity.Ghast;
import org.bukkit.plugin.Plugin;
import com.dpajd.ProtectionPlugin.CustomEvents.BPEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPGhast extends net.minecraft.server.EntityGhast implements BPCustomEntity{
	public BPGhast(World world) {
		this(world, plugin);
	}
	
	public BPGhast(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		Ghast ghast = (Ghast) this.getBukkitEntity();
		
		Location from = new Location(ghast.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(ghast.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPEntityMoveEvent e = new BPEntityMoveEvent(ghast,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !ghast.isDead()){
			return;
		}
		
		super.h_();
	}
}
