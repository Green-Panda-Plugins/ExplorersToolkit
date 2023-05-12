package dev.michaud.greenpanda.explorerstoolkit.events;

import dev.michaud.greenpanda.core.util.MaterialInfo;
import dev.michaud.greenpanda.explorerstoolkit.util.TranslationTexts;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

public class RightClickMap implements Listener {

  @EventHandler
  public void onRightClick(@NotNull PlayerInteractEvent event) {

    if (!event.getAction().isRightClick()) {
      return;
    }

    final ItemStack item = event.getItem();
    final Player player = event.getPlayer();
    final Block clickedBlock = event.getClickedBlock();

    if (clickedBlock != null && MaterialInfo.isInteractable(clickedBlock.getBlockData())) {
      return;
    }

    if (item != null && item.getType() == Material.FILLED_MAP) {
      showCoordinatesToPlayer(player, item);
    }

  }

  private static void showCoordinatesToPlayer(@NotNull Player player, @NotNull ItemStack mapItem) {

    final Location playerLocation = player.getLocation();
    final MapMeta mapItemMeta = (MapMeta) mapItem.getItemMeta();
    final MapView mapView = mapItemMeta.getMapView();

    if (mapView == null) {
      return;
    }

    if (!isWithinMap(playerLocation, mapView)) {
      return;
    }

    final Component message = TranslationTexts.MAP_ACTIONBAR.args(
        Component.text(String.format("%.0f", playerLocation.getX())),
        Component.text(String.format("%.0f", playerLocation.getY())),
        Component.text(String.format("%.0f", playerLocation.getZ())));

    player.sendActionBar(message);
  }

  private static boolean isWithinMap(@NotNull Location location, @NotNull MapView map) {

    final World world = location.getWorld();
    final World mapWorld = map.getWorld();

    if (world != mapWorld) {
      return false;
    }

    final double locationX = location.getX();
    final double locationZ = location.getZ();

    final int mapCenterX = map.getCenterX();
    final int mapCenterZ = map.getCenterZ();

    final int mapSize = switch (map.getScale()) {
      case CLOSEST -> 128 / 2;
      case CLOSE -> 256 / 2;
      case NORMAL -> 512 / 2;
      case FAR -> 1024 / 2;
      case FARTHEST -> 2048 / 2;
    };

    return !(locationX > (mapCenterX + mapSize))
        && !(locationX < (mapCenterX - mapSize))
        && !(locationZ > (mapCenterZ + mapSize))
        && !(locationZ < (mapCenterZ - mapSize));

  }

}