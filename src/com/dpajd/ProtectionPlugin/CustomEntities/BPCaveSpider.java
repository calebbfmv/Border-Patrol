package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.CaveSpider;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPCaveSpider extends net.minecraft.server.EntityCaveSpider implements BPCustomEntity{
	public BPCaveSpider(World world) {
		this(world, plugin);
	}
	
	public BPCaveSpider(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		CaveSpider caveSpider = (CaveSpider) this.getBukkitEntity();
		
		Location from = new Location(caveSpider.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(caveSpider.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(caveSpider,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !caveSpider.isDead()){
			return;
		}
		
		super.h_();
	}
}
