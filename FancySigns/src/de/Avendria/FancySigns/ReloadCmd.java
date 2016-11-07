package de.Avendria.FancySigns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCmd implements CommandExecutor {
	Main clasz;
	public ReloadCmd(Main main) {
		clasz = main;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] arg3) {
		if(!cs.hasPermission("fancysigns.use")){
			cs.sendMessage("§cYou dont have permissions!");
			return true;
		}
		clasz.reload();
		cs.sendMessage("§cPlugin reloaded!");
		return true;
	}

}
