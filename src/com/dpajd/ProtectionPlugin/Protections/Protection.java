package com.dpajd.ProtectionPlugin.Protections;

import java.util.ArrayList;
import java.util.Arrays;

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
		WELCOME,
		FAREWELL,
		/*GHAST,ENDER_DRAGON,WITHER*/;
		
		public static ProtectionType getTypeFromName(String name){
			for (ProtectionType p : ProtectionType.values()){
				if (p.name().equalsIgnoreCase(name)) return p;
			}
			return null;
		}
		
		public boolean isMessageFlag(){
			return new ArrayList<ProtectionType>(Arrays.asList(ProtectionType.WELCOME,ProtectionType.FAREWELL)).contains(this);
		}
	}
	
	public Protection(Main plugin){
		this.plugin = plugin;
	}
	
	public abstract ProtectionType getType();
	
}
