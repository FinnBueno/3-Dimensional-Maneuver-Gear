package me.finnbon.maneuvergear.gear;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum Side {
	
	LEFT, RIGHT;
	
	public Location getHandSide(Player player) {
		Location location = player.getLocation();
		float angle = location.getYaw() / 60;
		if (this == LEFT) {
			return location.add(Math.cos(angle) * .5, .6, Math.sin(angle) * .5);
		} else {
			return location.subtract(Math.cos(angle) * .5, -.6, Math.sin(angle) * .5);
		}
	}
	
}