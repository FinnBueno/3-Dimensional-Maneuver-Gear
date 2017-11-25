package me.finnbon.maneuvergear.gear;

import me.finnbon.maneuvergear.crafting.CraftingManager;
import me.finnbon.maneuvergear.util.BlockUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

import static me.finnbon.maneuvergear.gear.Side.LEFT;
import static me.finnbon.maneuvergear.gear.Side.RIGHT;

/**
 * This class represents the 3DMG around the player's hips. This class holds a {@link ConcurrentHashMap} to store the player's left and right {@link HookTip}.
 */
class Hook {

    private Player player;
    // The hook tips, with the side enum as their key
    private ConcurrentHashMap<Side, HookTip> tips = new ConcurrentHashMap<>();

    Hook(Player player) {
        this.player = player;
    }

    /**
     * Called every tick to progress the hooks and potentially pull the user towards them
     * @return Whether the 3DMG should be removed or not
     */
    boolean progress() {
        if (player == null || !player.isOnline() || player.isDead())
            return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.isSimilar(CraftingManager.HOOK))
            return false;

        if (tips.size() == 0)
            return true;

        // disable flying
        player.setFlying(false);
        player.setAllowFlight(false);

        // progress hook tips and remove in case needed
        for (Side s : tips.keySet()) {
            HookTip tip = tips.get(s);
            Location side = s.getHandSide(player);
            if (!tip.progress() || !drawLine(side, tip.getLocation())) {
                tips.remove(s);
                if (tips.size() == 0)
                    stop();
            }
        }

        // pull player towards struck hook tips
        if (player.isSneaking() && tips.size() != 0) {

            // get target location
            Location target = null;
            int noHit = 0;
            for (Side s : tips.keySet()) {
                HookTip tip = tips.get(s);
                if (!tip.hasHit()) {
                    noHit++;
                    continue;
                }
                if (target == null)
                    target = tip.getLocation();
                else
                    target.add(target.clone().subtract(tip.getLocation()).toVector().multiply(-0.5));

            }
            if (target == null)
                return true;

            if (noHit == tips.size())
                return true;

            // get direction to target location
            Vector flyDirection =
                    (target.distanceSquared(player.getLocation()) < ((tips.size() == 2) ? 4 : 1)) ?
                            new Vector() :
                            target.clone().subtract(player.getLocation()).toVector().normalize().multiply(1.75);

            // get angle between cable(s) direction and player's direction
            Vector playerDir = player.getLocation().getDirection()
                    .multiply(1.75);
            double angle = Math.toDegrees(flyDirection.angle(playerDir));

            // get maximum allowed angle
            double allowedAngle = Math.toDegrees(Math.atan(2.5 / player.getLocation().distance(target))) + 1;

            // set swinging angle
            Vector dir = angle > allowedAngle ? target.clone().subtract(player.getLocation()).toVector().normalize().multiply(1.75) : player.getLocation().getDirection().multiply(1.75);

            // actual pulling
            player.setAllowFlight(true);
            player.setFlying(false);
            player.setFallDistance(0F);
            player.setVelocity(dir);

        }

        return true;
    }

    /**
     * Draw a particle line and check if the line got obstructed or not
     * @param origin The start location
     * @param dest The destination location
     * @return Whether the line got obstructed by any solid blocks or not
     */
    private boolean drawLine(Location origin, Location dest) {
        Vector dir = dest.toVector().subtract(origin.toVector());
        double length = dir.length();
        dir.normalize();

        double x = dir.getX();
        double y = dir.getY();
        double z = dir.getZ();

        for (double i = 0.5D; i < length; i += 0.5D) {
            origin.add(x * i, y * i, z * i);

            if (BlockUtil.isSolid(origin.getBlock())) {
                origin.subtract(x * i, y * i, z * i);
                return false;
            }

            origin.getWorld().spawnParticle(Particle.REDSTONE, origin, 0, 0.549019608F, 0.549019608F, 0.549019608F, 0F);

            origin.subtract(x * i, y * i, z * i);
        }
        return true;
    }

    /**
     * Used to activate the specified side
     *
     * @param side The side to activate
     */
    void activate(Side side) {
        switch (side) {
            case LEFT:
                activateLeft();
                break;
            case RIGHT:
                activateRight();
                break;
        }
    }

    /**
     * Used to launch the left hook
     */
    private void activateLeft() {
        tips.put(LEFT, new HookTip(this, LEFT.getHandSide(player), player.getLocation().getDirection(), 25D));
    }

    /**
     * Used to launch the right hook
     */
    private void activateRight() {
        tips.put(RIGHT, new HookTip(this, RIGHT.getHandSide(player), player.getLocation().getDirection(), 25D));
    }

    /**
     * Used when the user moves to a different slot
     */
    void stop() {
        tips.clear();
        player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR);
        player.setFlying(player.getGameMode() == GameMode.SPECTATOR);
    }

    /**
     * @return The hook user
     */
    Player getPlayer() {
        return player;
    }

}