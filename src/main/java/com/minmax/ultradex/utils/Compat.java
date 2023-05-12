package com.minmax.ultradex.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import static com.minmax.ultradex.UltraDex.LOGGER;

public class Compat {
    public static World getWorld() {
        World world = Minecraft.getInstance().level;
        if (world == null) {
            LOGGER.error("Cannot get world instance");
        }
        return world;
    }
}
