package dev.michaud.greenpanda.explorerstoolkit.util;


import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Format {

  /**
   * Formats distance into a string (E.g. 700m or 5.1km)
   *
   * @param distance the distance in meters
   * @return Formatted string with the distance and units
   */
  @Contract(pure = true)
  public static @NotNull String formatDistance(double distance) {

    if (distance > 999000) {
      return "999+ km";
    }

    if (distance >= 1000) {
      return String.format("%.1fkm", distance / 1000);
    }

    return (int) distance + "m";
  }

  /**
   * Formats the distance between two locations as a string (E.g. 700m or 5.1km)
   * @param location1 Location A
   * @param location2 Location B
   * @return Formatted string with the distance and units
   */
  public static @NotNull String formatDistance(@NotNull Location location1, @NotNull Location location2) {
    return formatDistance(location1.distance(location2));
  }


}