package com.dpajd.ProtectionPlugin.Commands;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.command.CommandExecutor;

import com.dpajd.ProtectionPlugin.Main.BPConfig;
import com.dpajd.ProtectionPlugin.Main.BPConfig.EconPrices;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;

public abstract class BPCommand implements CommandExecutor{
	public enum CommandType{
		ADDFLAG(false),
		BYPASS(false),
		COUNT(false),
		CREATE(true),
		FAITH(true),
		FLAGS(false),
		HELP(false),
		REMOVE(true),
		REMOVEFLAG(true),
		SEE(false),
		UNFAITH(true);
		
		private boolean state;
		CommandType(boolean state){
			this.state = state;
		}
		
		public boolean getState(){
			return this.state;
		}
				
		public void setState(boolean state){
			this.state = state;
		}
		
		public static CommandType getTypeFromName(String name){
			for (CommandType cmd : CommandType.values()){
				if (cmd.name().equalsIgnoreCase(name)) return cmd;
			}
			return null;
		}
		
	}
	
	Main plugin;
	BPConfig settings;
	Economy econ;
	EconPrices prices;
	boolean econEnabled;
	
	public BPCommand(Main plugin){
		this.plugin = plugin;
		this.settings = plugin.getSettings();
		if (plugin.getSettings().getEconPrices().isEnabled()){
			this.econ = plugin.getSettings().getEcon();
		}
		this.econEnabled = this.econ != null;
		this.prices = this.settings.getEconPrices();
	}
	
	public abstract CommandType getType();
	
	public Double cost(){
		if (econEnabled) return prices.getPrice(getType());
		else return 0d;
	}
	
	public Double cost(ProtectionType type){
		if (econEnabled) return prices.getPrice(type);
		else return 0d;
	}
	
}
