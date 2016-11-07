package de.Avendria.FancySigns;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Manager {
	//Signs
    File signs_file = new File(Main.data_dir + "/" + "signs.yml");
    List<FancySign> signs = new ArrayList<FancySign>();
    //Server-Infos
    DataFetcher data;
    
    Gson gson;
    
    public Manager(){
    	data = new DataFetcher();
    	Main.pl.getServer().getMessenger().registerIncomingPluginChannel(Main.pl, "BungeeCord", data);
    	gson = new Gson();
    }
    public void loadSignsFromFile(){
        if(!signs_file.exists()) return;
        String json = "";
		try {
			json = Files.readAllLines(signs_file.toPath(), Charset.defaultCharset()).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Type t = new TypeToken<ArrayList<FancySign>>() {}.getType();
        signs = gson.fromJson(json, t);
        Main.log(signs.size() + " signs loaded (size: " + signs_file.length() + " bytes)");
    }
    public void saveSignsToFile(){
    	String json = gson.toJson(signs);
    	try {
			Files.write(signs_file.toPath(), json.getBytes(Charset.defaultCharset()));
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
    public void updateSignBlocks(){
    	Iterator<FancySign> it = signs.iterator();
    	while(it.hasNext()){
    		FancySign fs = it.next();
    		Location loc = fs.loc.get();
    		if(loc.getBlock() != null){
    			Block b = loc.getBlock();
    			if(b.getState() instanceof Sign){
    				fs.updateInfo();
    			}else{
    				it.remove();
    			}
    		}
    	}
    }
    public void updateSignInfos(){
    	Iterator<FancySign> it = signs.iterator();
    	while(it.hasNext()){
    		FancySign fs = it.next();
    		fs.updateInfo();
    	}
    }
    public FancySign getSignAt(Location loc){
    	for(FancySign fs : signs){
    		if(fs.loc.get().getBlockX() == loc.getBlockX() && fs.loc.get().getBlockY() == loc.getBlockY() && fs.loc.get().getBlockZ() == loc.getBlockZ()){
    			return fs;
    		}
    	}
    	return null;
    }
    public void updateSigns(){
    	Iterator<FancySign> it = signs.iterator();
    	while(it.hasNext()){
    		FancySign fs = it.next();
    		Location loc = fs.loc.get();
    		if(loc.getBlock() != null){
    			Block b = loc.getBlock();
    			if(b.getState() instanceof Sign){
    				fs.update();
    			}else{
    				it.remove();
    			}
    		}
    	}
    }
    public boolean isSignAt(Location loc){
    	for(FancySign fs : signs){
    		Location loc2 = fs.loc.get();
    		if(loc.getBlockX() == loc2.getBlockX() && loc.getBlockY() == loc2.getBlockY() && loc.getBlockZ() == loc2.getBlockZ()){
    			return true;
    		}
    	}
    	return false;
    }
    public void addSign(FancySign fs){
    	signs.add(fs);
    }
    public void removeSign(Location loc){
    	FancySign toRem = null;
    	for(FancySign fs : signs){
    		Location loc2 = fs.loc.get();
    		if(loc2.getBlockX() == loc.getBlockX() && loc2.getBlockY() == loc.getBlockY() && loc2.getBlockZ() == loc.getBlockZ()){
    			toRem = fs;
    			break;
    		}
    	}
    	if(toRem == null){
    		return;
    	}
    	signs.remove(toRem);
    }
    //Server managing
    public Server getServerFromName(String name){
    	for(Server s : data.fetchedServers){
    		if(s.name.equals(name)) return s;
    	}
    	return null;
    }
    public boolean serverExist(String name){
    	for(Server s : data.fetchedServers){
    		if(s.name.equals(name)) return true;
    	}
    	return false;
    }
    public List<Server> getServers(){
    	return data.fetchedServers;
    }
    //Template managing
}