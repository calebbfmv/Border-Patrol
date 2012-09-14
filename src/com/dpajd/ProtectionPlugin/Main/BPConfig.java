package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.dpajd.ProtectionPlugin.Commands.BPCommand.CommandType;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;

public class BPConfig {
	private Main plugin;
	
	// Config variables
	private Material tool;
	private List<Integer> sizes;
	private ArrayList<ProtectionType> protections;
	private boolean customEntities;
	// economy
	private EconPrices econPrices;
	private Economy economy;
	private boolean economyEnabled;
	
	public BPConfig(Main plugin){
		this.plugin = plugin;
		economyEnabled = setupEconomy();
		loadConfig(true);
	}
	
	private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	public Economy getEcon(){
		return economy;
	}
	
	private void defaultConfig(){
		plugin.getConfig().addDefault("Wand",Material.GOLD_AXE.name());
		plugin.getConfig().addDefault("RegionSizes", new String[]{"1","3"});
		for (ProtectionType type : ProtectionType.values()){
			plugin.getConfig().addDefault("Protections."+type.name(),true);
			plugin.getConfig().addDefault("Economy.Protections."+type.name()+".Enabled", true);
			plugin.getConfig().addDefault("Economy.Protections."+type.name()+".Cost", 100D);
		}
		plugin.getConfig().addDefault("CustomEntityEvents", true);
		
		plugin.getConfig().addDefault("Economy.Enabled", true);
		for (CommandType cmd : CommandType.values()){
			plugin.getConfig().addDefault("Economy.Commands." + cmd.name()+".Enabled", cmd.getState());
			if (cmd.getState()){ // if default give a price, else don't.
				plugin.getConfig().addDefault("Economy.Commands." + cmd.name() + ".Cost", 100D);
			}else{
				plugin.getConfig().addDefault("Economy.Commands." + cmd.name() + ".Cost", 0D);
			}
		}
		
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
		// economy
		econPrices = new EconPrices(economyEnabled);
		if (plugin.getConfig().getBoolean("Economy.Enabled")){
			for (String protection : plugin.getConfig().getConfigurationSection("Economy.Protections").getKeys(false)){
				this.econPrices.setPrice(ProtectionType.getTypeFromName(protection), plugin.getConfig().getDouble("Economy.Protections."+protection+".Cost"));
			}
			for (String command : plugin.getConfig().getConfigurationSection("Economy.Commands").getKeys(false)){
				this.econPrices.setPrice(CommandType.getTypeFromName(command), plugin.getConfig().getDouble("Economy.Commands."+command+".Cost"));
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
	
	public EconPrices getEconPrices(){
		return this.econPrices;
	}

	public class EconPrices{
		private HashMap<ProtectionType,Double> protectionPrices = new HashMap<ProtectionType,Double>();
		private HashMap<CommandType,Double> commandPrices = new HashMap<CommandType,Double>();
		private boolean enabled = false;
		
		public EconPrices(boolean enabled){
			this.enabled = enabled;
		}
		
		public boolean isEnabled(){
			return enabled;
		}
		
		public void setPrice(ProtectionType protection, double price){
			if (enabled) protectionPrices.put(protection, price);
		}
		
		public void setPrice(CommandType command, double price){
			if (enabled) commandPrices.put(command, price);
		}
		
		public double getPrice(ProtectionType protection){
			if (enabled) return protectionPrices.get(protection);
			else return 0;
		}
		
		public double getPrice(CommandType command){
			if (enabled) return commandPrices.get(command);
			else return 0;
		}
		
	}
}
