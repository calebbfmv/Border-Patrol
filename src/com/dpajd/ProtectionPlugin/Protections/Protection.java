package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.Listener;
import com.dpajd.ProtectionPlugin.Main.Main;

public abstract class Protection implements Listener{
	Main plugin;
	
	public enum ProtectionType{
		
		ELECTRIC,
		ENTRY,
		EXPLOSION,
		FIRE,
		ENDERMAN,
		WATER_FLOW,
		LAVA_FLOW,
		PISTON,
		BUILD,
		CHEST_ACCESS,
		USE,
		MONSTER_SPAWNING, 
		PVP,
		MOB_DAMAGE,
		VEHICLES,
		/*GHAST,ENDER_DRAGON,WITHER*/;
		
		public static ProtectionType getTypeFromName(String name){
			for (ProtectionType p : ProtectionType.values()){
				if (p.name().equalsIgnoreCase(name)) return p;
			}
			return null;
		}
	}
	
	public Protection(Main plugin){
		this.plugin = plugin;
	}
	
	public abstract ProtectionType getType();

	public Main getPlugin() {
		return plugin;
	}
	
}
