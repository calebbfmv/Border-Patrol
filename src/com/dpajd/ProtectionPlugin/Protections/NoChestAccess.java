package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import com.dpajd.ProtectionPlugin.Main.Main;
import com.dpajd.ProtectionPlugin.Main.Main.MsgType;
import com.dpajd.ProtectionPlugin.Regions.Region;

public class NoChestAccess extends Protection{

	public NoChestAccess(Main plugin) {
		super(plugin);
	}

	@Override
	public ProtectionType getType() {
		return ProtectionType.CHEST_ACCESS;
	}

	@EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e){
		if (!plugin.isBypass(e.getPlayer().getName())){
			if (plugin.getSettings().hasProtection(this.getType())){
		        if (e.getInventory().getHolder() instanceof Chest){
		            Chest c = (Chest) e.getInventory().getHolder();
		            if (plugin.isProtected(c.getChunk(), this.getType())){
		            	if (!plugin.getRegion(c.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that chest!");
		            	}
		            }
		        }else if (e.getInventory().getHolder() instanceof DoubleChest){
		        	DoubleChest dc = (DoubleChest) e.getInventory().getHolder();
		        	Chest left = (Chest) dc.getLeftSide();
		        	Chest right = (Chest) dc.getRightSide();
		        	if (plugin.isProtected(left.getChunk(), this.getType()) || plugin.isProtected(right.getChunk(), this.getType())){
		        		Region rl = plugin.getRegion(left.getChunk());
		        		Region rr = plugin.getRegion(right.getChunk());
		        		
		        		// At least ONE of the possible two chunks is protected
		        		if (rl == rr){
		        			if (rl != null){		        				
			        			if (rl.hasProtection(this.getType()) && !rl.hasAccess(e.getPlayer().getName())){
			        				e.setCancelled(true);
	    		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that chest!");
			        			}			        			
		        			}		        			
		        		}else{
		        			if ((rl != null) ^ (rr != null)){
		        				// Only ONE is a region
		        				if (rl != null){
		        					if (rl.hasProtection(this.getType())){
		        						if (!rl.hasAccess(e.getPlayer().getName())){
		        							e.setCancelled(true);
					        				e.getPlayer().openInventory(right.getBlockInventory());
		        						}else{
		        							e.setCancelled(true);
					        				e.getPlayer().openInventory(left.getBlockInventory());
		        						}
		        					}
		        				}else{
		        					if (rr.hasProtection(this.getType())){
		        						if (!rr.hasAccess(e.getPlayer().getName())){
		        							e.setCancelled(true);
					        				e.getPlayer().openInventory(left.getBlockInventory());
		        						}else{
		        							e.setCancelled(true);
					        				e.getPlayer().openInventory(right.getBlockInventory());
		        						}
		        					}		        					
		        				}		        				
		        			}else if (rl != null && rr != null){
			        			boolean canOpenLeft = false;
		        				boolean canOpenRight = false;
		        				
		        				// BOTH are a region		        				
		        				if (rl.hasProtection(this.getType())){
		        					if (rl.hasAccess(e.getPlayer().getName())){
		        						canOpenLeft = true;		        					
		        					}
		        				}else{
		        					canOpenLeft = true;
		        				}
		        				if (rr.hasProtection(this.getType())){		
		        					if (rr.hasAccess(e.getPlayer().getName())){
		        						canOpenRight = true;		        				
		        					}
		        				}else{
		        					canOpenRight = true;
		        				}
		        				
		        				if (!(canOpenLeft == true && canOpenRight == true)){
		        					if (canOpenLeft){
	        							e.setCancelled(true);
				        				e.getPlayer().openInventory(left.getBlockInventory());		        						
		        					}else if (canOpenRight){
	        							e.setCancelled(true);
				        				e.getPlayer().openInventory(right.getBlockInventory());		        						
		        					}else{
	        							e.setCancelled(true);
		    		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that chest!");		        						
		        					}		        					
		        				}		        				
		        			}
		        		}
		        		
		        	}
		        }else if (e.getInventory().getHolder() instanceof Furnace){
		        	Furnace f = (Furnace) e.getInventory().getHolder();
		        	if (plugin.isProtected(f.getChunk(), this.getType())){
		            	if (!plugin.getRegion(f.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that furnace!");
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof StorageMinecart){
		        	StorageMinecart s = (StorageMinecart) e.getInventory().getHolder();
		        	if (plugin.isProtected(s.getLocation().getChunk(), this.getType())){
		            	if (!plugin.getRegion(s.getLocation().getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that minecart!");
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof Dispenser){
		        	Dispenser d = (Dispenser) e.getInventory().getHolder();
		        	if (plugin.isProtected(d.getChunk(), this.getType())){
		            	if (!plugin.getRegion(d.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that dispenser!");
		            	}
		        	}
		        	
		        }else if (e.getInventory().getHolder() instanceof BrewingStand){
		        	BrewingStand b = (BrewingStand) e.getInventory().getHolder();
		        	if (plugin.isProtected(b.getChunk(), this.getType())){
		            	if (!plugin.getRegion(b.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(e.getPlayer().getName(), MsgType.DENIED, "You do not have access to that brewing stand!");
		            	}
		        	}
		        }
			}
		}

    }
}
