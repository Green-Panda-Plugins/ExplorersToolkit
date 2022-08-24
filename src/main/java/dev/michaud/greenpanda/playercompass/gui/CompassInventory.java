package dev.michaud.greenpanda.playercompass.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/* This holder is only used to query if a player is interacting with a custom GUI.
 * Everything here can be left blank but since getInventory is marked as NotNull we throw an
 * exception to be null safe. */
public class CompassInventory implements InventoryHolder {

  @Override
  public @NotNull Inventory getInventory() {
    throw new RuntimeException(
        "Calling getInventory() on an empty holder! This holder doesn't have any inventory associated with it");
  }
}