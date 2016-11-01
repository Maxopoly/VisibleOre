package com.github.civcraft.visibleore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.civcraft.visibleore.listeners.CobbleSpawnListener;

public class ConfigParser {
	
	private VisibleOrePlugin plugin;
	
	public ConfigParser(VisibleOrePlugin plugin) {
		this.plugin = plugin;
	}
	
	public void parse() {
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		Map <Material, Double> mapping = parseDistribution(config.getConfigurationSection("distribution"));
		if (mapping != null) {
			Bukkit.getPluginManager().registerEvents(new CobbleSpawnListener(mapping), plugin);
		}
	}
	
	
	public Map <Material, Double> parseDistribution(ConfigurationSection config) {
		Map <Material, Double> mapping = new HashMap<Material, Double>();
		if (config == null) {
			plugin.warning("No distribution section specified, disabling plugin");
			Bukkit.getPluginManager().disablePlugin(plugin);
			return null;
		}
		double sum = 0.0;
		for(String key : config.getKeys(false)) {
			if (!(config.isDouble(key))) {
				plugin.warning("Invalid identifier " + key + " in distribution section, skipping it");
				continue;
			}
			double chance = config.getDouble(key);
			if (chance <= 0.0 || chance > 1.0) {
				plugin.warning(chance + " is not a valid spawning chance, chance must be within (0.0, 1.0] ");
				continue;
			}
			Material mat;
			try {
				mat = Material.valueOf(key);
			}
			catch (IllegalArgumentException e) {
				plugin.warning("Invalid material identifier " + key + " in distribution section, skipping it");
				continue;
			}
			sum += chance;
			mapping.put(mat, chance);
		}
		if (sum < 1.0) {
			mapping.put(Material.COBBLESTONE, 1.0 - sum);
		}
		return mapping;
	}

}
