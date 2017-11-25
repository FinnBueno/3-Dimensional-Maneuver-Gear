package me.finnbon.maneuvergear.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * A class used for generic {@link Block} methods
 */
public class BlockUtil {
	
	private static final List<Material> NON_SOLID = Arrays.asList(Material.AIR,
			Material.BROWN_MUSHROOM,
			Material.BANNER,
			Material.BREWING_STAND,
			Material.COCOA,
			Material.CROPS,
			Material.DEAD_BUSH,
			Material.DETECTOR_RAIL,
			Material.DIODE_BLOCK_OFF,
			Material.DIODE_BLOCK_ON,
			Material.DOUBLE_PLANT,
			Material.ENDER_PORTAL,
			Material.END_ROD,
			Material.FIRE,
			Material.LAVA,
			Material.LEVER,
			Material.LONG_GRASS,
			Material.MELON_STEM,
			Material.NETHER_WARTS,
			Material.PORTAL,
			Material.POWERED_RAIL,
			Material.PUMPKIN_STEM,
			Material.RAILS,
			Material.REDSTONE_TORCH_OFF,
			Material.REDSTONE_TORCH_ON,
			Material.REDSTONE_WIRE,
			Material.RED_MUSHROOM,
			Material.RED_ROSE,
			Material.SAPLING,
			Material.SNOW,
			Material.STATIONARY_LAVA,
			Material.STATIONARY_WATER,
			Material.STONE_BUTTON,
			Material.STONE_PLATE,
			Material.SUGAR_CANE_BLOCK,
			Material.TORCH,
			Material.TRIPWIRE,
			Material.TRIPWIRE_HOOK,
			Material.VINE,
			Material.WALL_SIGN,
			Material.WATER,
			Material.WATER_LILY,
			Material.WEB,
			Material.WOOD_BUTTON,
			Material.WOOD_PLATE,
			Material.YELLOW_FLOWER);
	
	public static boolean isSolid(Block block) {
		return !NON_SOLID.contains(block.getType());
	}
	
}