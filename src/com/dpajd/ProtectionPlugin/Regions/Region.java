package com.dpajd.ProtectionPlugin.Regions;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.RegionMessages.RegionMessageType;

public class Region {
	private static Main plugin = (Main) Bukkit.getPluginManager().getPlugin("Border Patrol");
	private String name, world;
	private File regionFile = null;
	private FileConfiguration regionYaml = null;
	private ArrayList<ProtectionType> protections = new ArrayList<ProtectionType>();
	private ArrayList<ChunkData> chunks = new ArrayList<ChunkData>();
	private ArrayList<Member> members = new ArrayList<Member>();
	private Owner owner = null;
	private Location[] bounds = new Location[2];
	private RegionMessages messages = new RegionMessages(this);
	
	public Region(Player player, int width, Chunk chunk, ArrayList<ProtectionType> protections){
		// Constructor for new Region
		this.name = generateName();
		this.owner = new Owner(player);
		this.world = player.getWorld().getName();
		this.chunks = getChunks(width,chunk);
		this.bounds = getBounds();
		this.protections.addAll(protections);
		this.regionFile = new File(plugin.getDataFolder().getPath() + File.separator + "regions" + File.separator +  owner.getName() +".yml");
		this.regionYaml = YamlConfiguration.loadConfiguration(regionFile);
		loadMessages();
	}
	
	public Region(String name, Owner owner, Location[] bounds, ArrayList<Member> members, ArrayList<ProtectionType> protections){
		// Constructor for loading from file
		this.owner = owner;
		this.name = name;
		this.bounds = bounds;
		this.world = bounds[0].getWorld().getName();
		this.chunks = getChunks(bounds);
		this.members = members;
		this.protections = protections;
		this.regionFile = new File(plugin.getDataFolder().getPath() + File.separator + "regions" + File.separator +  owner.getName() +".yml");
		this.regionYaml = YamlConfiguration.loadConfiguration(regionFile);
		loadMessages();
	}

	private String generateName(){
		return Long.toHexString(System.currentTimeMillis());
	}
	
	public Date getDateCreated(){
		return new Date(Long.parseLong(name, 16));
	}
	
	private void loadMessages(){
		for (RegionMessageType msgType : RegionMessageType.values()){
			if (regionYaml.contains(name + msgType)){
				messages.setMessage(msgType, regionYaml.getString(name + msgType));
			}
		}
	}
	
	private void saveMessages(){
		for (RegionMessageType msgType : RegionMessageType.values()){
			if (messages.getMessage(msgType) != null){
				regionYaml.set(name + msgType, messages.getAltMessage(msgType));
			}
		}
	}
	
	public static ArrayList<ChunkData> getChunks(Location[] bounds){ // from offline
		ArrayList<ChunkData> chunks = new ArrayList<ChunkData>();
		for (int x = bounds[1].getBlockX(); x < bounds[0].getBlockX(); x+=16){
			for (int z = bounds[1].getBlockZ(); z < bounds[0].getBlockZ(); z+=16){
				chunks.add(new ChunkData(bounds[0].getWorld().getChunkAt(new Location(bounds[0].getWorld(),x,0,z))));
			}
		}
		return chunks;
	}
	
	private Location[] getBounds(){ // from memory
		ChunkData t = null;
		ChunkData b = null;
		for (ChunkData cd : chunks){
			if (t == null && b == null){
				t = cd;
				b = cd;
			}else{
				if (cd.getChunkX() < t.getChunkX() && cd.getChunkZ() < t.getChunkZ()) t = cd;
				if (cd.getChunkX() > b.getChunkX() && cd.getChunkZ() > b.getChunkZ()) t = cd;
			}
		}
		Location tLoc = new Location(t.getWorld(), t.getChunkX()*16+15,0,t.getChunkZ()*16+15);
		Location bLoc =	new Location(t.getWorld(), b.getChunkX()*16,0,b.getChunkZ()*16);
		return new Location[]{tLoc,bLoc};
	}
	
