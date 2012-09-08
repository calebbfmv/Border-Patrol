package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.Plugin;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPSpider extends net.minecraft.server.EntitySpider implements BPCustomEntity{
	public BPSpider(World world) {
		this(world, plugin);
	}
	
	public BPSpider(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		Spider spider = (Spider) this.getBukkitEntity();
		
		Location from = new Location(spider.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(spider.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(spider,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !spider.isDead()){
			return;
		}
		
		super.h_();
	}
}
