package com.dpajd.ProtectionPlugin.Protections;

import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
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
		Player p = (Player) e.getPlayer();
		if (!plugin.isBypass(p.getName())){
			if (plugin.getSettings().hasProtection(this.getType())){
		        if (e.getInventory().getHolder() instanceof Chest){
		            Chest c = (Chest) e.getInventory().getHolder();
		            if (plugin.isProtected(c.getChunk(), this.getType())){
		            	if (!plugin.getRegion(c.getChunk()).hasAccess(p.getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that chest!");
		            	}
		            }
		        }else if (e.getInventory().getHolder() instanceof DoubleChest){
		        	DoubleChest dc = (DoubleChest) e.getInventory().getHolder();
		        	Chest left = (Chest) dc.getLeftSide();
		        	Chest right = (Chest) dc.getRightSide();
		        	if (plugin.isProtected(left.getChunk(), this.getType()) || plugin.isProtected(right.getChunk(), this.getType())){
		        		Region lr = plugin.getRegion(left.getChunk());
		        		Region rr = plugin.getRegion(right.getChunk());
		        		
		        		// At least ONE of the possible two chunks is protected
		        		if (lr == rr){
		        			if (lr != null){		        				
			        			if (lr.hasProtection(this.getType()) && !lr.hasAccess(p.getName())){
			        				e.setCancelled(true);
	    		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that chest!");
			        			}			        			
		        			}		        			
		        		}else{
		        			if ((lr != null) ^ (rr != null)){
		        				// Only ONE is a region
		        				if (lr != null){
		        					if (lr.hasProtection(this.getType())){
		        						if (!lr.hasAccess(p.getName())){
		        							e.setCancelled(true);
					        				p.openInventory(right.getBlockInventory());
		        						}else{
		        							e.setCancelled(true);
					        				p.openInventory(left.getBlockInventory());
		        						}
		        					}
		        				}else{
		        					if (rr.hasProtection(this.getType())){
		        						if (!rr.hasAccess(p.getName())){
		        							e.setCancelled(true);
					        				p.openInventory(left.getBlockInventory());
		        						}else{
		        							e.setCancelled(true);
					        				p.openInventory(right.getBlockInventory());
		        						}
		        					}		        					
		        				}		        				
		        			}else if (lr != null && rr != null){
			        			boolean canOpenLeft = false;
		        				boolean canOpenRight = false;
		        				
		        				// BOTH are a region		        				
		        				if (lr.hasProtection(this.getType())){
		        					if (lr.hasAccess(p.getName())){
		        						canOpenLeft = true;		        					
		        					}
		        				}else{
		        					canOpenLeft = true;
		        				}
		        				if (rr.hasProtection(this.getType())){		
		        					if (rr.hasAccess(p.getName())){
		        						canOpenRight = true;		        				
		        					}
		        				}else{
		        					canOpenRight = true;
		        				}
		        				
		        				if (!(canOpenLeft == true && canOpenRight == true)){
		        					if (canOpenLeft){
	        							e.setCancelled(true);
				        				p.openInventory(left.getBlockInventory());		        						
		        					}else if (canOpenRight){
	        							e.setCancelled(true);
				        				p.openInventory(right.getBlockInventory());		        						
		        					}else{
	        							e.setCancelled(true);
		    		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that chest!");		        						
		        					}		        					
		        				}		        				
		        			}
		        		}
		        		
		        	}
		        }else if (e.getInventory().getHolder() instanceof Furnace){
		        	Furnace f = (Furnace) e.getInventory().getHolder();
		        	if (plugin.isProtected(f.getChunk(), this.getType())){
		            	if (!plugin.getRegion(f.getChunk()).hasAccess(p.getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that furnace!");
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof StorageMinecart){
		        	StorageMinecart s = (StorageMinecart) e.getInventory().getHolder();
		        	if (plugin.isProtected(s.getLocation().getChunk(), this.getType())){
		            	if (!plugin.getRegion(s.getLocation().getChunk()).hasAccess(p.getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that minecart!");
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof Dispenser){
		        	Dispenser d = (Dispenser) e.getInventory().getHolder();
		        	if (plugin.isProtected(d.getChunk(), this.getType())){
		            	if (!plugin.getRegion(d.getChunk()).hasAccess(p.getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that dispenser!");
		            	}
		        	}
		        	
		        }else if (e.getInventory().getHolder() instanceof BrewingStand){
		        	BrewingStand b = (BrewingStand) e.getInventory().getHolder();
		        	if (plugin.isProtected(b.getChunk(), this.getType())){
		            	if (!plugin.getRegion(b.getChunk()).hasAccess(p.getName())){
		            		e.setCancelled(true);
		            		plugin.sendMessage(p.getName(), MsgType.DENIED, "You do not have access to that brewing stand!");
		            	}
		        	}
		        }
			}
		}

    }
}
