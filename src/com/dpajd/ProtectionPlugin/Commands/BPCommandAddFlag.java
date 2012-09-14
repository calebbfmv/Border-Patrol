package com.dpajd.ProtectionPlugin.Commands;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.Region;
import com.dpajd.ProtectionPlugin.Regions.RegionMessages.RegionMessageType;

public class BPCommandAddFlag extends BPCommand{

	public BPCommandAddFlag(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length > 0){
				Region r = plugin.getRegion(player.getLocation().getChunk());
				if (r != null){
					if (r.getOwner().getName().equals(player.getName()) || BPPerms.isAdmin(player)){
						ProtectionType type = ProtectionType.getTypeFromName(args[0]);
						if (type != null){
							if (BPPerms.hasFlag(player, type)){
								if (type.isMessageFlag()){
									if (args.length > 1){
										String message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
										RegionMessageType msgType = RegionMessageType.getType(type);
										if (econEnabled){
											if (econ.has(player.getName(), cost()+cost(type))){
												r.addProtection(type);
												r.getMessages().setMessage(msgType, message);
												r.saveRegion();
												plugin.sendMessage(player, "Added " + type.name() + " protection to the region with the message '"+ChatColor.GRAY+message+ChatColor.GOLD+"'.");
											}else{
												plugin.sendMessage(player, MsgType.DENIED, new String[]{"Not enough funds!","This command costs: "+econ.format(cost()),"This protection costs: "+econ.format(cost(type))});
											}
										}else{
											r.addProtection(type);
											r.getMessages().setMessage(msgType, message);
											r.saveRegion();
											plugin.sendMessage(player, "Added " + type.name() + " protection to the region with the message '"+ChatColor.GRAY+message+ChatColor.GOLD+"'.");
										}
									}else{
										plugin.sendMessage(player, MsgType.DENIED, "You must provide a message!");
									}
								}else{
									if (!r.hasProtection(type)){
										if (econEnabled){
											if (econ.has(player.getName(), cost()+cost(type))){
												r.addProtection(type);
												r.saveRegion();
												plugin.sendMessage(player, "Added " + type.name() + " protection to the region.");
											}else{
												plugin.sendMessage(player, MsgType.DENIED, new String[]{"Not enough funds!","This command costs: "+econ.format(cost()),"This protection costs: "+econ.format(cost(type))});
											}
										}else{
											r.addProtection(type);
											r.saveRegion();
											plugin.sendMessage(player, "Added " + type.name() + " protection to the region.");
										}
									}else{
										plugin.sendMessage(player, MsgType.DENIED, "Protection already exists!");
									}
								}
							}else{
								plugin.sendMessage(player, MsgType.DENIED, "You do not have permission to add that protection type!");
							}
						}else{
							plugin.sendMessage(player, MsgType.DENIED, "Not a valid protection type!");
						}	
					}
				}else{
					plugin.sendMessage(player, MsgType.DENIED, "Cannot add a protection to this chunk!");
				}
			}else{
				plugin.sendMessage(player, MsgType.DENIED, "You must provide a flag name!");
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return CommandType.ADDFLAG;
	}

}
