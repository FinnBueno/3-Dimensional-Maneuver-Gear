package me.finnbon.maneuvergear.gear;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.finnbon.maneuvergear.ManeuverGear;
import me.finnbon.maneuvergear.util.BlockUtil;

/**
 * This class represents the tip of any launched hook
 */
class HookTip {

    // The max range of any hook
	private static double RANGE = Double.NaN;
	
	private Vector dir;
	private double speed;
	private Location loc;
	private boolean hit;
	private Hook originUser;
	private int travelled;

    /**
     * @param originUser The user
     * @param loc The start location
     * @param direction The user's direction
     * @param speed The hook's speed
     */
	HookTip(Hook originUser, Location loc, Vector direction, double speed) {
		this.originUser = originUser;
		this.dir = direction;
		this.dir.normalize().multiply(0.4);
		this.speed = speed;
		this.loc = loc;
		this.hit = false;
		this.travelled = 0;
		if (RANGE == Double.NaN)
			RANGE = JavaPlugin.getPlugin(ManeuverGear.class).getConfig().getInt("Range");
	}

    /**
     * Called every tick to progress the hook
     * @return Whether the hook tip should be removed or not
     */
	boolean progress() {
		// check the distance
		if (originUser.getPlayer().getEyeLocation().distanceSquared(loc) > RANGE * RANGE)
			return false;
		
		if (originUser.getPlayer().getLocation().distanceSquared(loc) <= 1 && !originUser.getPlayer().isSneaking() && hit) {
			return false;
		}
		
		// check if the hook got a hit and if so, if the object is struck with still exists
		if (hit) {
			loc.add(dir);
			if (!BlockUtil.isSolid(loc.getBlock()))
				return false;
			loc.subtract(dir);
		}

		// if the hook tip has hit an object, stop moving
		if (hit)
			return true;
		
		// move the hook tip forward
		Block block;
		for (int i = 1; i <= speed; i++) {
		    travelled++;
		    if (travelled > RANGE) {
		        return false;
            }
			loc.add(dir);
			block = loc.getBlock();
			if (BlockUtil.isSolid(block)) {
				loc.subtract(dir);
				hit = true;
				originUser.getPlayer().playSound(originUser.getPlayer().getEyeLocation(), Sound.BLOCK_ANVIL_PLACE, 1F, 1F);
				break;
			}
		}
		return true;
	}

    /**
     * Get the current location of the hooktip
     * @return The current location of the hooktip
     */
	Location getLocation() {
		return loc.clone();
	}

    /**
     * Whether the hooktip has made contact with a solid surface
     * @return Whether the hooktip has made contact with a solid surface
     */
	boolean hasHit() {
		return hit;
	}
	
}