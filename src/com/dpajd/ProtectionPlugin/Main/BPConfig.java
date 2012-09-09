package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;

public class BPConfig {
	private Main plugin;
	
	// Config variables
	private Material tool;
	private List<Integer> sizes;
	private ArrayList<ProtectionType> protections;
	private boolean customEntities;
	
	public BPConfig(Main plugin){
		this.plugin = plugin;
		loadConfig(true);
	}
	
	private void defaultConfig(){
		plugin.getConfig().addDefault("Wand",Material.GOLD_AXE.name());
		plugin.getConfig().addDefault("RegionSizes", new String[]{"1","3"});
		for (ProtectionType type : ProtectionType.values()){
			plugin.getConfig().addDefault("Protections."+type.name(),true);
		}
		plugin.getConfig().addDefault("CustomEntityEvents", true);
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	/**
	 * @param defaultConfig Write the defaults to the config
	 */
	public void loadConfig(boolean defaultConfig){
		if (defaultConfig) defaultConfig();
		loadConfig();
	}
	
	private void loadConfig(){
		// tool
		tool = getMat(plugin.getConfig().getString("Wand", Material.GOLD_AXE.name()));	
		if (tool == null) tool = Material.GOLD_AXE;
		// sizes
		sizes = plugin.getConfig().getIntegerList("RegionSizes");
		// custom entity events
		customEntities = plugin.getConfig().getBoolean("CustomEntityEvents", true);
		// protections
		protections = new ArrayList<ProtectionType>();
		for (String protection : plugin.getConfig().getConfigurationSection("Protections").getKeys(false)){
			if (plugin.getConfig().getBoolean("Protections." + protection)){
				protections.add(ProtectionType.getTypeFromName(protection));
			}
		}
	}
	
	public boolean hasProtection(ProtectionType type){
		return protections.contains(type);
	}
	
	public boolean getEntitiesEnabled(){
		return customEntities;
	}
	
	public Material getTool(){
		return tool;
	}
	
	public List<Integer> getSizes(){
		return sizes;
	}
	
	private Material getMat(String s){
		if (isInt(s)){
			return Material.getMaterial(Integer.parseInt(s));
		}else{
			return Material.getMaterial(s);
		}
	}
	
	private boolean isInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){return false;}
	}

}
