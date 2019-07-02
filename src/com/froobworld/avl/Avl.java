package com.froobworld.avl;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.froobworld.avl.metrics.Metrics;
import com.froobworld.avl.tasks.CompatibilityCheckTask;
import com.froobworld.avl.tasks.MainTask;

public class Avl extends JavaPlugin {
	private AvlConfiguration config;

	@Override
	public void onEnable() {
		if (new CompatibilityCheckTask(this).passedCheck()) {
			config = new AvlConfiguration(this, AvlConfiguration.CONFIG_CURRENT_VERSION, "config.yml");
			config.loadFromFile();

			addTasks();

			new Metrics(this);
			logger().info("Successfully enabled.");
		}
	}

	private void addTasks() {
		long ticksPerAllowSearch = config.getLong("ticks-per-allow-search",
				600L /* Default value, if config does not contain the entry */);
		Bukkit.getScheduler().runTaskTimer(this, new MainTask(this), 0L, ticksPerAllowSearch <= 0 ? 600 : ticksPerAllowSearch);
	}

	public AvlConfiguration getAvlConfig() {
		return config;
	}

	public static Logger logger() {
		return JavaPlugin.getPlugin(Avl.class).getLogger();
	}
}
