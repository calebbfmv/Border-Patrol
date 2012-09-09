package com.dpajd.ProtectionPlugin.Regions;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import com.dpajd.ProtectionPlugin.Main.Main;

public class ChunkData {
	private int chunkX, chunkZ;
	private String world;
	
	public ChunkData(Chunk chunk){
		this.world = chunk.getWorld().getName();
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
	}
	public ChunkData(World world, int chunkX, int chunkZ){
		this.world = world.getName();
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}
	public ChunkData(String world, int chunkX, int chunkZ){
		this.world = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}
	
	public int getChunkX(){
		return chunkX;
	}
	
	public int getChunkZ(){
		return chunkZ;
	}
	
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	public Chunk getChunk(){
		return getWorld().getChunkAt(chunkX, chunkZ);
	}
	
	public String toString(){
		return "["+chunkX+","+chunkZ+"]";
	}
	
	public Region getRegion(){
		Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Patrol");
		for (Region r : plugin.getRegions()){
			for (ChunkData c : r.getChunks()){
				if (c.getChunk().equals(this.getChunk())) return r;
			}
		}
		return null;
	}
	
}
