package com.dpajd.ProtectionPlugin.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.dpajd.ProtectionPlugin.Main.ChunkRegion.ProtectionType;

@SuppressWarnings("unused")
@Deprecated
public class BPListener implements Listener{
	private BPConfig settings;
	private Main plugin;
	private HashMap<String,Location> toolUse = new HashMap<String,Location>();
	
	public BPListener(Main plugin){
		this.plugin = plugin;
		this.settings = plugin.settings;
	}
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){
		ChunkRegion cr;
		if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
			if (e.getEntity() instanceof Enderman){
				if (cr.hasProtection(ProtectionType.NO_ENDERMAN_GRIEF)){
					e.setCancelled(true);
				}
			}else if (e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				if (!cr.hasAccess(p.getName())){
					e.setCancelled(true);
					e.getBlock().getState().update(true);
				}
			}
		}
	}
    private boolean isLava(Block block){
        return block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA;
    }

    private boolean isWater(Block block){
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER;
    }
    
    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent e){
    	if (isLava(e.getBlock()) || isWater(e.getBlock())){
    		ChunkRegion cr;
    		if ((cr = ChunkRegion.getRegionAt(e.getToBlock().getLocation())) != null){
    			if (cr.hasProtection(ProtectionType.NO_WATER_FLOW)){
    				if (isWater(e.getBlock())){
    					e.setCancelled(true);
    				}
    			}else if (cr.getProtections().contains(ProtectionType.NO_LAVA_FLOW)){
    				if (isLava(e.getBlock())){
    					e.setCancelled(true);
    				}
    			}
    		}
    	}
    }
    
	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent e){
		ChunkRegion cr;
		if (e.getBlock().getType() == Material.WATER){
			
			if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
				if (cr.hasProtection(ProtectionType.NO_WATER_FLOW)){
					e.setCancelled(true);
				}
			}
		}else if (e.getBlock().getType() == Material.LAVA){
			if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
				if (cr.hasProtection(ProtectionType.NO_LAVA_FLOW)){
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent e){
		ChunkRegion cr;
		if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
			if (cr.hasProtection(ProtectionType.NO_FIRE)){
				e.setCancelled(true);
			}
		}
	}
		
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		ChunkRegion cr;
		if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
			if (!cr.hasAccess(e.getPlayer().getName())){
				e.setCancelled(true);
				e.getBlock().getState().update(true);
			}
		}else {
			if (plugin.toolEnabled.contains(e.getPlayer().getName()) && e.getPlayer().getItemInHand().getType().equals(settings.getTool())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e){
		ChunkRegion cr;
		if ((cr = ChunkRegion.getRegionAt(e.getBlock().getLocation())) != null){
			if (!cr.hasAccess(e.getPlayer().getName())){
				e.setCancelled(true);
			}
		}else {
			if (plugin.toolEnabled.contains(e.getPlayer().getName()) && e.getPlayer().getItemInHand().getType().equals(settings.getTool())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if (plugin.toolEnabled.contains(e.getPlayer().getName())){
			if (plugin.toolEnabled.contains(e.getPlayer().getName())){
				ChunkRegion cr;
				if (e.getClickedBlock() != null){
					Location click = e.getClickedBlock().getLocation(); 
					String playerName = e.getPlayer().getName();
					if ((cr = ChunkRegion.getRegionAt(e.getPlayer().getLocation())) == null){
						if (BPPerms.canCreate(e.getPlayer())){
							if (e.getAction() == Action.LEFT_CLICK_BLOCK){
								if (e.getItem().getType() == settings.getTool()){
									toolUse.put(playerName, click);
									e.getPlayer().sendMessage(Main.default_prefix + "Creating region protection. Right click the same block to confirm.");
								}
							}else if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
								if (e.getItem() != null){
									if (e.getItem().getType() == settings.getTool()){
										if (toolUse.containsKey(playerName)){
											if (toolUse.get(playerName).getBlock().equals(e.getClickedBlock())){
												ChunkRegion newChunk = new ChunkRegion(click,playerName);
												ChunkRegion.saveRegion(newChunk);
												e.getPlayer().sendMessage(Main.default_prefix + "You created a chunk protection '" + newChunk.getId()+"'");
												toolUse.remove(playerName);
											}
										}else{
											e.getPlayer().sendMessage(Main.default_prefix + "You must left click the block before you can confirm.");
										}
									}
								}
							}
						}
					}else{
						e.getPlayer().sendMessage(Main.default_prefix + "You cannot claim this chunk!");
						toolUse.remove(playerName);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent e){
		for (Block b : e.blockList()){
			if (ChunkRegion.isChunkProtected(b.getChunk())){
				if (ChunkRegion.getRegionAt(b.getLocation()).hasProtection(ProtectionType.NO_EXPLOSION)){
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
			if (from.hasProtection(ProtectionType.NO_PISTON_GRIEF)){ // NPE
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
			ChunkRegion to = ChunkRegion.getRegionAt(e.getRetractLocation());
			ChunkRegion from = ChunkRegion.getRegionAt(e.getBlock().getLocation());
			if (to.hasProtection(ProtectionType.NO_PISTON_GRIEF)){
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
				if (to.hasProtection(ProtectionType.NO_ENTRY)){
					if (to != from){
						if (!to.hasAccess(e.getPlayer().getName())){
							e.getPlayer().sendMessage(Main.default_prefix + "You are not permitted to enter this area!");
							e.getPlayer().teleport(e.getFrom());
						}
					}
				}
				if (to.hasProtection(ProtectionType.ELECTRIC_FENCE)){
					if (to != from){
						if (!to.hasAccess(e.getPlayer().getName())){
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 15, 5));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 15, 1));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 30, 1));
							e.getPlayer().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
							e.getPlayer().setHealth((int) Math.floor(e.getPlayer().getHealth()/2));
							e.getPlayer().teleport(e.getFrom());
							Vector v = e.getPlayer().getLocation().getDirection();
							v.multiply(-1).add(new Vector(0,0.2,0));
							e.getPlayer().setVelocity(v);
						}
					}
				}
			}
		}

	}
	
	// XXX: RedstoneChangeEvent??
}
