package com.dpajd.ProtectionPlugin.Main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BPListener implements Listener{
	private BPConfig settings;
	private Main plugin;
	
	public BPListener(Main plugin){
		this.plugin = plugin;
		this.settings = plugin.settings;
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		
	}
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e){
		
	}
	
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e){
		
	}
	
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent e){
		if (e.isSticky()){
			//
		}
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		
	}
	
	// XXX: RedstoneChangeEvent??
}
