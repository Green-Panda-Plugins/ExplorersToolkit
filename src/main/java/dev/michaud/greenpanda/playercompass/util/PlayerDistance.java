package dev.michaud.greenpanda.playercompass.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public record PlayerDistance(Player player, double distanceSquared) implements
    Comparable<PlayerDistance> {

  public Player getPlayer() {
    return player;
  }

  public double getDistanceSquared() {
    return distanceSquared;
  }

  public double getDistance() {
    return Math.sqrt(distanceSquared);
  }

  @Override
  public int compareTo(@NotNull PlayerDistance other) {
    return distanceSquared < other.distanceSquared ? -1 : 1;
  }
}
