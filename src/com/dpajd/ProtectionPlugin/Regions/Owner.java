package com.dpajd.ProtectionPlugin.Regions;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Owner extends Member{
	private File regionFile = null;
	private FileConfiguration regionYaml = null;
	private ArrayList<Region> regionList = new ArrayList<Region>();
	
	public Owner(Player p) {
		super(p);
		this.regionFile = new File(plugin.getDataFolder().getPath() + File.separator + "regions" + File.separator +  this.name +".yml");
		this.regionYaml = YamlConfiguration.loadConfiguration(regionFile);
		getRegions();
	}
	
	public Owner(String name){
		super(name);
		this.regionFile = new File(plugin.getDataFolder().getPath() + File.separator + "regions" + File.separator +  this.name +".yml");
		this.regionYaml = YamlConfiguration.loadConfiguration(regionFile);
		getRegions();
	}
	
	private void updateRegions(){
		ArrayList<Region> regionList = new ArrayList<Region>();
		for (Region r : plugin.getRegions()){
			if (r.getOwner().getName().equals(this.getName())) regionList.add(r);
		}
		this.regionList = regionList;
	}
	
	public ArrayList<Region> getRegions(){
		if (regionList.size() == 0){
			updateRegions();
		}
		return this.regionList;
	}
	
	public void deleteRegions(){
		for (Region r : getRegions()){
			r.deleteRegion();
			plugin.removeRegion(r);
		}
	}
	
	public int getRegionCount(){
		return getRegions().size();
	}
	
	public FileConfiguration getRegionDataFile(){
		return regionYaml;
	}

}
