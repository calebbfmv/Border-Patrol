package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;
import com.dpajd.ProtectionPlugin.Regions.RegionMessages.RegionMessageType;

public class EntryMessage extends Protection implements Message{

	public EntryMessage(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.WELCOME;
	}

	@Override
	public RegionMessageType getMessageType() {
		return RegionMessageType.WELCOME;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (plugin.isProtected(e.getTo().getChunk())){
				Region rTo = plugin.getRegion(e.getTo().getChunk());
				Region rFrom = plugin.getRegion(e.getFrom().getChunk());
				if (!(rTo == rFrom)){
					if (rTo != null){
						if (rTo.hasProtection(this.getType())){
							plugin.sendMessage(e.getPlayer(), rTo.getMessages().getMessage(this.getMessageType()));
						}
					}
				}
			}
		}
	}
}
