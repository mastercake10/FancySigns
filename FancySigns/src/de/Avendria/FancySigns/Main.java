package de.Avendria.FancySigns;


import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.Avendria.FancySigns.Listeners.Listeners;

public class Main extends JavaPlugin{
	public static File data_dir;
	public static Plugin pl;
	public static Manager manager;
	public static FileConfiguration cfg;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		Main.pl = this;
		data_dir = this.getDataFolder();
		
		//Load Signs
		manager = new Manager();
		manager.loadSignsFromFile();
	
		//Config
		this.saveDefaultConfig();
		cfg = this.getConfig();
		
		//Listener
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new Listeners(), this);
		
		//Commands
		this.getCommand("fancysigns").setExecutor(new ReloadCmd(this));
		
		
	    Main.pl.getServer().getMessenger().registerOutgoingPluginChannel(Main.pl, "BungeeCord");
	    
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
			public void run(){
				manager.updateSignInfos();
				//Getting new server-infos
				if(Bukkit.getOnlinePlayers().size() > 0){
					//Only works if player is online
					updateServerInfos();
				
				}
			}
		}, 20L, 20L);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				manager.updateSigns();
				//manager.updateSigns();
			}
		}, 20L, 20L);
	}
	public void reload(){
		Bukkit.getPluginManager().disablePlugin(this);
		Bukkit.getPluginManager().enablePlugin(this);
		//onDisable();
		//onEnable();
	}
	public static void sendPluginMessage(Player p, byte[] ar){
		p.sendPluginMessage(Main.pl, "BungeeCord", ar);
	}
	public void updateServerInfos(){
		Main.pl.getServer().getScheduler().scheduleSyncDelayedTask(Main.pl, new Runnable(){
			public void run(){
				manager.data.fetch();
			}
		}, 5L);
	}
	@Override
	public void onDisable(){
		manager.saveSignsToFile();
	}
	public Manager getManager(){
		return manager;
	}
	public static void log(String msg){
		System.out.println("[FancySigns] " + msg);
	}

}


// Schilderaufbau:

// [FS]
// servername
// templatename
//