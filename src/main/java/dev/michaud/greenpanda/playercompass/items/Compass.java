package dev.michaud.greenpanda.playercompass.items;

import dev.michaud.greenpanda.core.item.Craftable;
import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.playercompass.PlayerCompass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Compass implements Craftable {

  public static Compass customItem;
  public static ItemStack itemStack;

  public static void init() {
    customItem = new Compass();
    itemStack = customItem.makeItem();

    ItemRegistry.registerItem(customItem);
  }

  @Override
  public @NotNull JavaPlugin getOwnerPlugin() {
    return PlayerCompass.getPlugin();
  }

  @Override
  public @NotNull ItemStack makeItem() {

    ItemStack item = Craftable.super.makeItem();

    CompassMeta meta = (CompassMeta) item.getItemMeta();

    meta.setLodestoneTracked(true);
    meta.setLodestone(new Location(
        Bukkit.getServer().getWorlds().get(0),
        Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));

    item.setItemMeta(meta);

    return item;

  }

  @Override
  public int customModelData() {
    return 206031;
  }

  @Override
  public @NotNull String customId() {
    return "player_compass";
  }

  @Override
  public @NotNull Component displayName() {
    return Component.text("Player Compass")
        .decoration(TextDecoration.ITALIC, false);
  }

  @Override
  public @NotNull Material baseMaterial() {
    return Material.COMPASS;
  }

  @Override
  public @NotNull Recipe recipe() {
    ShapedRecipe recipe = new ShapedRecipe(namespacedKey(), itemStack);
    recipe.shape(
        "AAA",
        "ACA",
        "AAA");
    recipe.setIngredient('A', Material.AMETHYST_SHARD);
    recipe.setIngredient('C', Material.COMPASS);

    return recipe;
  }

  public boolean hasInInventory(@NotNull Player player) {

    for (ItemStack item : player.getInventory()) {
      if (isType(item)) {
        return true;
      }
    }

    return false;
  }

  public void setLocation(@NotNull ItemStack compass, Location location) {
    CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
    compassMeta.setLodestoneTracked(false);
    compassMeta.setLodestone(location);
    compass.setItemMeta(compassMeta);
  }

  public void clearLocation(@NotNull ItemStack compass) {

    //BS location
    Location loc = new Location(
        Bukkit.getServer().getWorlds().get(0),
        Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

    CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
    compassMeta.setLodestoneTracked(true);
    compassMeta.setLodestone(loc);
    compass.setItemMeta(compassMeta);
  }

}