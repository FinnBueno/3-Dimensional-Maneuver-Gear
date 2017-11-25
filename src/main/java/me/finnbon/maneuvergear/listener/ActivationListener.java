package me.finnbon.maneuvergear.listener;

import me.finnbon.maneuvergear.ManeuverGear;
import me.finnbon.maneuvergear.crafting.CraftingManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

/**
 * The listener class to (de)activate the 3DMG
 */
public class ActivationListener implements Listener {

    private ManeuverGear plugin;

    public ActivationListener(ManeuverGear plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Listener for inventory click
     * @param event The event that was fired
     */
    @EventHandler
    public void itemClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (CraftingManager.HOOK.isSimilar(item))
            plugin.getHookManager().start((Player) event.getWhoClicked());
    }

    /**
     * Listener for item dropping
     * @param event The event that was fired
     */
    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (CraftingManager.HOOK.isSimilar(item))
            plugin.getHookManager().start(event.getPlayer());
    }

    /**
     * Listener for item pickup
     * @param event The event that was fired
     */
    @EventHandler
    public void pickup(EntityPickupItemEvent event) {
        Entity ent = event.getEntity();
        if (!(ent instanceof Player))
            return;

        Player player = (Player) ent;

        if (CraftingManager.HOOK.isSimilar(player.getInventory().getItemInMainHand()))
            plugin.getHookManager().start(player);
    }

    /**
     * Listener for slot switching
     * @param event The event that was fired
     */
    @EventHandler
    public void slotSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        int newSlotInt = event.getNewSlot();
        int oldSlotInt = event.getPreviousSlot();
        ItemStack newSlot = player.getInventory().getItem(newSlotInt);
        ItemStack oldSlot = player.getInventory().getItem(oldSlotInt);

        if (CraftingManager.HOOK.isSimilar(newSlot) && !CraftingManager.HOOK.isSimilar(oldSlot)) {
            plugin.getHookManager().start(player);
        } else if (!CraftingManager.HOOK.isSimilar(newSlot) && CraftingManager.HOOK.isSimilar(oldSlot)) {
            plugin.getHookManager().stop(player);
        }
    }

    /**
     * Listener for hand switching
     * @param event The event that was fired
     */
    @EventHandler
    public void switchHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack main = event.getMainHandItem();
        if (CraftingManager.HOOK.isSimilar(main))
            plugin.getHookManager().start(player);
    }

    /**
     * Listener for players joining the server
     * @param event The event that was fired
     */
    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().isSimilar(CraftingManager.HOOK))
            plugin.getHookManager().start(player);
    }

    /**
     * Listener for players leaving the server
     * @param event The event that was fired
     */
    @EventHandler
    public void quit(PlayerQuitEvent event) {
        plugin.getHookManager().stop(event.getPlayer());
    }

}