package com.minmax.ultradex.util

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries

object BiomeUtils {
    fun Biome.getDisplayName(): ITextComponent {
        val rl = ForgeRegistries.BIOMES.getKey(this) ?: return StringTextComponent(this.toString())
        return TranslationTextComponent("biome." + rl.namespace + '.' + rl.path.replace('/', '.'))
    }
}