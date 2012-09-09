package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPPerms {
	
	public static boolean canCreate(Player p){
		Region r = ((Main)Bukkit.getPluginManager().getPlugin("Border Patrol")).getRegion(p.getLocation().getChunk());
		if (r != null){
			return false;
		}
		return (p.hasPermission("BP.command.protect"));
	}
	
	public static boolean canRemove(Player p){
		Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Protection");
		Region r = plugin.getRegion(p.getLocation().getChunk());
		if (r != null){
			if(r.getOwner().getName().equals(p.getName()) && p.hasPermission("BP.command.remove.self")){
				return true;
			}else if(p.hasPermission("BP.command.remove.others")){
				return true;
			}else if (isAdmin(p)) return true;
		}
		return false;
	}
	
	public static boolean canBypass(Player p){
		return p.hasPermission("BP.command.bypass");
	}
	
	public static boolean canSee(Player p, Region r){
		return (canUse(p) || r.hasAccess(p));
	}
	
	public static boolean isAdmin(Player p){
		return p.hasPermission("BP.admin");
	}
	
	public static boolean canUse(Player p){
		return p.hasPermission("BP.command.use");
	}
	
	public static boolean hasFlag(Player p, ProtectionType type){
		return p.hasPermission("BP.flag." + type.name());
	}
}
