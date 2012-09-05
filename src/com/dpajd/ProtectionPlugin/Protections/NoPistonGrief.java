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
		return ProtectionType.NO_PISTON_GRIEF;
	}

	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e){
		HashSet<Chunk> chunks = new HashSet<Chunk>();
		for (Block b : e.getBlocks()){
			chunks.add(b.getChunk());
		}
		if (chunks.size() > 1){
			ArrayList<Chunk> chunkList = new ArrayList<Chunk>(chunks);
			Region from = plugin.getRegion(chunkList.get(0));
			Region to = plugin.getRegion(chunkList.get(1));
			if (from.hasProtection(this.getType())){
				if (from != null && to != null){
					if (!to.getOwner().equals(from.getOwner())){
						e.setCancelled(true);
					}
				}else if(from != null && to == null){
					if (!from.isInside(e.getBlock())){
						e.setCancelled(true);
					}
				}
			}

		}
	}
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent e){
		if (e.isSticky()){
			Region to = plugin.getRegion(e.getRetractLocation().getChunk());
			Region from = plugin.getRegion(e.getBlock().getChunk());
			if (to.hasProtection(this.getType())){
				if (to != null && from != null){
					if (!from.getOwner().equals(to.getOwner())){
						e.setCancelled(true);
					}
				}else if(to != null && from == null){
					e.setCancelled(true);
				}
			}
		}
	}
}
