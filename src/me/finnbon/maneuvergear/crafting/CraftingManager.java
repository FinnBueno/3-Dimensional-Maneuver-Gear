package me.finnbon.maneuvergear.crafting;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.finnbon.maneuvergear.util.ItemBuilder;

/**
 * This class manages the crafting of the 3DMG
 */
public class CraftingManager implements Listener {
	
	public static final ItemStack HOOK = new ItemBuilder(Material.IRON_BARDING).setName("&l&73D Maneuver Gear")
			.setLore("&8Used for extreme mobility").build();
	public static final ItemStack ROPE = new ItemBuilder(Material.STRING).setName("&l&8Rope")
			.setLore("&8Used to make &73D Maneuver Gear").build();
	
	public CraftingManager(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		// create rope recipe
		ShapedRecipe ropeRecipe = new ShapedRecipe(ROPE);
		ropeRecipe.shape("SSS", "III", "SSS");
		ropeRecipe.setIngredient('S', Material.STRING);
		ropeRecipe.setIngredient('I', Material.IRON_INGOT);
		plugin.getServer().addRecipe(ropeRecipe);
		
		// create 3DMG recipe
		ShapedRecipe hookRecipe = new ShapedRecipe(HOOK);
		hookRecipe.shape("III", "IPR", "III");
		hookRecipe.setIngredient('I', Material.IRON_BLOCK);
		hookRecipe.setIngredient('P', Material.PISTON_BASE);
		hookRecipe.setIngredient('R', Material.STRING);
		plugin.getServer().addRecipe(hookRecipe);
	}
	
	@EventHandler
	public void craft(PrepareItemCraftEvent event) {
		if (event.getRecipe() == null)
			return;
		if (!isCorrectResult(event.getRecipe().getResult()))
			return;
		// we're dealing with an attempt to craft the 3DMG.
		ItemStack[] items = event.getInventory().getMatrix();
		if (!items[5].isSimilar(ROPE))
			event.getInventory().setResult(null);
	}

    /**
     * Whether the passed {@link ItemStack} is a 3DMG item or not
     * @param item The {@link ItemStack} to check
     * @return  Whether the {@link ItemStack} is a valid 3DMG or not
     */
	private boolean isCorrectResult(ItemStack item) {
		return item.isSimilar(HOOK);
	}
	
}