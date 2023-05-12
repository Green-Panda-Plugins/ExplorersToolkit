package dev.michaud.greenpanda.explorerstoolkit.events;

import dev.michaud.greenpanda.core.event.PlayerGetItemEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerUnlockLodestoneRecipe implements Listener {

  @EventHandler
  public void onPlayerGetItem(@NotNull PlayerGetItemEvent event) {

    ItemStack item = event.getItem();
    Player player = event.getPlayer();

    if (item.getType() == Material.CHISELED_POLISHED_BLACKSTONE
        || item.getType() == Material.COMPASS) {
      player.discoverRecipe(NamespacedKey.minecraft("lodestone"));
    }

  }

}