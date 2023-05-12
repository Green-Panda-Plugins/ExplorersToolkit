package dev.michaud.greenpanda.explorerstoolkit.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

public class CheaperLodestone {

  public static @NotNull Recipe recipe() {

    NamespacedKey key = NamespacedKey.minecraft("lodestone");

    return new ShapedRecipe(key, new ItemStack(Material.LODESTONE))
        .shape("BBB",
            "BIB",
            "BBB")
        .setIngredient('B', Material.CHISELED_POLISHED_BLACKSTONE)
        .setIngredient('I', Material.COMPASS);

  }

}