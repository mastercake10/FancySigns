package de.Avendria.FancySigns;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class DataFetcher implements PluginMessageListener {
	public boolean fetched = false;
	public List<Server> buffer = new ArrayList<Server>();
	public List<Server> fetchedServers = new ArrayList<Server>();
	int cnt = 0;
	
	public DataFetcher() {
	
	}
	
	public void fetch() {
		buffer.clear();
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		Main.sendPluginMessage(player, out.toByteArray());
	}
	
	public void fetchIps(String[] servers) {
		for (String s : servers) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("ServerIP");
			out.writeUTF(s);
			Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			Main.sendPluginMessage(player, out.toByteArray());
		}
	}
	
	int cnt2 = 0;
	
	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServers")) {
			String[] serverList = in.readUTF().split(", ");
			cnt = serverList.length;
			fetchIps(serverList);
		} else if (subchannel.equals("ServerIP")) {
			String serverName = in.readUTF();
			String ip = in.readUTF();
			short port = in.readShort();
			buffer.add(new Server(serverName, ip, port));
			cnt2++;
			if (cnt2 >= cnt) {
				//Fetched all servers
				fetched = true;
				//Swap
				fetchedServers = buffer;
			}
		}
	}
}
