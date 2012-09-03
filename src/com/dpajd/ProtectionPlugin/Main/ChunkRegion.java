package com.dpajd.ProtectionPlugin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ChunkRegion {
	
	private int x, z;
	private HashSet<String> access = new HashSet<String>();
	private String owner, world;
	private static File chunkFile = new File("plugins" + File.separator + "BorderPatrol" + File.separator + "protections.yml");
	private static FileConfiguration chunkYaml = null;
	
	public ChunkRegion(Location loc, String owner, ArrayList<String> access) {
		this.owner = owner;
		access.add(owner);
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();		
		world = loc.getWorld().getName();
		this.access.addAll(access); // potential error?
	}
	
	public ChunkRegion(int x, int z, String world, String owner, ArrayList<String> access){
		this.x = x;
		this.z = z;
		this.world = world;
		this.owner = owner;
		this.access.addAll(access); // potential error?
	}
	
	public void setOwner(String name){
		this.owner = name;
	}
	
	public String getOwner(){
		return owner;
	}
	
	public String getId(){
		return world + x + z;
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
	
	public HashSet<String> getAccess(){
		return access;
	}
	
	public int getChunkX(){
		return x;
	}
	
	public int getChunkZ(){
		return z;
	}
	
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	public static void generateFence(ChunkRegion cr){
		int x1 = cr.getChunkX() * 16, x2 = x1 + 16, z1 = cr.getChunkZ() * 16, z2 = z1 + 16;
		World world = cr.getWorld();
		// top-left (x1,z1)
		// bottom-r (x2,z2)
		for (int x = x1; x < x2; x ++){
			world.getBlockAt(x, world.getHighestBlockYAt(x, z1), z1).setType(Material.FENCE);
			world.getBlockAt(x, world.getHighestBlockYAt(x, z2), z2).setType(Material.FENCE);
		}
		for (int z = z1 -1; z < z2 -1; z ++){
			world.getBlockAt(x1, world.getHighestBlockYAt(x1, z), z).setType(Material.FENCE);
			world.getBlockAt(x2, world.getHighestBlockYAt(x2, z), z).setType(Material.FENCE);
		}
	}
	
	public static boolean isChunkProtected(Chunk ch){
		return loadRegion(ch) != null ? true : false;
	}
	
	public static ChunkRegion getRegionAt(Location loc){
		return loadRegion(loc.getChunk());
	}
	
	public static ChunkRegion loadRegion(Chunk ch){
		return loadRegion(ch.getWorld().getName() + ch.getX() + ch.getZ());
	}
	
	public static ChunkRegion loadRegion(String id){
		if (chunkYaml == null){
			chunkYaml = YamlConfiguration.loadConfiguration(chunkFile);
		}
		if (chunkYaml.getKeys(false).contains(id)){
			String owner;
			String world;
			int x, z;
			HashSet<String> members = new HashSet<String>();
			
			owner = chunkYaml.getString(id + ".Owner");
			world = chunkYaml.getString(id + ".World");
			x = chunkYaml.getInt(id + ".Location.X");
			z = chunkYaml.getInt(id + ".Location.Z");
			
			if (chunkYaml.getConfigurationSection(id + ".Members") != null){
				/*for (String member : chunkYaml.getConfigurationSection(id + ".Members").getKeys(false)){
					members.add(member);
				}*/
				members.addAll(chunkYaml.getStringList(id + ".Members"));
			}
			
			return new ChunkRegion(x,z,world,owner, new ArrayList<String>(members));
		}
		return null;
	}
	
	public static void saveRegion(ChunkRegion cr){
		if (chunkYaml == null){
			chunkYaml = YamlConfiguration.loadConfiguration(chunkFile);
		}
		chunkYaml.set(cr.getId() + ".Owner", cr.getOwner());
		chunkYaml.set(cr.getId() + ".World", cr.getWorld().getName());
		chunkYaml.set(cr.getId() + ".Location.X", cr.getChunkX());
		chunkYaml.set(cr.getId() + ".Location.Z", cr.getChunkZ());
		chunkYaml.set(cr.getId() + ".Members", new ArrayList<String>(cr.getAccess()));
	}
}
