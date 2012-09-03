package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ChunkRegion {
	
	private int x, z;
	private HashSet<String> access = new HashSet<String>();
	private String owner, world;
	
	public ChunkRegion(Location loc, String owner) {
		this.owner = owner;
		access.add(owner);
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();		
		world = loc.getWorld().getName();
	}
	
	public ChunkRegion(int x, int z, String world, String owner, ArrayList<String> access){
		this.x = x;
		this.z = z;
		this.world = world;
		this.owner = owner;
		this.access.addAll(access);
	}
	
	public String getId(){
		return "" + x + z;
	}
	
	public boolean hasAccess(String name){
		return access.contains(name);
	}
	
	public boolean isInside(Block b){
		return Bukkit.getWorld(world).getChunkAt(x, z).equals(b.getChunk());
	}
	
	public void addAccess(String name){
		access.add(name);
	}
	
	public void removeAccess(String name){
		access.remove(name);	
	}
	
	public static ChunkRegion loadRegion(String id){
		// TODO: Read region from file
		return null;
	}
	
	public static void saveRegion(ChunkRegion cr){
		// TODO: Save region to file
	}
}
