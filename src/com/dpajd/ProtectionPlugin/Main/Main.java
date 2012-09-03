package com.dpajd.ProtectionPlugin.Main;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public final Logger log = Logger.getLogger("Minecraft");
	public PluginDescriptionFile pdf;
	public final Permission protect = new Permission("bp.protect");
	
	@Override
	public void onEnable() {
		pdf = this.getDescription();
		log.info("You have now enabled " + pdf.getName() + " Version "
				+ pdf.getVersion() + " Made by " + pdf.getAuthors());
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		FileConfiguration cfg = getConfig();
		cfg.createSection("Simply change the id to a Valid ID to change the item used to select");
		cfg.addDefault("Item.InHand", 286); // golden axe
		cfg.options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onDisable() {
		log.info(ChatColor.GOLD + "You have now disabled " + pdf.getName()
				+ " Version " + pdf.getVersion() + " Made by "
				+ pdf.getAuthors());
	}

	public void GenerateFence(Location loc1, Location loc2) {
		int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math
				.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(
				loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(
				loc1.getBlockX(), loc2.getBlockX()), maxz = Math.max(
				loc1.getBlockZ(), loc2.getBlockZ());
		if (!loc1.getWorld().equals(loc2.getWorld())) {
			Location loc = new Location(loc1.getWorld(), minx, miny, minz);
			while (loc.getBlockX() < maxx) {
				loc.setY((loc.getWorld().getHighestBlockYAt(loc) + 1));
				loc.getBlock().setType(Material.FENCE);
				loc.setX(loc.getX() + 1);
			}
			while (loc.getBlockZ() < maxz) {
				loc.setY((loc.getWorld().getHighestBlockYAt(loc) + 1));
				loc.getBlock().setType(Material.FENCE);
				loc.setZ(loc.getZ() + 1);
			}
			while (loc.getBlockX() > minx) {
				loc.setY((loc.getWorld().getHighestBlockYAt(loc) + 1));
				loc.getBlock().setType(Material.FENCE);
				loc.setX(loc.getX() - 1);
			}
			while (loc.getBlockX() > minz) {
				loc.setY((loc.getWorld().getHighestBlockYAt(loc) + 1));
				loc.getBlock().setType(Material.FENCE);
				loc.setZ(loc.getZ() - 1);
			}
			Location gate1 = new Location(loc1.getWorld(), (minx + maxx) / 2,
					1, minz);
			gate1.setY(gate1.getWorld().getHighestBlockYAt(gate1) + 1);
			Location gate2 = new Location(loc1.getWorld(), (minx + maxx) / 2,
					1, maxz);
			gate1.setY(gate1.getWorld().getHighestBlockYAt(gate2) + 1);
			Location gate3 = new Location(loc1.getWorld(), minx, 1,
					(minz + maxz) / 2);
			gate1.setY(gate1.getWorld().getHighestBlockYAt(gate3) + 1);
			Location gate4 = new Location(loc1.getWorld(), maxx, 1,
					(minz + maxz) / 2);
			gate1.setY(gate1.getWorld().getHighestBlockYAt(gate4) + 1);
			gate1.getBlock().setType(Material.FENCE_GATE);
			gate2.getBlock().setType(Material.FENCE_GATE);
			gate3.getBlock().setType(Material.FENCE_GATE);
			gate4.getBlock().setType(Material.FENCE_GATE);
		}
	}
}