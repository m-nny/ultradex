package com.minmax.ultradex.reference

import com.minmax.ultradex.UltraDex
import com.pixelmonmod.pixelmon.Pixelmon
import net.minecraft.util.ResourceLocation

object Resources {
    object Gui {
        object Jei {
            val TABS = ResourceLocation(Pixelmon.MODID, "textures/items/pokeballs/poke_ball.png")
            val POKEMON_EVOLUTION = ResourceLocation(UltraDex.MOD_ID, "textures/gui/evolution.png")
            val POKEMON_SPAWNING = ResourceLocation(UltraDex.MOD_ID, "textures/gui/pokemon_spawn.png")
        }
    }
}