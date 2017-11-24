package me.finnbon.maneuvergear.gear;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class manages all the hook instances, so basically all the players that are using 3dmg. \nIt contains a
 * {@see java.util.Set} to hold all the {@link Hook} instances
 */
public class HookManager extends BukkitRunnable implements Listener {

    private static final Set<Hook> INSTANCES = new HashSet<>();

    /**
     * <B>DO NOT CALL BY YOURSELF. SHOULD ONLY BE CALLED ONCE BY THE PLUGIN ITSELF</B>
     * @param plugin The {@link JavaPlugin} to start the {@link BukkitRunnable} with
     */
    public HookManager(JavaPlugin plugin) {
        runTaskTimer(plugin, 0L, 1L);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void run() {
        List<Hook> toRemove = new ArrayList<>();
        for (Hook hook : INSTANCES)
            if (!hook.progress()) {
                hook.stop();
                toRemove.add(hook);
            }
        INSTANCES.removeAll(toRemove);
        toRemove.clear();
    }

    /**
     * Listener to listen for when a player wants to launch a hook
     *
     * @param event The event that was fired indicating the player is left or
     *              right clicking
     */
    @EventHandler
    public void click(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Hook instance = getInstance(player);
        if (instance == null)
            return;


        Action act = event.getAction();
        if (act != Action.PHYSICAL)
            instance.activate(Side.valueOf(act.name().split("_")[0]));
    }

    /**
     * Called when a player takes the hooks into their active slot
     *
     * @param player The player performing the action
     */
    public void start(Player player) {
        for (Hook hook : INSTANCES)
            if (hook.getPlayer().getEntityId() == player.getEntityId())
                return;
        if (!player.hasPermission("3dmg.use"))
            player.sendMessage(ChatColor.RED + "You're not allowed to use the 3D Maneuver Gear!");
        else
            INSTANCES.add(new Hook(player));
    }

    /**
     * Called when a player takes the hooks out of their active slot
     *
     * @param player The player performing the action
     */
    public void stop(Player player) {
        Hook toRemove = null;
        for (Hook hook : INSTANCES)
            if (hook.getPlayer() == player) {
                toRemove = hook;
                hook.stop();
                break;
            }
        if (toRemove != null)
            INSTANCES.remove(toRemove);
    }

    private Hook getInstance(Player player) {
        for (Hook hook : INSTANCES)
            if (hook.getPlayer() == player)
                return hook;
        return null;
    }

    public void onDisable() {
        for (Hook hook : INSTANCES)
            hook.stop();
        INSTANCES.clear();
        cancel();
    }

}