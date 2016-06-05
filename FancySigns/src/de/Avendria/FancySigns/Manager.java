package de.Avendria.FancySigns;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class Manager {
	//Signs
    File signs_file = new File(Main.data_dir + "/" + "signs.yml");
    YamlConfiguration signs_cfg;
    List<Location> sign_locs = new ArrayList<Location>();
  
    //Server-Infos
    DataFetcher data;
    
    public Manager(){
    	data = new DataFetcher();
    	Main.pl.getServer().getMessenger().registerIncomingPluginChannel(Main.pl, "BungeeCord", data);
    }
    public void loadSignsFromFile(){
        if(!signs_file.exists()) return;
        signs_cfg = YamlConfiguration.loadConfiguration(signs_file);
        sign_locs = stringsToLocations(signs_cfg.getStringList("locs"));
        Main.log(sign_locs.size() + " signs loaded (size: " + signs_file.length() + " bytes)");
    }
    public void saveSignsToFile(){
    	signs_cfg.set("locs", locationsToString(sign_locs));
        try {
        	signs_cfg.save(signs_file);
		} catch (IOException e) {
			Main.log("Error saving signs.yml: " + e.getCause().getCause().toString());
		}
        Main.log("Signs saved (size: " + signs_file.length() + " bytes)");
    }
    public List<String> locationsToString(List<Location> locs){
    	List<String> strings = new ArrayList<String>();
    	for(Location loc : locs){
    		strings.add(loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ());
    	}
    	return strings;
    }
    public List<Location> stringsToLocations(List<String> strings){
    	List<Location> locations = new ArrayList<Location>();
    	for(String raw2 : strings){
    		String[] raw = raw2.split(":");
    		locations.add(new Location(Bukkit.getWorld(raw[0]), Double.parseDouble(raw[1]), Double.parseDouble(raw[2]), Double.parseDouble(raw[3])));
    	}
    	return locations;
    }
    public void addSign(Location loc){
    	sign_locs.add(loc);
    }
    public void removeSign(Location loc){
    	Location toRem = null;
    	for(Location loc2 : sign_locs){
    		if(loc2.getBlockX() == loc.getBlockX() && loc2.getBlockY() == loc.getBlockY() && loc2.getBlockZ() == loc.getBlockZ()){
    			toRem = loc2;
    			break;
    		}
    	}
    	if(toRem == null){
    		return;
    	}
    	sign_locs.remove(toRem);
    }
    
}