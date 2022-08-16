package com.hinaplugin.changelocation.changelocation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class ChangeLocation extends JavaPlugin implements Listener {

    public static ChangeLocation plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ChangeLocation getPlugin(){ return plugin; }

    private final NamespacedKey namespacedKey = new NamespacedKey(this, "cl");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("getbow")) {
            ItemStack itemStack = new ItemStack(Material.BOW, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(namespacedKey, PersistentDataType.STRING, "clbow");
            itemMeta.setDisplayName("僕たち・・・私たち・・・いれかわってる！？");
            itemStack.setItemMeta(itemMeta);
            Player player = (Player) sender;
            Inventory inventory = player.getInventory();
            inventory.addItem(itemStack);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof Player){
            ProjectileSource source = ((Arrow) event.getDamager()).getShooter();
            Player player = (Player) source;
            Player player1 = (Player) event.getEntity();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
            if ("clbow".equalsIgnoreCase(container.get(namespacedKey, PersistentDataType.STRING))) {
                double x1 = player1.getLocation().getX();
                double y1 = player1.getLocation().getY();
                double z1 = player1.getLocation().getZ();
                double x2 = player.getLocation().getX();
                double y2 = player.getLocation().getY();
                double z2 = player.getLocation().getZ();

                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(new Location(player1.getWorld(), x1, y1, z1));
                        player1.teleport(new Location(player.getWorld(), x2, y2, z2));
                        cancel();
                    }
                };
                runnable.runTaskTimer(this, 0L, 20L);
            }
        }
    }
}
