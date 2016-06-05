package de.Avendria.FancySigns;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.Avendria.FancySigns.Listeners.Listeners;

public class Main extends JavaPlugin{

	@Override
	public void onEnable(){
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Listeners(), this);
	}
	
	@Override
	public void onDisable(){
		
	}

}


// Schilderaufbau:

// [FS]
// servername
// templatename
//