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
	public static enum ProtectionType{
		ELECTRIC_FENCE, NO_ENTRY, NO_EXPLOSION, NO_FIRE, NO_ENDERMAN_GRIEF,
		NO_WATER_FLOW, NO_LAVA_FLOW, NO_PISTON_GRIEF, NONE;
		public static ProtectionType getTypeFromName(String name){
			for (ProtectionType p : ProtectionType.values()){
				if (p.name().equalsIgnoreCase(name)) return p;
			}
			return null;
		}
	}
	
	private int x, z;
	private HashSet<String> access = new HashSet<String>();
	private String owner, world;
	private static File chunkFile = new File("plugins" + File.separator + "BorderPatrol" + File.separator + "protections.yml");
	private static FileConfiguration chunkYaml = null;
	@SuppressWarnings("serial")
	private HashSet<ProtectionType> protections = new HashSet<ProtectionType>(){{add(ProtectionType.NONE);}};
	
	public ChunkRegion(Location loc, String owner) {
		this.owner = owner;
		access.add(owner);
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();		
		world = loc.getWorld().getName();
	}
	
	public ChunkRegion(int x, int z, String world, String owner){
		this.x = x;
		this.z = z;
		this.world = world;
		this.owner = owner;
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
		return this.owner == name || access.contains(name);
	}
	
	public ArrayList<ProtectionType> getProtections(){
		return new ArrayList<ProtectionType>(protections);
	}
	
	public void setProtections(ArrayList<ProtectionType> protections){
		if (protections == null){
			this.protections.clear();
			this.protections.add(ProtectionType.NONE);
		}else this.protections = new HashSet<ProtectionType>(protections);
	}
	
	public void addProtections(ArrayList<ProtectionType> protections){
		this.protections.addAll(protections);
		if (this.protections.contains(ProtectionType.NONE)) this.protections.remove(ProtectionType.NONE);
	}
	
	public void addProtection(ProtectionType p){
		protections.add(p);
		if (this.protections.contains(ProtectionType.NONE)) this.protections.remove(ProtectionType.NONE);
	}
	
	public void removeAllProtections(){
		protections.clear();
		protections.add(ProtectionType.NONE);
	}
	
	public void removeProtections(ArrayList<ProtectionType> protections){
		this.protections.removeAll(protections);
		if (!this.protections.contains(ProtectionType.NONE)) this.protections.add(ProtectionType.NONE);
	}
	
	public void removeProtection(ProtectionType p){
		protections.remove(p);
		if (!this.protections.contains(ProtectionType.NONE)) this.protections.add(ProtectionType.NONE);
	}
	
	// XXX: isInside(Block) unused...
	public boolean isInside(Block b){ 
		return Bukkit.getWorld(world).getChunkAt(x,z).equals(b.getChunk());
	}
	
	public void addAccess(String name){
		access.add(name);
	}
	
	public void removeAccess(String name){
		if (name != this.owner) access.remove(name);	
	}
	
	public HashSet<String> getAccess(){
		return access;
	}
	
	public void setAccess(HashSet<String> access){
		this.access = access;
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
			world.getBlockAt(x, world.getHighestBlockYAt(x, z1)+1, z1).setType(Material.FENCE);
			world.getBlockAt(x, world.getHighestBlockYAt(x, z2)+1, z2).setType(Material.FENCE);
		}
		for (int z = z1 -1; z < z2 +1; z ++){ // Verify in-game
			world.getBlockAt(x1, world.getHighestBlockYAt(x1, z)+1, z).setType(Material.FENCE);
			world.getBlockAt(x2, world.getHighestBlockYAt(x2, z)+1, z).setType(Material.FENCE);
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
			HashSet<ProtectionType> protections = new HashSet<ProtectionType>();
			
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
			if (chunkYaml.getConfigurationSection(id + ".Protections") != null){
				for (String p : chunkYaml.getConfigurationSection(id + ".Protections").getKeys(false)){
					protections.add(ProtectionType.getTypeFromName(p));
				}
			}
			
			ChunkRegion cr = new ChunkRegion(x,z,world,owner);
			cr.setProtections(new ArrayList<ProtectionType>(protections));
			cr.setAccess(members);
			return cr;
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
		
		ArrayList<String> protections = new ArrayList<String>();
		for (ProtectionType p : cr.getProtections())
			protections.add(p.name());
		chunkYaml.set(cr.getId() + ".Protections", protections);
	}
	
	public static void deleteRegion(String id){
		// TODO: Delete a region
		chunkYaml.set(id, null);
	}
}
