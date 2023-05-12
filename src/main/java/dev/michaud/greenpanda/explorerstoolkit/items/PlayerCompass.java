package dev.michaud.greenpanda.explorerstoolkit.items;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.core.item.RecipeUnlockable;
import dev.michaud.greenpanda.explorerstoolkit.ExplorersToolkit;
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

public class PlayerCompass implements RecipeUnlockable {

  @Override
  public @NotNull JavaPlugin getOwnerPlugin() {
    return ExplorersToolkit.getPlugin();
  }

  @Override
  public @NotNull ItemStack makeItem() {

    ItemStack item = RecipeUnlockable.super.makeItem();

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
    return Component.translatable("greenpanda.item.playerCompass")
        .decoration(TextDecoration.ITALIC, false);
  }

  @Override
  public @NotNull Material baseMaterial() {
    return Material.COMPASS;
  }

  @Override
  public @NotNull Material recipeRequirement() {
    return Material.COMPASS;
  }

  @Override
  public @NotNull Recipe recipe() {
    ShapedRecipe recipe = new ShapedRecipe(namespacedKey(), makeItem());
    recipe.shape(
        "AAA",
        "ACA",
        "AAA");
    recipe.setIngredient('A', Material.AMETHYST_SHARD);
    recipe.setIngredient('C', Material.COMPASS);

    return recipe;
  }

  public static boolean hasInInventory(@NotNull Player player) {
    for (ItemStack item : player.getInventory()) {
      if (ItemRegistry.isCustomItem(PlayerCompass.class, item)) {
        return true;
      }
    }

    return false;
  }

  public static void setLocation(@NotNull ItemStack compass, Location location) {
    CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
    compassMeta.setLodestoneTracked(false);
    compassMeta.setLodestone(location);
    compass.setItemMeta(compassMeta);
  }

  public static void clearLocation(@NotNull ItemStack compass) {
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