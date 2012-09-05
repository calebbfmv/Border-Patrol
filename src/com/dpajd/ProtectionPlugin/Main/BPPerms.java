package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPPerms {
	
	public static boolean canCreate(Player p){
		Region r = ((Main)Bukkit.getPluginManager().getPlugin("Border Patrol")).getRegion(p.getLocation().getChunk());
		if (r != null){
			return false;
		}
		return (p.hasPermission("BP.protect"));
	}
	
	public static boolean canRemove(Player p){
		Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Protection");
		Region r = plugin.getRegion(p.getLocation().getChunk());
		if (r != null){
			if(r.getOwner().getName().equals(p.getName()) && p.hasPermission("BP.remove.self")){
				return true;
			}else if(!r.getOwner().getName().equals(p.getName()) && p.hasPermission("BP.remove.others")){
				return true;
			}else if (isAdmin(p)) return true;
		}
		return false;
	}
	
	public static boolean isAdmin(Player p){
		return p.hasPermission("BP.admin");
	}
	
	public static boolean isDonator(Player p){
		return p.hasPermission("BP.donator");
	}
	
	public static boolean canUse(Player p){
		return p.hasPermission("BP.use");
	}
}
