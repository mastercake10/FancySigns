package de.Avendria.FancySigns;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SLLocation {
	double x;
	double z;
	double y;
	float pitch;
	float yaw;
	String w;
	
	public SLLocation(Location loc) {
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		pitch = loc.getPitch();
		yaw = loc.getYaw();
		w = loc.getWorld().getName();
	}
	
	public Location get() {
		Location loc = new Location(Bukkit.getWorld(w), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
	}
}
