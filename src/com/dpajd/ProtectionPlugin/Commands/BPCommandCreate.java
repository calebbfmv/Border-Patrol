package com.dpajd.ProtectionPlugin.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.ChunkData;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandCreate extends BPCommand{

	public BPCommandCreate(Main plugin) {
		super(plugin);
	}

	private boolean isInteger(String string){
		try{
			Integer.parseInt(string);
			return true;
		}catch (NumberFormatException e){ return false; }
	}
	
	@SuppressWarnings("serial")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			String argsString = StringUtils.join(args, " ").toLowerCase();
			if (BPPerms.canCreate(player)){
				if (argsString.contains("-size")){
					ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(args));
					int sizeFlag = arguments.indexOf("-size");
					if (arguments.size() > (sizeFlag + 1)){
						if (isInteger(arguments.get(sizeFlag+1))){
							int size = Integer.parseInt(arguments.get(sizeFlag+1));
							if ((size-1)%2==0){ // if it is odd
								if (plugin.getSettings().getSizes().contains(size)){
									Chunk c = player.getLocation().getChunk();
									
									Location upper = new Location(player.getWorld(), (c.getX()*16)-(16*((size-1)/2)),0,(c.getZ()*16)+(16*((size-1)/2)));
									Location lower = new Location(player.getWorld(), (c.getX()*16)+(16*((size-1)/2)),0,(c.getZ()-(16*((size-1)/2))));
									
									boolean create = true;
									for (ChunkData cd : Region.getChunks(new Location[]{lower, upper})){
										if (cd.getRegion() != null){
											create = false;
											break;
										}
									}
									Bukkit.broadcastMessage("create = " + create);
									if (create){
										Region r = new Region(player,size,player.getLocation().getChunk(),new ArrayList<ProtectionType>(){{add(ProtectionType.BUILD);}});
										r.saveRegion();
										if (argsString.contains("-f")){
											r.generateFence();
										}
										plugin.addRegion(r);
										plugin.sendMessage(player, "Created protection id: '" + r.getName()+"'");
									}else{
										plugin.sendMessage(player, MsgType.DENIED, "You cannot create a region overlapping another!");
									}
								}else{
									plugin.sendMessage(player, MsgType.DENIED, "Region size not allowed by server!");
								}
							}else{
								plugin.sendMessage(player, MsgType.DENIED, "Region size invalid!");
							}
						}else{
							plugin.sendMessage(player, MsgType.DENIED, "Region size invalid!");
						}
					}else{
						plugin.sendMessage(player, MsgType.DENIED, "Invalid command arguments!");
					}
				}else{
					if (plugin.getRegion(player.getLocation().getChunk()) == null){
						Region r = new Region(player,1,player.getLocation().getChunk(),new ArrayList<ProtectionType>(){{add(ProtectionType.BUILD);}});
						if (argsString.contains("-f")){
							r.generateFence();
						}
						r.saveRegion();
						plugin.addRegion(r);
						plugin.sendMessage(player, "Created protection id: '" + r.getName()+"'");
					}else{
						plugin.sendMessage(player, MsgType.DENIED, "You cannot create a region overlapping another!");
					}
				}
			}else{
				plugin.sendMessage(player, MsgType.DENIED, "You cannot create this region!");
			}
		}
		return true;
	}

}
