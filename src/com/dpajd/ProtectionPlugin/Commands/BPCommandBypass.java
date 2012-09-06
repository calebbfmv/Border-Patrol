package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;

public class BPCommandBypass extends BPCommand{

	public BPCommandBypass(Main plugin) {
		super(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (BPPerms.canBypass(player)){
				plugin.toggle(plugin.bypassEnabled,player.getName());
				plugin.sendMessage(player, "Toggled admin bypass: " + ((plugin.bypassEnabled.contains(player.getName()))?"On":"Off"));
			}else{
				plugin.sendMessage(player, MsgType.DENIED, "You don't have permission to do that!");
			}
		}
		return true;
	}

}
