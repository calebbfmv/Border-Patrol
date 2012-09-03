package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.Material;

public class BPConfig {
	private Main plugin;
	private Material tool;
	
	public BPConfig(Main plugin){
		this.plugin = plugin;
	}
	
	private void defaultConfig(){
		plugin.getConfig().addDefault("Item.InHand",286);
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public void loadConfig(){
		tool = Material.getMaterial(plugin.getConfig().getInt("Item.InHand", 286));
	}

}
