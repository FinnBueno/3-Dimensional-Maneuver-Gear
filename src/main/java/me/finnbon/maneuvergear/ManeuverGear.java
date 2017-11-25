package me.finnbon.maneuvergear;

import me.finnbon.maneuvergear.command.GiveItemCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.finnbon.maneuvergear.crafting.CraftingManager;
import me.finnbon.maneuvergear.gear.HookManager;
import me.finnbon.maneuvergear.listener.ActivationListener;

/**
 * The main plugin class
 */
public class ManeuverGear extends JavaPlugin {

    private HookManager hookManager;

    @Override
    public void onEnable() {
        // set config options
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(false);
        getConfig().addDefault("Range", 100);
        getConfig().addDefault("Damage", 2.0);
        saveConfig();

        hookManager = new HookManager(this);
        new CraftingManager(this);
        new ActivationListener(this);
        new GiveItemCommand(this);
        // start for online players, in case the server got reloaded
        for (Player player : getServer().getOnlinePlayers()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (CraftingManager.HOOK.isSimilar(item))
                hookManager.start(player);
        }
    }

    @Override
    public void onDisable() {
        hookManager.onDisable();
    }

    /**
     * @return The only allowed instance of the HookManager class
     */
    public HookManager getHookManager() {
        return hookManager;
    }

}