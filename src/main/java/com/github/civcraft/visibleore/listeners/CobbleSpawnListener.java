package com.github.civcraft.visibleore.listeners;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import vg.civcraft.mc.civmodcore.util.BiasedRandomPicker;

public class CobbleSpawnListener implements Listener {
	
	private Collection <BlockFace> sides;
	private BiasedRandomPicker<Material> picker;
	
	public CobbleSpawnListener(Map <Material, Double> chances) {
		this.picker = new BiasedRandomPicker<Material>(chances);
		sides = new HashSet<BlockFace>();
		sides.add(BlockFace.NORTH);
		sides.add(BlockFace.SOUTH);
		sides.add(BlockFace.EAST);
		sides.add(BlockFace.WEST);
	}
	
	@EventHandler
	public void liquidSpread(BlockFromToEvent e) {
		if (!isLiquid(e.getBlock().getType())) {
			return;
		}
		Block origin = e.getBlock();
		Block b = e.getToBlock();
		if (isLava(origin.getType()) && b.getType() == Material.AIR) {
			for(BlockFace bf : sides) {
				if (isWater(b.getRelative(bf).getType())) {
					e.setCancelled(true);
					applyEffect(b);
				}
			}
		}
		if (isWater(origin.getType())) {
			//if block below the one the water is flowing in is lava, it is transformed
			if (isLava(b.getRelative(BlockFace.DOWN).getType())) {
				if (b.getRelative(BlockFace.DOWN).getData() != 0) {
					//0 means source block, which would mean obsidian, but we only want to catch cobble gen
					applyEffect(b.getRelative(BlockFace.DOWN));
				}
			}
			for(BlockFace bf : sides) {
				Block side = b.getRelative(bf);
				if (isLava(side.getType())) {
					applyEffect(side);
				}
			}
		}
	}
	
	public void applyEffect(Block b) {
		Material m = picker.getRandom();
		b.setType(m);
	}
	
	public static boolean isLiquid(Material m) {
		return isWater(m) || isLava(m);
	}
	
	public static boolean isLava(Material m) {
		return m == Material.STATIONARY_LAVA || m == Material.LAVA;
	}
	
	public static boolean isWater(Material m) {
		return m == Material.STATIONARY_WATER || m == Material.WATER;
	}

}
