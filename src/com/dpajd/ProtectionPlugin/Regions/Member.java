package com.dpajd.ProtectionPlugin.Regions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.Main;

public class Member {
	String name;
	Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Patrol");
	
	public Member(Player p){
		this.name = p.getName();
	}
	
	public Member(String name){
		this.name = name;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(name);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isOnline(){
		return Bukkit.getPlayer(name).equals(null);
	}
	
	public String toString(){
		return this.name;
	}
}
