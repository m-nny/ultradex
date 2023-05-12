package com.minmax.ultradex.config;

import com.minmax.ultradex.jei.PixelmonJEIPlugin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

public final class Settings {
    public static int ITEMS_PER_ROW = 4;
    public static int ITEMS_PER_COLUMN = 4;

    public static boolean gameLoaded = false;

    public static void reload() {
        if (gameLoaded) {
            PixelmonJEIPlugin.resetCategories();
        }
    }
}