	private ArrayList<ChunkData> getChunks(int width, Chunk chunk){
		ArrayList<ChunkData> chunkList = new ArrayList<ChunkData>();
		if (width > 1){
			int size = (width - 1) / 2;
			for (int chX = 0 - size; chX <= size; chX++){
				for (int chZ = 0 - size; chZ <= size; chZ++){
					chunkList.add(new ChunkData(chunk.getWorld().getChunkAt(chX+chunk.getX(), chZ+chunk.getZ())));
				}
			}
			
			bounds[0] = new Location(chunk.getWorld(),chunk.getX()*16-(16*((width-1)/2)),0,chunk.getZ()*16+(16*((width-1)/2)));
			bounds[1] = new Location(chunk.getWorld(),chunk.getX()*16+(16*((width-1)/2))+15,0,chunk.getZ()*16-(16*((width-1)/2))+15);
			
		}else{
			chunkList.add(new ChunkData(chunk));
			
			bounds[0] = new Location(chunk.getWorld(),chunk.getX()*16,0,chunk.getZ()*16);
			bounds[1] = new Location(chunk.getWorld(),chunk.getX()*16+15,0,chunk.getZ()*16+15);
			
		}
		
		return chunkList;
	}
	
	public ArrayList<ChunkData> getChunks(){
		return chunks;
	}
	
	public boolean hasProtection(ProtectionType type){
		for (ProtectionType p : protections){
			if(p.equals(type)) return true;
		}
		return false;
	}
	
	public ArrayList<ProtectionType> getProtections(){
		return protections;
	}
	
	public void setProtections(ArrayList<ProtectionType> protections){
		this.protections = protections;
	}
	
	public void addProtection(ProtectionType p){
		if (!protections.contains(p)) protections.add(p);
	}
	
	public void removeProtection(ProtectionType p){
		if (protections.contains(p)) protections.remove(p);
	}
	
	public World getWorld(){
		return Bukkit.getWorld(world);
	}
	
	public String getName(){
		return name;
	}

	public ArrayList<Member> getMembers(){
		return members;
	}
	
	public Member getMember(String playerName){
		Member member = null;
		for (Member m : members){
			if (m.getPlayer().getName().equals(playerName)) return m;
		}
		return member;
	}
	
	public boolean hasAccess(String name){
		for (Member m : members){
			if (m.getName().equals(name)) return true;
		}
		if (owner.getName().equals(name)) return true;
		return false;
	}
	
	public boolean hasAccess(Player player){
		return hasAccess(player.getName());
	}
	
	public Owner getOwner(){
		return owner;
	}
	
	public void setOwner(Owner owner){
		this.owner = owner;
	}
	
	public Member getMember(Player player){
		for (Member m : members){
			if (m.getName().equals(player.getName())){
				return m;
			}
		}
		return null;
	}
	
	public void removeMember(Member member){
		for (Member m : members){
			if (m.getName().equals(member.getName())){
				members.remove(m);
				saveRegion();
				break;
			}
		}
	}
	
	public void addMember(Member member){
		members.add(member);
	}
	
	public boolean isInside(Block b){
		if (b.getWorld().getName().equals(world)){
			int x1 = bounds[1].getBlockX();
			int x2 = bounds[0].getBlockX();
			int z1 = bounds[1].getBlockZ();
			int z2 = bounds[0].getBlockZ();
			
			Location bLoc = b.getLocation();
			
			if (	(bLoc.getBlockX() >= x1 && bLoc.getBlockX() <= x2) &&
					(bLoc.getBlockZ() >= z1 && bLoc.getBlockZ() <= z2)){
				return true;
			}
		}
		return false;
	}
	
	public void generateFence(){
		int x1 = bounds[1].getBlockX();
		int x2 = bounds[0].getBlockX();
		int z1 = bounds[1].getBlockZ();
		int z2 = bounds[0].getBlockZ();
		World world = bounds[0].getWorld();
		for (int x = x1; x <= x2; x ++){
			world.getBlockAt(x, world.getHighestBlockYAt(x, z1), z1).setType(Material.FENCE);
			world.getBlockAt(x, world.getHighestBlockYAt(x, z2), z2).setType(Material.FENCE);
		}
		for (int z = z1 ; z <= z2 ; z ++){
			world.getBlockAt(x1, world.getHighestBlockYAt(x1, z), z).setType(Material.FENCE);
			world.getBlockAt(x2, world.getHighestBlockYAt(x2, z), z).setType(Material.FENCE);
		}
		world.getBlockAt(x1, world.getHighestBlockYAt(x1, z1+((z2-z1)/2)), z1+((z2-z1)/2)).setTypeIdAndData(Material.FENCE_GATE.getId(), (byte) 1, true);
	}
	
