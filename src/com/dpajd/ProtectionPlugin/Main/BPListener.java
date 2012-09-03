package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.dpajd.ProtectionPlugin.Main.ChunkRegion.ProtectionType;

public class BPListener implements Listener{
	private BPConfig settings;
	private Main plugin;
	private HashMap<String,Location> toolUse = new HashMap<String,Location>();
	
	public BPListener(Main plugin){
		this.plugin = plugin;
		this.settings = plugin.settings;
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		ChunkRegion cr;
		if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
			if (!cr.hasAccess(e.getPlayer().getName())){
				e.setCancelled(true);
				e.getBlock().getState().update(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (plugin.toolEnabled.contains(e.getPlayer().getName())){
			@SuppressWarnings("unused") // Only used if chunkregion is null
			ChunkRegion cr;
			Location click = e.getClickedBlock().getLocation();
			String playerName = e.getPlayer().getName();
			if ((cr = ChunkRegion.getRegionAt(e.getPlayer().getLocation())).equals(null) && BPPerms.canCreate(e.getPlayer())){
				if (e.getAction() == Action.LEFT_CLICK_BLOCK){
					if (e.getItem().getType() == settings.getTool()){
						toolUse.put(playerName, click);
					}
				}else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
					if (e.getItem().getType() == settings.getTool()){
						if (toolUse.containsKey(playerName)){
							ChunkRegion.saveRegion(new ChunkRegion(click,playerName));
							toolUse.remove(playerName);
						}else{
							e.getPlayer().sendMessage(Main.default_prefix + "You must left click the block before you can confirm.");
						}
					}
				}
				
			}
		}

	}
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e){
		for (Block b : e.blockList()){
			if (ChunkRegion.isChunkProtected(b.getChunk())){
				if (ChunkRegion.getRegionAt(b.getLocation()).getProtections().contains(ProtectionType.NO_EXPLOSION)){
					e.blockList().clear(); // Remove blocks from damage, allow entity damage.
					//e.setCancelled(true);
					break;
				}
				
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e){
		HashSet<String> chunks = new HashSet<String>();
		for (Block b : e.getBlocks()){
			chunks.add(b.getWorld().getName() + b.getChunk().getX() + b.getChunk().getZ()); // ChunkRegion ID composite
		}
		if (chunks.size() > 1){
			ArrayList<String> chunkList = new ArrayList<String>(chunks);
			ChunkRegion from = ChunkRegion.loadRegion(chunkList.get(0));
			ChunkRegion to = ChunkRegion.loadRegion(chunkList.get(1));
			if (to.getProtections().contains(ProtectionType.NO_PISTON_GRIEF)){
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
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent e){
		if (e.isSticky()){
			ChunkRegion to = ChunkRegion.getRegionAt(e.getRetractLocation());
			ChunkRegion from = ChunkRegion.getRegionAt(e.getBlock().getLocation());
			if (to.getProtections().contains(ProtectionType.NO_PISTON_GRIEF)){
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
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		// Consider optimizing this
		if (!e.getFrom().getChunk().equals(e.getTo().getChunk())){
			if (ChunkRegion.isChunkProtected(e.getTo().getChunk())){
				ChunkRegion from = ChunkRegion.getRegionAt(e.getFrom());
				ChunkRegion to = ChunkRegion.getRegionAt(e.getTo());
				if (to.getProtections().contains(ProtectionType.NO_ENTRY)){
					if (to != from){
						if (!to.getAccess().contains(e.getPlayer().getName())){
							e.getPlayer().sendMessage(Main.default_prefix + "You are not permitted to enter this area!");
							e.getPlayer().teleport(e.getFrom());
						}
					}
				}
			}
		}

	}
	
	// XXX: RedstoneChangeEvent??
}
