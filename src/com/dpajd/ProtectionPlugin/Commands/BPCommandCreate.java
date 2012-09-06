package com.dpajd.ProtectionPlugin.Commands;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Protections.Protection.ProtectionType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class BPCommandCreate extends BPCommand{

	public BPCommandCreate(Main plugin) {
		super(plugin);
	}

	@SuppressWarnings("serial")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			String argsString = StringUtils.join(args, " ").toLowerCase();
			if (BPPerms.canCreate(player)){
				if (argsString.contains("-size")){
					
				}
				Region r = new Region(player,1,player.getLocation().getChunk(),new ArrayList<ProtectionType>(){{add(ProtectionType.BUILD);}});
				if (argsString.contains("-f")){
					r.generateFence();
				}
				r.saveRegion();
				plugin.addRegion(r);
				plugin.sendMessage(player, "Created protection id: '" + r.getName()+"'");
			}
		}
		return true;
	}

}
