package me.finnbon.maneuvergear.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private Material mat;
	private short durability;
	private String name;
	private List<String> lore;
	
	public ItemBuilder(Material mat) {
		this.mat = mat;
	}

	public Material getMat() {
		return mat;
	}

	public ItemBuilder setMat(Material mat) {
		this.mat = mat;
		return this;
	}

	public short getDurability() {
		return durability;
	}

	public ItemBuilder setDurability(short durability) {
		this.durability = durability;
		return this;
	}

	public String getName() {
		return name;
	}

	public ItemBuilder setName(String name) {
		this.name = colour(name);
		return this;
	}

	public List<String> getLore() {
		return lore;
	}

	public ItemBuilder setLore(String... lore) {
		this.lore = new ArrayList<>();
		for (String l : lore)
			this.lore.add(colour(l));
		return this;
	}
	
	public ItemBuilder addLineToLore(String... lore) {
		for (String l : lore)
			this.lore.add(colour(l));
		return this;
	}
	
	public ItemBuilder removeLineFromLore(int index) {
		lore.remove(index);
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(mat);
		item.setDurability(durability);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private String colour(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
}