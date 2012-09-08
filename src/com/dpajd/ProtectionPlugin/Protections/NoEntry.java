package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import com.dpajd.ProtectionPlugin.CustomEvents.BPHostileEntityMoveEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoEntry extends Protection{

	public NoEntry(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.ENTRY;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				if (plugin.isProtected(e.getTo().getChunk())){
					Region rTo = plugin.getRegion(e.getTo().getChunk());
					Region rFrom = plugin.getRegion(e.getFrom().getChunk());
					if (!rTo.equals(rFrom)){
						if (rTo.hasProtection(this.getType())){
							String pName = e.getPlayer().getName();
							if (!rTo.hasAccess(pName)){
								plugin.sendMessage(e.getPlayer(), MsgType.DENIED, "You aren't permitted entry! Ask " + rTo.getOwner().getName() + " to permit you.");
								e.getPlayer().teleport(e.getFrom());
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBPHostileEntityMoveEvent(BPHostileEntityMoveEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			if (plugin.isProtected(e.getTo().getChunk())){
				Region rTo = plugin.getRegion(e.getTo().getChunk());
				Region rFrom = plugin.getRegion(e.getFrom().getChunk());
				if (!rTo.equals(rFrom)){
					if (rTo.hasProtection(this.getType())){
						e.getCreature().teleport(e.getFrom());
					}
				}
			}
		}
	}
}
