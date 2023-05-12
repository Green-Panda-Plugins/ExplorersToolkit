package dev.michaud.greenpanda.explorerstoolkit.events;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.explorerstoolkit.gui.PlayerCompassInventory;
import dev.michaud.greenpanda.explorerstoolkit.items.PlayerCompass;
import dev.michaud.greenpanda.explorerstoolkit.util.TranslationTexts;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerCompassGuiInteract implements Listener {

  @EventHandler
  public static void onGuiInteract(InventoryClickEvent event) {

    final PlayerCompass compassInstance = ItemRegistry.getInstance(PlayerCompass.class);
    final ItemStack eventItem = event.getCurrentItem();
    final Player player = (Player) event.getWhoClicked();
    final ItemStack compassItem = player.getInventory().getItemInMainHand();

    if (!(event.getInventory().getHolder() instanceof PlayerCompassInventory)) {
      return; //Not the gui we are looking for
    }

    event.setCancelled(true);

    if (eventItem == null || !event.getInventory().contains(eventItem)) {
      return; //Not the head we're looking for
    }

    if (!(eventItem.getItemMeta() instanceof SkullMeta meta)) {
      return; //Didn't click on player head
    }

    if (!compassInstance.isType(compassItem)) {
      return; //Not holding compass
    }

    final OfflinePlayer target = meta.getOwningPlayer();

    if (target != null && target.isOnline()) {

      Player targetPlayer = (Player) target;
      PlayerCompass.setLocation(compassItem, targetPlayer.getLocation());
      player.sendActionBar(TranslationTexts.NOW_TRACKING.args(targetPlayer.displayName()));

    } else {

      PlayerCompass.clearLocation(compassItem);
      player.sendActionBar(TranslationTexts.PLAYER_NOT_FOUND);

    }

    event.getInventory().close();

  }
}