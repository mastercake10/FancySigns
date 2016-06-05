package de.Avendria.FancySigns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class DataFetcher implements PluginMessageListener{
	public boolean fatched = false;
	
	public DataFetcher(){
	    
	    fetch();
	}
	public void fetch(){
		Main.log("Getting some Server infos...");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		Main.sendPluginMessage(player, out.toByteArray());
	}
	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("BungeeCord")) {
	        return;
	    }
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    if (subchannel.equals("GetServers")){
		    String[] serverList = in.readUTF().split(", ");
		    
	    }
	}
}
