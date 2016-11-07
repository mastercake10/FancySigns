package de.Avendria.FancySigns;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class FancySign {
	public SLLocation loc;
	public String serverName;
	public String templateName;
	
	int online = 0;
	int max = 0;
	String motd = "";
	boolean isonline = false;
	
	public void update() {
		Sign sign = (Sign) loc.get().getBlock().getState();
		sign.setLine(0, format(Main.cfg.getStringList("templates." + templateName + ".layout").get(0)));
		sign.setLine(1, format(Main.cfg.getStringList("templates." + templateName + ".layout").get(1)));
		sign.setLine(2, format(Main.cfg.getStringList("templates." + templateName + ".layout").get(2)));
		sign.setLine(3, format(Main.cfg.getStringList("templates." + templateName + ".layout").get(3)));
		sign.update();
		
		if(Main.cfg.getString("templates." + templateName + ".background").equalsIgnoreCase("wool")){
			updateBackground(Material.WOOL, getStartingColor(motd));
		}else if(Main.cfg.getString("templates." + templateName + ".background").equalsIgnoreCase("glass")){
			updateBackground(Material.STAINED_GLASS, getStartingColor(motd));
		}else if(Main.cfg.getString("templates." + templateName + ".background").equalsIgnoreCase("clay")){
			updateBackground(Material.STAINED_CLAY, getStartingColor(motd));
		}
	}
	public void updateInfo(){
		try {
			ping();
			isonline = true;	
		} catch (Exception e) {
			isonline = false;
			motd = "";
			online = 0;
		}
	}
	private int getStartingColor(String s){
		if(s.length() > 1){
			if(s.toCharArray()[0] == '§'){
				switch(s.toCharArray()[1]){
					case '0':
						return 15;
					case '1':
						return 11;
					case '2':
						return 13;
					case '3':
						return 9;
					case '4':
						return 14;
					case '5':
						return 10;
					case '6':
						return 1;
					case '7':
						return 8;
					case '8':
						return 7;
					case '9':
						return 3;
					case 'a':
						return 5;
					case 'b':
						return 9;
					case 'c':
						return 14;
					case 'd':
						return 2;
					case 'e':
						return 4;
					case 'f':
						return 0;
					default:
						return 0;
				}
			}else{
				return 0;
			}
		}else{
			return 0;
		}
	}
	private void updateBackground(Material mat, int color){
		Location loc3 = loc.get();
		Sign s = (Sign) loc3.getBlock().getState();
		if(s.getType() == Material.WALL_SIGN){
			BlockFace bf = ((Directional) s.getData()).getFacing();
			Location loc2 = new Location(loc3.getWorld(), loc3.getBlockX() - bf.getModX(), loc3.getBlockY() - bf.getModY(), loc3.getBlockZ() - bf.getModZ());
			Block wall = loc2.getBlock();
			wall.setType(mat);
			wall.setData((byte) color);
			
		}
		
	}
	private String format(String s){
		s = s.replace("%name%", serverName);
		s = s.replace("%online%", online + "");
		s = s.replace("%slots%", max + "");
		String isOn = Main.cfg.getString("templates." + templateName + ".offline");
		if(isonline){
			isOn = Main.cfg.getString("templates." + templateName + ".online");
		}
		s = s.replace("%isonline%", isOn);
		String finalmotd = motd;
		if(finalmotd.length() > 16){
			finalmotd = finalmotd.substring(0, 16);
		}
		s = s.replace("%motd%", finalmotd);
		s = s.replace('&', '§');
		return s;
	}
	
	private void ping() throws UnknownHostException, IOException{
		Server s = Main.manager.getServerFromName(serverName);
		if(s == null) return;
		Socket socket = new Socket(s.ip, s.port);
		socket.setSoTimeout(10000);
		Packet p = new PacketHandshake(s.ip, s.port, 1, 47);
		p.write(socket.getOutputStream());
		socket.getInputStream().read();
		//int length = socket.getInputStream().read();
		//byte[] buf = new byte[length + 1];
		//socket.getInputStream().read(buf, 0, length + 1);
		//String json = new String(buf, StandardCharsets.UTF_8);
		StringBuilder sb = new StringBuilder();
		char old = 'a';
		while(true){
			char c = (char) socket.getInputStream().read();
			sb.append(c);
			if(sb.length() > 10){
				if(c == '}' && old == '}'){
					break;
				}
			}
			old = c;
		}
		String json = new String(sb.toString().getBytes("ISO-8859-1"), StandardCharsets.UTF_8);
		char old2 = 'a';
		int starting = 0;
		for(int i = 0; i < json.length(); i++){
			char c = json.charAt(i);
			if(old2 == '{' && c == '\"'){
				starting = i;
				break;
			}
			old2 = c;
		};
		json = json.substring(starting - 1);
		/*JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(reader).getAsJsonObject();
		
		motd = o.get("description").getAsJsonObject().get("text").getAsString();
		online = o.get("players").getAsJsonObject().get("online").getAsInt();
		max = o.get("players").getAsJsonObject().get("max").getAsInt();*/
		motd = json.split("\"description\":")[1].split(",")[0];
		if(motd.contains("\"text\"")){ //Extract json
			motd = motd.split("\"text\":\"")[1].split("\"")[0];
		}
		motd = motd.replaceAll("\"", "");
		try{
			online = Integer.parseInt(json.split("\"online\":")[1].split(",")[0]);	
		}catch(NumberFormatException e){
			online = Integer.parseInt(json.split("\"online\":")[1].split("}")[0]);
		}
		max = Integer.parseInt(json.split("\"max\":")[1].split(",")[0]);
		socket.close();
		
	}
	public void teleport(Player player) {
		if(Main.cfg.getBoolean("templates." + templateName + ".joinOnClick")){
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF(serverName);
			Main.sendPluginMessage(player, out.toByteArray());
		}
		
	}
	
}