	public void deleteRegion(){
		regionYaml.set(name, null);
		try {
			regionYaml.save(regionFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public RegionMessages getMessages(){
		return messages;
	}
	
	
	public void reloadRegion(){
				
		String world = regionYaml.getString(name + ".World");
		
		int x1 = regionYaml.getInt(name + ".Bounds.Location1.X");
		int z1 = regionYaml.getInt(name + ".Bounds.Location1.Z");
		int x2 = regionYaml.getInt(name + ".Bounds.Location2.X");
		int z2 = regionYaml.getInt(name + ".Bounds.Location2.Z");
		Location loc1 = new Location(Bukkit.getWorld(world),x1,0,z1);
		Location loc2 = new Location(Bukkit.getWorld(world),x2,0,z2);
		Location[] bounds = new Location[]{loc1,loc2};
		
		ArrayList<Member> members = new ArrayList<Member>();
		for (String memberName : regionYaml.getStringList(name + ".Members")){
			members.add(new Member(memberName));
		}
		
		ArrayList<ProtectionType> protections = new ArrayList<ProtectionType>();
		for (String protectionName : regionYaml.getStringList(name + ".Protections")){
			protections.add(ProtectionType.getTypeFromName(protectionName));
		}
		
		this.world = world;
		this.bounds = bounds;
		this.members = members;
		this.protections = protections;
		
	}
	
	public void saveRegion(){
		
		regionYaml.set(name + ".World", world);
		
		regionYaml.set(name + ".Bounds.Location1.X", bounds[0].getBlockX());
		regionYaml.set(name + ".Bounds.Location1.Z", bounds[0].getBlockZ());
		regionYaml.set(name + ".Bounds.Location2.X", bounds[1].getBlockX());
		regionYaml.set(name + ".Bounds.Location2.Z", bounds[1].getBlockZ());
		
		ArrayList<String> memberList = new ArrayList<String>();
		for (Member member : members){
			memberList.add(member.getName());
		}
		regionYaml.set(name + ".Members", memberList.toArray(new String[memberList.size()]));
		
		ArrayList<String> protectionList = new ArrayList<String>();
		for (ProtectionType type : protections){
			protectionList.add(type.name());
		}
		regionYaml.set(name + ".Protections", protectionList.toArray(new String[protectionList.size()]));
		
		saveMessages();
		
		try {
			regionYaml.save(regionFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Region> loadRegions(){
		// File locations: plugins/Border Patrol/regions/<owner>.yml
		File dir = new File(plugin.getDataFolder().getPath() + File.separator + "regions");
		if (dir.exists()){
			FilenameFilter filter = new FilenameFilter(){
				@Override
				public boolean accept(File dir, String fileName) {
					return fileName.endsWith(".yml");
				}
			};
			ArrayList<Region> regions = new ArrayList<Region>();
			for (File file : dir.listFiles(filter)){
				FileConfiguration regionYaml = YamlConfiguration.loadConfiguration(file);
				
				Owner owner = new Owner(file.getName().substring(0, file.getName().lastIndexOf(".")));
				if (regionYaml.getKeys(false).size() > 0){
					for (String name : regionYaml.getKeys(false)){
						
						String world = regionYaml.getString(name + ".World");
						
						int x1 = regionYaml.getInt(name + ".Bounds.Location1.X");
						int z1 = regionYaml.getInt(name + ".Bounds.Location1.Z");
						int x2 = regionYaml.getInt(name + ".Bounds.Location2.X");
						int z2 = regionYaml.getInt(name + ".Bounds.Location2.Z");
						
						Location loc1 = new Location(Bukkit.getWorld(world),x1,0,z1);
						Location loc2 = new Location(Bukkit.getWorld(world),x2,0,z2);
						Location[] bounds = new Location[]{loc1,loc2};
						
						ArrayList<Member> members = new ArrayList<Member>();
						for (String memberName : regionYaml.getStringList(name + ".Members")){
							members.add(new Member(memberName));
						}
						
						ArrayList<ProtectionType> protections = new ArrayList<ProtectionType>();
						for (String protectionName : regionYaml.getStringList(name + ".Protections")){
							protections.add(ProtectionType.getTypeFromName(protectionName));
						}
						Region r = new Region(name, owner, bounds, members, protections);
						r.loadMessages();
						
						regions.add(r);
					}
				}

				
			}
			return regions;
			
		}else return new ArrayList<Region>();
	}
}
