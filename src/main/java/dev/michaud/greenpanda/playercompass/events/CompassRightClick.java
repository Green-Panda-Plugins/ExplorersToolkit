package dev.michaud.greenpanda.playercompass.events;

import dev.michaud.greenpanda.playercompass.gui.CompassInventory;
import dev.michaud.greenpanda.playercompass.items.Compass;
import dev.michaud.greenpanda.playercompass.util.Format;
import dev.michaud.greenpanda.playercompass.util.PlayerDistance;
import dev.michaud.greenpanda.playercompass.util.SortByDistance;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CompassRightClick implements Listener {

  //How many slots to show in the compass gui
  final static int guiSlotsToShow = 9;

  @EventHandler
  public static void onRightClick(PlayerInteractEvent event) {

    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK
        && event.getAction() != Action.LEFT_CLICK_AIR) {
      return;
    }

    ItemStack eventItem = event.getItem();

    if (eventItem == null || eventItem.getType() != Material.COMPASS) {
      return;
    }

    if (Compass.customItem.isType(eventItem)) {
      playerCompassRightClick(event.getPlayer(), eventItem);
    } else {
      compassRightClick(event);
    }

  }

  private static void playerCompassRightClick(Player player, ItemStack item) {

    boolean success = openCompassGUI(player);

    if (!success) {
      Compass.customItem.clearLocation(item);
      player.sendActionBar(Component.text("There are currently no players to track"));
    }
  }

  private static void compassRightClick(PlayerInteractEvent event) {

    Block clickedBlock = event.getClickedBlock();
    ItemStack compass = event.getItem();

    if (clickedBlock == null || compass == null
        || clickedBlock.getType() != Material.LODESTONE) {
      return;
    }

    CompassMeta meta = (CompassMeta) compass.getItemMeta();
    Location location = clickedBlock.getLocation();

    List<Component> lore = new ArrayList<>();
    String coords = "XYZ: " + location.getBlockX() + " / " + location.getBlockY() + " / "
        + location.getBlockZ();

    lore.add(Component.text(coords)
        .color(NamedTextColor.GRAY)
        .decoration(TextDecoration.ITALIC, false));

    meta.lore(lore);
    compass.setItemMeta(meta);

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
      if (!otherPlayer.equals(player) && Compass.customItem.hasInInventory(otherPlayer)) {
        onlinePlayers.add(otherPlayer);
      }
    }

    Iterable<PlayerDistance> playerDistances = SortByDistance.getClosest(player, onlinePlayers,
        guiSlotsToShow);
    ItemStack[] playerHeads = getPlayerHeads(playerDistances);

    //No other players found
    if (playerHeads.length == 0) {
      return false;
    }

    //Create GUI
    TextComponent title = Component.text("Track Player");
    Inventory gui = Bukkit.createInventory(new CompassInventory(), guiSlotsToShow, title);
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
      SkullMeta meta = (SkullMeta) skull.getItemMeta();
      meta.setOwningPlayer(p);
      String dist = Format.formatDistance(playerDistance.getDistance());
      meta.displayName(Component.text(p.getName()).append(Component.text(dist)));
      skull.setItemMeta(meta);

      headsList.add(skull);
    }

    ItemStack[] headsArray = new ItemStack[headsList.size()];
    headsArray = headsList.toArray(headsArray);

    return headsArray;
  }

}