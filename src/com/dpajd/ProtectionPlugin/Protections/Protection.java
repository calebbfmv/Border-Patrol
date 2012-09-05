package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.Listener;
import com.dpajd.ProtectionPlugin.Main.Main;

public abstract class Protection implements Listener{
	Main plugin;
	
	public enum ProtectionType{
		
		ELECTRIC_FENCE,
		NO_ENTRY,
		NO_EXPLOSION,
		NO_FIRE,
		NO_ENDERMAN_GRIEF,
		NO_WATER_FLOW,
		NO_LAVA_FLOW,
		NO_PISTON_GRIEF,
		NO_BUILD,
		NO_INTERACT;
		
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
