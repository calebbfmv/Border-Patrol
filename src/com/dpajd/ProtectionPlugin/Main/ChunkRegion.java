package com.dpajd.ProtectionPlugin.Main;

import java.io.File;
import java.io.IOException;
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
	private static File chunkFile = new File("plugins" + File.separator + "Border Patrol" + File.separator + "protections.yml");
	private static FileConfiguration chunkYaml = null;
	private HashSet<ProtectionType> protections;
	
	public ChunkRegion(Location loc, String owner) {
		this.owner = owner;
		x = loc.getChunk().getX();
		z = loc.getChunk().getZ();		
		world = loc.getWorld().getName();
		loadProtections();
		loadMembers();
	}
	
	public ChunkRegion(int x, int z, String world, String owner){
		this.x = x;
		this.z = z;
		this.world = world;
		this.owner = owner;
		loadProtections();
		loadMembers();
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
		//System.out.println("owner:"+ this.owner + ", " + access.toString() + ", returns:"+ (this.owner == name) +","+ (access.contains(name)));
		return this.owner.equals(name) || access.contains(name);
	}
	
	public ArrayList<ProtectionType> getProtections(){
		return new ArrayList<ProtectionType>(protections);
	}
	
	public void loadProtections(){
		protections = new HashSet<ProtectionType>();
		if (chunkYaml == null){
			chunkYaml = YamlConfiguration.loadConfiguration(chunkFile);
		}
		if (chunkYaml.getConfigurationSection(getId()) != null){
			if (chunkYaml.getStringList(getId() + ".Protections") != null){
				for (String p : chunkYaml.getStringList(getId() + ".Protections")){
					protections.add(ProtectionType.getTypeFromName(p));
				}
			}
		}
	}
	
	public void loadMembers(){
		if (chunkYaml == null){
			chunkYaml = YamlConfiguration.loadConfiguration(chunkFile);
		}
		//System.out.println("loadMembers() -> " + getId() + ".Members");
		if (chunkYaml.getConfigurationSection(getId()) != null){
			//System.out.println(chunkYaml.getList(getId() + ".Members"));
			if (chunkYaml.getStringList(getId() + ".Members") != null){
				for (String s : chunkYaml.getStringList(getId() + ".Members")){
					access.add(s.toString());
				}
			}
		}
	}
	
	public void _p(ArrayList<ProtectionType> protections){
		if (protections == null){
			this.protections.clear();
			this.protections.add(ProtectionType.NONE);
		}else this.protections = new HashSet<ProtectionType>(protections);
	}
	
	public void setProtections(ArrayList<ProtectionType> protections){
		_p(protections);
		saveRegion(this);
	}
	
	public void addProtections(ArrayList<ProtectionType> protections){
		this.protections.addAll(protections);
		if (this.protections.contains(ProtectionType.NONE)) this.protections.remove(ProtectionType.NONE);
		saveRegion(this);
	}
	
	public void addProtection(ProtectionType p){
		protections.add(p);
		if (this.protections.contains(ProtectionType.NONE)) this.protections.remove(ProtectionType.NONE);
		saveRegion(this);
	}
	
	public void removeAllProtections(){
		protections.clear();
		protections.add(ProtectionType.NONE);
		saveRegion(this);
	}
	
	public void removeProtections(ArrayList<ProtectionType> protections){
		this.protections.removeAll(protections);
		if (!this.protections.contains(ProtectionType.NONE)) this.protections.add(ProtectionType.NONE);
		saveRegion(this);
	}
	
	public void removeProtection(ProtectionType p){
		protections.remove(p);
		if (!this.protections.contains(ProtectionType.NONE)) this.protections.add(ProtectionType.NONE);
		saveRegion(this);
	}
	
	// XXX: isInside(Block) unused...
	public boolean isInside(Block b){ 
		return Bukkit.getWorld(world).getChunkAt(x,z).equals(b.getChunk());
	}
	
	public void addAccess(String name){
		access.add(name);
		System.out.println("Adding:"+name);
		saveRegion(this);
		System.out.println("the name:" + ((access.contains(name))?"exists":"doesn't exist"));
	}
	
	public void removeAccess(String name){
		access.remove(name);
		saveRegion(this);
	}
	
	public HashSet<String> getAccess(){
		return access;
	}
	
	public void _m(HashSet<String> access){
		this.access = access;
	}
	
	public void setAccess(HashSet<String> access){
		_m(access);
		saveRegion(this);
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
			System.out.println("LOADING REGION " + id);
			String owner;
			String world;
			int x, z;
			/*HashSet<String> members = new HashSet<String>();
			HashSet<ProtectionType> protections = new HashSet<ProtectionType>();*/
			
			owner = chunkYaml.getString(id + ".Owner");
			world = chunkYaml.getString(id + ".World");
			x = chunkYaml.getInt(id + ".Location.X");
			z = chunkYaml.getInt(id + ".Location.Z");
			
			/*if (chunkYaml.getConfigurationSection(id + ".Members") != null){
				System.out.println(chunkYaml.getStringList(id + ".Members"));
				for (String member : chunkYaml.getStringList(id + ".Members")){
					members.add(member);
				}
				System.out.println(members);
			}
			if (chunkYaml.getConfigurationSection(id + ".Protections") != null){
				System.out.println(chunkYaml.getStringList(id + ".Protections"));
				for (String p : chunkYaml.getStringList(id + ".Protections")){
					protections.add(ProtectionType.getTypeFromName(p));
				}
				System.out.println(protections);
			}*/
			
			ChunkRegion cr = new ChunkRegion(x,z,world,owner);
			cr.loadMembers();
			cr.loadProtections();
			/*cr._p(new ArrayList<ProtectionType>(protections));
			cr._m(members);*/
			System.out.println("--END LOADING");
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
		if (protections.size() == 0){
			protections.add(ProtectionType.NONE.toString());
		}
		chunkYaml.set(cr.getId() + ".Protections", protections.toArray(new String[protections.size()]));
		chunkYaml.set(cr.getId() + ".Members", cr.getAccess().toArray(new String[cr.getAccess().size()]));
		System.out.println(" ");
		System.out.println("protections: "+ protections);
		System.out.println("members: " + cr.getAccess());
		try {
			chunkYaml.save(chunkFile);
			System.out.println("-- SAVE");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteRegion(String id){
		// TODO: Delete a region
		chunkYaml.set(id, null);
		try {
			chunkYaml.save(chunkFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
