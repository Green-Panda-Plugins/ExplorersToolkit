package dev.michaud.greenpanda.explorerstoolkit.events;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.core.util.MaterialInfo;
import dev.michaud.greenpanda.explorerstoolkit.gui.PlayerCompassInventory;
import dev.michaud.greenpanda.explorerstoolkit.items.PlayerCompass;
import dev.michaud.greenpanda.explorerstoolkit.util.Format;
import dev.michaud.greenpanda.explorerstoolkit.util.PlayerDistance;
import dev.michaud.greenpanda.explorerstoolkit.util.SortByDistance;
import dev.michaud.greenpanda.explorerstoolkit.util.TranslationTexts;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RightClickCompass implements Listener {

  static final int GUI_SLOTS_TO_SHOW = 27;

  @EventHandler
  public static void onRightClick(@NotNull PlayerInteractEvent event) {

    if (event.getAction().isRightClick()) {
      return;
    }

    final ItemStack eventItem = event.getItem();

    if (ItemRegistry.isCustomItem(PlayerCompass.class, eventItem)) {
      playerCompassRightClick(event.getPlayer(), eventItem);
    } else {
      normalCompassRightClick(event);
    }
  }

  private static void playerCompassRightClick(Player player, ItemStack item) {

    if (!openCompassGUI(player)) {
      PlayerCompass.clearLocation(item);
      player.sendActionBar(TranslationTexts.NO_TRACKABLE_PLAYERS);
    }

  }

  private static void normalCompassRightClick(@NotNull PlayerInteractEvent event) {

    Block block = event.getClickedBlock();
    ItemStack item = event.getItem();
    Player player = event.getPlayer();

    if (item == null || item.getType() != Material.COMPASS) {
      return;
    }

    if (block != null && block.getType() == Material.LODESTONE) {
      setLodestoneCompassLore(block, item);
    } else if (!isInteractable(block) || player.isSneaking()) {
      showCoordinates(player, item);
    }

  }

  private static void setLodestoneCompassLore(@NotNull Block clickedBlock, @NotNull ItemStack compass) {

    if (clickedBlock.getType() != Material.LODESTONE) {
      throw new IllegalArgumentException("Clicked block isn't a lodestone");
    }

    if (compass.getType() != Material.COMPASS) {
      throw new IllegalArgumentException("Item isn't a compass");
    }

    final CompassMeta meta = (CompassMeta) compass.getItemMeta();
    final Location location = clickedBlock.getLocation();

    final List<Component> lore = new ArrayList<>();
    lore.add(TranslationTexts.LODESTONE_COMPASS_LORE.args(
        Component.text(location.getBlockX()),
        Component.text(location.getBlockY()),
        Component.text(location.getBlockZ()))
        .color(NamedTextColor.GRAY)
        .decoration(TextDecoration.ITALIC, true));

    meta.lore(lore);
    compass.setItemMeta(meta);
  }

  private static void showCoordinates(@NotNull Player player, @NotNull ItemStack compass) {

    if (!player.isValid()) {
      throw new IllegalArgumentException("Player is invalid: " + player);
    }

    if (compass.getType() != Material.COMPASS) {
      throw new IllegalArgumentException("Item isn't a compass: " + compass);
    }

    final CompassMeta meta = (CompassMeta) compass.getItemMeta();
    final Location location = meta.getLodestone();

    if (location == null || location.getWorld() != player.getWorld()) {
      return;
    }

    final String distanceStr = Format.formatDistance(location, player.getLocation());

    final Component message = TranslationTexts.LODESTONE_COMPASS_ACTIONBAR.args(
        Component.text(String.format("%.0f", location.getX())),
        Component.text(String.format("%.0f", location.getY())),
        Component.text(String.format("%.0f", location.getZ())),
        Component.text(distanceStr));

    player.sendActionBar(message);
  }

  /**
   * Displays the compass GUI to the player.
   *
   * @param player the player to open the GUI for
   * @return returns true if the GUI was successfully opened
   */
  private static boolean openCompassGUI(Player player) {

    List<Player> onlinePlayers = new ArrayList<>();

    for (Player otherPlayer : player.getWorld().getPlayers()) {
      if (!otherPlayer.equals(player) && PlayerCompass.hasInInventory(otherPlayer)) {
        onlinePlayers.add(otherPlayer);
      }
    }

    Iterable<PlayerDistance> playerDistances = SortByDistance.getClosest(player, onlinePlayers,
        GUI_SLOTS_TO_SHOW);
    ItemStack[] playerHeads = getPlayerHeads(playerDistances);

    if (playerHeads.length == 0) {
      return false; //No other players found
    }

    //Create GUI
    Component title = TranslationTexts.COMPASS_GUI_MENU_TITLE;
    Inventory gui = Bukkit.createInventory(new PlayerCompassInventory(), GUI_SLOTS_TO_SHOW, title);
    gui.setContents(playerHeads);

    player.openInventory(gui);

    return true;
  }

  /**
   * Creates a list of player heads belonging to the given players
   *
   * @param players players to get heads for
   * @return returns a list of player heads.
   */
  private static ItemStack[] getPlayerHeads(Iterable<PlayerDistance> players) {

    List<ItemStack> headsList = new ArrayList<>();

    for (PlayerDistance playerDistance : players) {

      Player p = playerDistance.getPlayer();

      ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

      //Set info
      final String dist = " (" + Format.formatDistance(playerDistance.getDistance()) + ")";
      final SkullMeta meta = (SkullMeta) skull.getItemMeta();

      meta.setOwningPlayer(p);
      meta.displayName(Component.text(p.getName()).append(Component.text(dist)));
      skull.setItemMeta(meta);

      headsList.add(skull);
    }

    ItemStack[] headsArray = new ItemStack[headsList.size()];
    headsArray = headsList.toArray(headsArray);

    return headsArray;
  }

  private static boolean isInteractable(@Nullable Block block) {
    if (block == null) {
      return false;
    }

    return MaterialInfo.isInteractable(block.getBlockData());
  }

}