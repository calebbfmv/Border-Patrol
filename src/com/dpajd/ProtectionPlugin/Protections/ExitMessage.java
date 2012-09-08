package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;
import com.dpajd.ProtectionPlugin.Regions.RegionMessages.RegionMessageType;

public class ExitMessage extends Protection implements Message{

	public ExitMessage(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.FAREWELL;
	}

	@Override
	public RegionMessageType getMessageType() {
		return RegionMessageType.FAREWELL;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (plugin.isProtected(e.getTo().getChunk())){
				Region rTo = plugin.getRegion(e.getTo().getChunk());
				Region rFrom = plugin.getRegion(e.getFrom().getChunk());
				if (!rTo.equals(rFrom)){
					if (rFrom.hasProtection(this.getType())){
						plugin.sendMessage(e.getPlayer(), rFrom.getMessages().getMessage(this.getMessageType()));
					}
				}
			}
		}
	}
}
