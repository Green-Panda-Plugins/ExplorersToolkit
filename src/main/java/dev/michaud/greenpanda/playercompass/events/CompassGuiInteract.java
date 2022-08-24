package dev.michaud.greenpanda.playercompass.events;

import dev.michaud.greenpanda.playercompass.gui.CompassInventory;
import dev.michaud.greenpanda.playercompass.items.Compass;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CompassGuiInteract implements Listener {

  @EventHandler
  public static void onGuiInteract(InventoryClickEvent event) {

    //Not the gui we are looking for
    if (!(event.getInventory().getHolder() instanceof CompassInventory)) {
      return;
    }

    event.setCancelled(true);

    ItemStack eventItem = event.getCurrentItem();

    //Not the head we're looking for
    if (eventItem == null || !event.getInventory().contains(eventItem)) {
      return;
    }

    //Didn't click on player head
    if (!(eventItem.getItemMeta() instanceof SkullMeta meta)) {
      return;
    }

    Player player = (Player) event.getWhoClicked();
    ItemStack compassItem = player.getInventory().getItemInMainHand();

    //Not holding compass
    if (!Compass.customItem.isType(compassItem)) {
      return;
    }

    Player targetPlayer = (Player) meta.getOwningPlayer();

    if (targetPlayer != null && targetPlayer.isOnline()) {

      Compass.customItem.setLocation(compassItem, targetPlayer.getLocation());
      player.sendActionBar(Component.text("Now tracking " + targetPlayer.getName()));

    } else {

      Compass.customItem.clearLocation(compassItem);
      player.sendActionBar(Component.text("That player couldn't be found"));

    }

    event.getInventory().close();

  }
}
