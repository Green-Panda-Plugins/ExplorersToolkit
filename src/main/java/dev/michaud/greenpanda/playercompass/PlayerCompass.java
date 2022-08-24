package dev.michaud.greenpanda.playercompass;

import dev.michaud.greenpanda.playercompass.events.CompassGuiInteract;
import dev.michaud.greenpanda.playercompass.events.CompassRightClick;
import dev.michaud.greenpanda.playercompass.items.Compass;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerCompass extends JavaPlugin {

  private static PlayerCompass plugin;

  public static PlayerCompass getPlugin() {
    return plugin;
  }

  @Override
  public void onEnable() {
    plugin = this;

    //Events
    PluginManager pluginManager = getServer().getPluginManager();
    pluginManager.registerEvents(new CompassGuiInteract(), plugin);
    pluginManager.registerEvents(new CompassRightClick(), plugin);

    //Init
    Compass.init();

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
