package com.dpajd.ProtectionPlugin.Regions;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

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
	
	@Deprecated
	public Region getRegion(){
		// XXX: Get the region the ChunkData is associated with.
		return null;
	}
	
}
