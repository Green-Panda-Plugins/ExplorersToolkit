package dev.michaud.greenpanda.explorerstoolkit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SortByDistance {

  /**
   * Sorts the closest players in order of distance.
   *
   * @param source The player to calculate the distance from
   * @param others Other players to sort by distance
   * @param max    The maximum amount of players in the final list
   * @return returns an Iterable containing the given players sorted by distance and their distance
   * from the player.
   * @see PlayerDistance
   */
  public static @NotNull Iterable<PlayerDistance> getClosest(final Player source,
      final @NotNull Iterable<Player> others, int max) {

    final List<PlayerDistance> distances = new ArrayList<>();

    for (final Player player : others) {
      double distance = source.getLocation().distanceSquared(player.getLocation());
      var playerDistance = new PlayerDistance(player, distance);
      distances.add(playerDistance);
    }

    Collections.sort(distances);

    return distances.subList(0, Math.min(max, distances.size()));
  }
}