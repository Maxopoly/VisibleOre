package com.github.civcraft.visibleore;

import com.github.civcraft.visibleore.commands.VisibleOreCommandHandler;

import vg.civcraft.mc.civmodcore.ACivMod;

public class VisibleOrePlugin extends ACivMod {

    private static VisibleOrePlugin instance;

    public void onEnable() {
		super.onEnable();
		instance = this;
		handle = new VisibleOreCommandHandler();
		handle.registerCommands();
		ConfigParser cp = new ConfigParser(this);
		cp.parse();
    }

    public void onDisable() {

    }

    @Override
    protected String getPluginName() {
    	return "VisibleOre";
    }

    public static VisibleOrePlugin getInstance() {
    	return instance;
    }

}
