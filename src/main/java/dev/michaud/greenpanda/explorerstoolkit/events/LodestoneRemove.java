package dev.michaud.greenpanda.explorerstoolkit.events;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import dev.michaud.greenpanda.core.event.PlayerGetItemEvent;
import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.explorerstoolkit.items.PlayerCompass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LodestoneRemove implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onLodestoneBreak(@NotNull BlockBreakEvent event) {

    final Block block = event.getBlock();
    final Location location = block.getLocation();

    if (block.getType() == Material.LODESTONE) {
      removeLodestoneLoreForAll(location);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onLodestoneDestroyed(@NotNull BlockDestroyEvent event) {

    final Block block = event.getBlock();
    final Location location = block.getLocation();

    if (block.getType() == Material.LODESTONE) {
      removeLodestoneLoreForAll(location);
    }

  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onChunkLoad(@NotNull ChunkLoadEvent event) {

    if (event.isNewChunk()) {
      return; //New chunks won't have lodestone compasses
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      for (ItemStack item : player.getInventory().getContents()) {
        checkLodestoneLore(item);
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerGetCompass(@NotNull PlayerGetItemEvent event) {

    final ItemStack item = event.getItem();

    if (item == null || item.getType() != Material.COMPASS) {
      return;
    }

    checkLodestoneLore(item);
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {

    Player player = event.getPlayer();

    for (ItemStack item : player.getInventory().getContents()) {
      checkLodestoneLore(item);
    }
  }

  private static void removeLodestoneLoreForAll(Location blockLocation) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      for (ItemStack item : player.getInventory().getContents()) {
        removeLodestoneLore(item, blockLocation);
      }
    }
  }

  private static void removeLodestoneLore(ItemStack item, @NotNull Location blockLocation) {
    if (item == null || item.getType() != Material.COMPASS) {
      return;
    }

    final CompassMeta meta = (CompassMeta) item.getItemMeta();
    final Location compassLocation = meta.getLodestone();

    if (meta.isLodestoneTracked() && meta.hasLodestone()
        && locationEquals(blockLocation, compassLocation)
        && !ItemRegistry.isCustomItem(PlayerCompass.class, item)) {
      meta.lore(null);
      item.setItemMeta(meta);
    }
  }

  private static void checkLodestoneLore(ItemStack item) {
    if (item == null || item.getType() != Material.COMPASS) {
      return;
    }

    final CompassMeta meta = (CompassMeta) item.getItemMeta();
    final Location compassLocation = meta.getLodestone();

    if (meta.isLodestoneTracked() && meta.hasLodestone()
        && isLodestoneMissing(compassLocation)
        && !ItemRegistry.isCustomItem(PlayerCompass.class, item)) {
      meta.lore(null);
      item.setItemMeta(meta);
    }
  }

  @Contract("null, _ -> false; _, null -> false")
  private static boolean locationEquals(Location location1, Location location2) {

    if (location1 == null || location2 == null) {
      return false;
    }

    if (!location1.isWorldLoaded() || !location2.isWorldLoaded()) {
      return false;
    }

    final World world1 = location1.getWorld();
    final World world2 = location2.getWorld();

    if (world1 == null || !world1.equals(world2)) {
      return false;
    }

    int x1 = location1.getBlockX();
    int y1 = location1.getBlockY();
    int z1 = location1.getBlockZ();

    int x2 = location2.getBlockX();
    int y2 = location2.getBlockY();
    int z2 = location2.getBlockZ();

    return x1 == x2 && y1 == y2 && z1 == z2;
  }

  @Contract("null -> false")
  private static boolean isLodestoneMissing(Location location) {

    if (location == null) {
      return false;
    }

    if (!location.isWorldLoaded() || !location.isChunkLoaded()) {
      return false;
    }

    return location.getBlock().getType() != Material.LODESTONE;
  }
}