package dev.michaud.greenpanda.playercompass.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Format {

  /**
   * Formats the distance in meters. Ex. (700m) or (5.1km)
   *
   * @param distance the distance to format
   * @return returns a string with the distanceSquared and units
   */
  @Contract(pure = true)
  public static @NotNull String formatDistance(double distance) {
    if (distance >= 1000) {
      return " (" + String.format("%.1f", distance / 1000) + "km)";
    }

    return " (" + (int) distance + "m)";
  }


}
