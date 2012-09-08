package com.dpajd.ProtectionPlugin.CustomEntities;

import net.minecraft.server.World;
import org.bukkit.Location;
import org.bukkit.entity.Silverfish;
import org.bukkit.plugin.Plugin;
import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class BPSilverfish extends net.minecraft.server.EntitySilverfish implements BPCustomEntity{
	public BPSilverfish(World world) {
		this(world, plugin);
	}
	
	public BPSilverfish(World world, Plugin plugin){
		super(world);
		
		if (plugin == null || !(plugin instanceof Main)){
			this.world.removeEntity(this);
			return;
		}
		
	}
	
	@Override
	public void h_(){
		Silverfish silverfish = (Silverfish) this.getBukkitEntity();
		
		Location from = new Location(silverfish.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
		Location to = new Location(silverfish.getWorld(), this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		
		BPHostileEntityMoveEvent e = new BPHostileEntityMoveEvent(silverfish,from,to);
		
		this.world.getServer().getPluginManager().callEvent(e);
		
		if (e.isCancelled() && !silverfish.isDead()){
			return;
		}
		
		super.h_();
	}
}
