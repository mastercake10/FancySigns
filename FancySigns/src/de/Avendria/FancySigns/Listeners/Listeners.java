package de.Avendria.FancySigns.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.Avendria.FancySigns.FancySign;
import de.Avendria.FancySigns.Main;
import de.Avendria.FancySigns.SLLocation;

public class Listeners implements Listener{
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block b = e.getClickedBlock();
			if(b.getState() instanceof Sign){
				FancySign fs = Main.manager.getSignAt(b.getLocation());
				if(fs != null){
					fs.teleport(e.getPlayer());
				}
			}
		}
	}
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("[fsigns]") || e.getLine(0).equalsIgnoreCase("[fancysigns]")) {
			Player player = e.getPlayer();
			if(!player.hasPermission("fancysigns.use")){
				player.sendMessage("§cYou don't have permission to create FancySigns!");
				e.setCancelled(true);
				return;
			}
			if(!Main.manager.serverExist(e.getLine(1))){
				player.sendMessage("§cThis server does not exist! Available servers:");
				String out = "";
				for(de.Avendria.FancySigns.Server server : Main.manager.getServers()){
					out = out + server.name + ", ";
				}
				out = out.substring(0, out.length() - 2);
				player.sendMessage(ChatColor.YELLOW + out);
				e.setCancelled(true);
				return;
			}
			if(e.getLine(2).length() < 1 || !Main.cfg.contains("templates." + e.getLine(2))){
				player.sendMessage("§cThis template does not exist!");
				e.setCancelled(true);
				return;
			}
			FancySign fs = new FancySign();
			fs.serverName = e.getLine(1);
			fs.templateName = e.getLine(2);
			fs.loc = new SLLocation(e.getBlock().getLocation());
			player.sendMessage("§aFancySign created!");
			Main.manager.addSign(fs);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onBreak(BlockBreakEvent e){
		if(Main.manager.isSignAt(e.getBlock().getLocation())){
			Main.manager.removeSign(e.getBlock().getLocation());
			e.getPlayer().sendMessage("§cFancySign destroyed!");
		}
	}
}
