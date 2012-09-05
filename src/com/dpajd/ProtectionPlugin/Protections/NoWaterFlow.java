package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoWaterFlow extends Protection{

	public NoWaterFlow(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.NO_WATER_FLOW;
	}

    private boolean isWater(Block block){
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }
    
    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			Region r = plugin.getRegion(e.getBlock().getChunk());
			if (r != null && isWater(e.getBlock())){
				if (r.hasProtection(this.getType())) e.setCancelled(true);
			}
		}
    }
}
