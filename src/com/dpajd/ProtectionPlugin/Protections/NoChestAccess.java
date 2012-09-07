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
import com.dpajd.ProtectionPlugin.Regions.Owner;
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
		            	}
		            }
		        }else if (e.getInventory().getHolder() instanceof DoubleChest){
		        	DoubleChest dc = (DoubleChest) e.getInventory().getHolder();
		        	Chest left = (Chest) dc.getLeftSide();
		        	Chest right = (Chest) dc.getRightSide();
		        	
		        	if (plugin.isProtected(left.getChunk(), this.getType()) || plugin.isProtected(right.getChunk(), this.getType())){
		        		Region rl = plugin.getRegion(left.getChunk());
		        		Region rr = plugin.getRegion(right.getChunk());
		        		if (rl.equals(rr)){
		        			if (!rl.hasAccess(e.getPlayer().getName())){
		        				e.setCancelled(true);
		        			}
		        		}else{
		        			boolean al = rl.hasAccess(e.getPlayer().getName());
		        			boolean ar = rr.hasAccess(e.getPlayer().getName());
		        			if (!(!(al ^ ar) && al)){ // if no access to one of the chest parts
		        				e.setCancelled(true);
		        			}
		        		}
		        	}
		        }else if (e.getInventory().getHolder() instanceof Furnace){
		        	Furnace f = (Furnace) e.getInventory().getHolder();
		        	if (plugin.isProtected(f.getChunk(), this.getType())){
		            	if (!plugin.getRegion(f.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof StorageMinecart){
		        	StorageMinecart s = (StorageMinecart) e.getInventory().getHolder();
		        	if (plugin.isProtected(s.getLocation().getChunk(), this.getType())){
		            	if (!plugin.getRegion(s.getLocation().getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            	}
		        	}
		        }else if (e.getInventory().getHolder() instanceof Dispenser){
		        	Dispenser d = (Dispenser) e.getInventory().getHolder();
		        	if (plugin.isProtected(d.getChunk(), this.getType())){
		            	if (!plugin.getRegion(d.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            	}
		        	}
		        	
		        }else if (e.getInventory().getHolder() instanceof BrewingStand){
		        	BrewingStand b = (BrewingStand) e.getInventory().getHolder();
		        	if (plugin.isProtected(b.getChunk(), this.getType())){
		            	if (!plugin.getRegion(b.getChunk()).hasAccess(e.getPlayer().getName())){
		            		e.setCancelled(true);
		            	}
		        	}
		        }
			}
		}

    }
}
