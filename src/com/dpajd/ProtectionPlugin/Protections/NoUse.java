package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.block.Jukebox;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Attachable;
import org.bukkit.material.Bed;
import org.bukkit.material.Diode;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;

public class NoUse extends Protection{

	public NoUse(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.USE;
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (!plugin.isBypass(e.getPlayer())){
			if (plugin.getSettings().hasProtection(this.getType())){
				if (e.getClickedBlock() != null){
					if (plugin.isProtected(e.getClickedBlock().getChunk(), this.getType())){
						MaterialData md = e.getClickedBlock().getState().getData();
						if (md instanceof Attachable || md instanceof Openable || md instanceof Bed || md instanceof Minecart || md instanceof Diode){
							if (!plugin.getRegion(e.getClickedBlock().getChunk()).hasAccess(e.getPlayer())){
								e.setCancelled(true);
								plugin.sendMessage(e.getPlayer(), MsgType.DENIED, "You are not allowed to do that!");
							}
						}else if (e.getClickedBlock() instanceof Jukebox){
							e.setCancelled(true);
							plugin.sendMessage(e.getPlayer(), MsgType.DENIED, "You are not allowed to do that!");
						}
					}
				}
			}
		}
	}

	// XXX: Hook redstone change event for external sources and maybe tripwire and pressure plates?
}
