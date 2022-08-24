package dev.michaud.greenpanda.playercompass.events;

import dev.michaud.greenpanda.playercompass.items.Compass;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

  @EventHandler
  public static void onPlayerJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();
    NamespacedKey recipe = Compass.customItem.namespacedKey();

    if (!player.hasDiscoveredRecipe(recipe)) {
      player.discoverRecipe(recipe);
    }

  }
}