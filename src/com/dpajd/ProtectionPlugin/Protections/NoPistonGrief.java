package com.dpajd.ProtectionPlugin.Protections;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoPistonGrief extends Protection{

	public NoPistonGrief(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.PISTON;
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e){
		if (plugin.getSettings().hasProtection(this.getType())){
			HashSet<Chunk> chunks = new HashSet<Chunk>();
			for (Block b : e.getBlocks()){
				chunks.add(b.getChunk());
			}
			Region from;
			Region to;
			ArrayList<Chunk> chunkList = new ArrayList<Chunk>(chunks);
			if (chunks.size() >=1){
				switch (chunks.size()){
					case 1:
						from = plugin.getRegion(e.getBlock());
						to = plugin.getRegion(chunkList.get(0));
						break;
					case 2:
						from = plugin.getRegion(chunkList.get(0));
						to = plugin.getRegion(chunkList.get(1));
						break;
					default: return;
				}
				if (from != null && to != null){ // Both are regions
					if (!to.getOwner().getName().equals(from.getOwner().getName())){
						if (to.hasProtection(this.getType())) e.setCancelled(true);
					}
				}else if(to != null && from == null){ // To is a region, from is empty
					if (!to.isInside(e.getBlock())){
						if (to.hasProtection(this.getType())) e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent e){
		if (e.isSticky()){
			if (plugin.getSettings().hasProtection(this.getType())){
				Region to = plugin.getRegion(e.getRetractLocation().getChunk());
				Region from = plugin.getRegion(e.getBlock().getChunk());
				if (to != null && from != null){
					if (!from.getOwner().equals(to.getOwner())){
						if (to.hasProtection(this.getType())) e.setCancelled(true);
					}
				}else if(to != null && from == null){
					if (to.hasProtection(this.getType())) e.setCancelled(true);
				}
			}
		}
	}
}
