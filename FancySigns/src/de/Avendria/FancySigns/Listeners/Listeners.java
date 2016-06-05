package de.Avendria.FancySigns.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import de.Avendria.FancySigns.ConfigManager;

public class Listeners implements Listener{

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[FS]")) {
			//Das geänderte schild ist ein FancySigns
			Player player = event.getPlayer();
			if (player.hasPermission("FancySigns.create")){
				if(ConfigManager.doServerExists(event.getLine(1))) {
					if(ConfigManager.doTemplateExists(event.getLine(2))){
						//Schild kann registrierd werden
						
						
					}else{
						//Template existiert nicht
						
					}
				}else{
					//Server existiert nicht
					
				}
			}else{
				//Der Spieler hat keine Rechte zum erstellen eines FancySigns
				player.sendMessage("You dont have Permissions");
				return;
			} 
		}
	}
}
