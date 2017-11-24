package me.finnbon.maneuvergear.command;

import me.finnbon.maneuvergear.ManeuverGear;
import me.finnbon.maneuvergear.crafting.CraftingManager;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class GiveItemCommand implements CommandExecutor, TabCompleter {

    private final ManeuverGear plugin;

    public GiveItemCommand(ManeuverGear plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getServer().getPluginCommand("3dmg");
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(RED + "You must be a player to use that command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("3dmg.give")) {
            player.sendMessage(RED + "You do not have permission to use this command!");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(RED + "Usage: /3dmg <gear|rope> [amount] [player]");
        } else if (args[0].equalsIgnoreCase("gear")) {

            int amount = args.length > 1 && args[1].matches("^\\d+$") ? Integer.parseInt(args[1]) : 1;

            Player target;
            boolean givenByOther = false;
            if (args.length > 2) {
                if (!player.hasPermission("3dmg.give.others")) {
                    player.sendMessage(RED + "You do not have permission to give to other players!");
                    return true;
                }
                target = plugin.getServer().getPlayer(args[2]);
                if (target == null) {
                    player.sendMessage(RED + "That player is not online!");
                    return false;
                } else {
                    player.sendMessage(GREEN + "You've given " + amount + " 3DMG" + (amount > 1 ? "s" : "") + " to " + target.getName());
                    givenByOther = true;
                }
            } else {
                target = player;
            }

            target.sendMessage(GREEN + "You were given " + amount + " 3DMG" + (amount > 1 ? "s" : "") + (givenByOther ? " by " + player.getPlayer().getName() : ""));
            for (int i = 0; i < amount; i++)
                target.getInventory().addItem(CraftingManager.HOOK);

            if (target.getInventory().getItemInMainHand().isSimilar(CraftingManager.HOOK))
                plugin.getHookManager().start(player);

        } else if (args[0].equalsIgnoreCase("rope")) {

            int amount = args.length > 1 && args[1].matches("^\\d+$") ? Integer.parseInt(args[1]) : 1;

            Player target;
            boolean givenByOther = false;
            if (args.length > 2) {
                if (!player.hasPermission("3dmg.give.others")) {
                    player.sendMessage(RED + "You do not have permission to give to other players!");
                    return true;
                }
                target = plugin.getServer().getPlayer(args[2]);
                if (target == null) {
                    player.sendMessage(RED + "That player is not online!");
                    return false;
                } else {
                    player.sendMessage(GREEN + "You've given " + amount + " rope" + (amount > 1 ? "s" : "") + " to " + target.getName());
                    givenByOther = true;
                }
            } else {
                target = player;
            }

            target.sendMessage(GREEN + "You were given " + amount + " rope" + (amount > 1 ? "s" : "") + (givenByOther ? " by " + player.getPlayer().getName() : ""));
            for (int i = 0; i < amount; i++)
                target.getInventory().addItem(CraftingManager.ROPE);

        } else {
            player.sendMessage(RED + "Usage: /3dmg <gear|rope> [amount]");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (args.length == 1)
            return Arrays.asList("gear", "rope");
        if (args.length == 3)
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        return Collections.emptyList();
    }
}