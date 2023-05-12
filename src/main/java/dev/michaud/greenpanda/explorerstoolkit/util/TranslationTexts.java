package dev.michaud.greenpanda.explorerstoolkit.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;

public class TranslationTexts {

  /* MAP */
  public static final TranslatableComponent MAP_ACTIONBAR = Component.translatable(
      "greenpanda.explorerstoolkit.mapActionbar")
      .fallback("%s / %s / %s");

  /* LODESTONE COMPASS */
  public static final TranslatableComponent LODESTONE_COMPASS_LORE = Component.translatable(
      "greenpanda.explorerstoolkit.lodestoneCompassLore")
      .fallback("%s / %s / %s");

  public static final TranslatableComponent LODESTONE_COMPASS_ACTIONBAR = Component.translatable(
          "greenpanda.explorerstoolkit.lodestoneCompassActionbar")
      .fallback("%s / %s / %s (%s)");

  /* PLAYER COMPASS */
  public static final TranslatableComponent PLAYER_NOT_FOUND = Component.translatable(
          "greenpanda.playerCompass.actionBar.playerNotFound")
      .fallback("That player couldn't be found!");

  public static final TranslatableComponent NOW_TRACKING = Component.translatable(
          "greenpanda.playerCompass.actionBar.trackingPlayer")
      .fallback("Now tracking %s");

  public static final TranslatableComponent NO_TRACKABLE_PLAYERS = Component.translatable(
          "greenpanda.playerCompass.actionBar.noTrackablePlayers")
      .fallback("There are currently no players to track");

  public static final TranslatableComponent COMPASS_GUI_MENU_TITLE = Component.translatable(
          "greenpanda.playerCompass.gui.title")
      .fallback("Track Players");

}