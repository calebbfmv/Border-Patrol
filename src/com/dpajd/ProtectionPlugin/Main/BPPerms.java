package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.entity.Player;

public class BPPerms {
	public static boolean canCreate(Player p){
		return (p.hasPermission("BP.protect"));
	}
	public static boolean canRemove(Player p){
		ChunkRegion cr = ChunkRegion.getRegionAt(p.getLocation());
		if (cr != null){
			if (cr.getOwner().equals(p.getName()) || isAdmin(p)) return true;
		}
		return false;
	}
	public static boolean isAdmin(Player p){
		return p.hasPermission("BP.admin");
	}
}
