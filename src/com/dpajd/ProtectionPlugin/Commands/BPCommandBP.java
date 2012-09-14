package com.dpajd.ProtectionPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.dpajd.ProtectionPlugin.Main.BPPerms;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;

public class BPCommandBP extends BPCommand{

	public BPCommandBP(Main plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (sender instanceof Player) ? (Player)sender: null;
		if (player != null){
			if (args.length == 0){
				plugin.sendMessage(player, plugin.getDescription().getVersion());
			}else{
				if (args[0].equalsIgnoreCase("reload")){
					if (BPPerms.isAdmin(sender)){
						plugin.getSettings().loadConfig(false);
						plugin.reloadRegions();
						plugin.sendMessage(player, "Reloaded.");
					}
				}
			}
		}else{
			if (args.length == 0){
				plugin.log.info(MsgType.LOG + plugin.getDescription().getVersion());
			}else{
				if (args[0].equalsIgnoreCase("reload")){
					plugin.getSettings().loadConfig(false);
					plugin.reloadRegions();
					plugin.log.info(MsgType.LOG + "Reloaded.");
				}
			}
		}
		return true;
	}

	@Override
	public CommandType getType() {
		return null;
	}

}
