package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ChunkRegion {
	
	private int x, z;
	private HashSet<String> access = new HashSet<String>();
	private String owner, world, id;
	
	public ChunkRegion(Location loc, String owner) {
		this.owner = owner;
		access.add(owner);
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();		
		world = loc.getWorld().getName();
		id = "1";
	}
	
	public ChunkRegion(int x, int z, String world, String owner, ArrayList<String> access){
		this.x = x;
		this.z = z;
		this.world = world;
		this.owner = owner;
		this.access.addAll(access);
		id = "1";
		//Come back to here because you cant spell right.
	}
	
	private static int getNewId(){
		return 0;
		//Come back. Get unique Id. Whenever sleepy head comes back.
	}
	
	public String getId(){
		return id;
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
		if(access.contains(name)){
			access.remove(name);	
		}
	}
	
	public ChunkRegion loadRegion(String regionName){
		return null;
	}
	
	
	// TODO: Write Chunk Region to a freaking file. Static=easier.
	// TODO Read it as well. -_- weee.
}
