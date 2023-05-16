package com.minmax.ultradex.reference

import com.minmax.ultradex.UltraDex
import com.pixelmonmod.pixelmon.Pixelmon
import net.minecraft.util.ResourceLocation

object Resources {
    object Gui {
        object Jei {
            val TABS = ResourceLocation(Pixelmon.MODID, Textures.Pixelmon.POKEBALL)
            val POKEMON_DROP = ResourceLocation(UltraDex.MOD_ID, Textures.Gui.Jei.POKEMON_DROP)
            val POKEMON_EVOLUTION = ResourceLocation(UltraDex.MOD_ID, Textures.Gui.Jei.POKEMON_EVOLUTION)
            val POKEMON_SPAWNING = ResourceLocation(UltraDex.MOD_ID, Textures.Gui.Jei.POKEMON_SPAWNING)
        }
    }
}