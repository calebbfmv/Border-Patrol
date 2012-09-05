package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;

import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoLavaFlow extends Protection{

	public NoLavaFlow(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.NO_LAVA_FLOW;
	}
	
    private boolean isLava(Block block){
        return block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA;
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent e){
		Region r = plugin.getRegion(e.getBlock().getChunk());
		if (r != null && isLava(e.getBlock())){
			if (r.hasProtection(this.getType())) e.setCancelled(true);
		}
    }
}
