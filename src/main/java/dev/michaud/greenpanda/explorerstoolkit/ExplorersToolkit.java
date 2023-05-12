package dev.michaud.greenpanda.explorerstoolkit;

import dev.michaud.greenpanda.core.item.ItemRegistry;
import dev.michaud.greenpanda.explorerstoolkit.events.LodestoneRemove;
import dev.michaud.greenpanda.explorerstoolkit.events.RightClickMap;
import dev.michaud.greenpanda.explorerstoolkit.events.PlayerCompassGuiInteract;
import dev.michaud.greenpanda.explorerstoolkit.events.RightClickCompass;
import dev.michaud.greenpanda.explorerstoolkit.events.PlayerUnlockLodestoneRecipe;
import dev.michaud.greenpanda.explorerstoolkit.items.PlayerCompass;
import dev.michaud.greenpanda.explorerstoolkit.recipe.CheaperLodestone;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExplorersToolkit extends JavaPlugin {

  private static ExplorersToolkit plugin;

  public static ExplorersToolkit getPlugin() {
    return plugin;
  }

  @Override
  public void onEnable() {
    plugin = this;

    //Events
    getServer().getPluginManager().registerEvents(new RightClickCompass(), this);
    getServer().getPluginManager().registerEvents(new RightClickMap(), this);
    getServer().getPluginManager().registerEvents(new PlayerCompassGuiInteract(), this);
    getServer().getPluginManager().registerEvents(new PlayerUnlockLodestoneRecipe(), this);
    getServer().getPluginManager().registerEvents(new LodestoneRemove(), this);

    //Init
    ItemRegistry.register(PlayerCompass.class);

    //Recipe
    Bukkit.removeRecipe(NamespacedKey.minecraft("lodestone"));
    Bukkit.addRecipe(CheaperLodestone.recipe());
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}