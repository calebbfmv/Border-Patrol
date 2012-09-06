package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import com.dpajd.ProtectionPlugin.Main.Main;

public class NoExplosion extends Protection{

	public NoExplosion(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.ENTRY;
	}

	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			for (Block b : e.blockList()){
				if (plugin.isProtected(b.getChunk())){
					if (plugin.getRegion(b.getChunk()).hasProtection(this.getType())){
						e.blockList().clear();
						break;
					}
				}
			}
		}
	}
}
