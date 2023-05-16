package com.minmax.ultradex.jei.ingredients.biome

import mezz.jei.api.ingredients.IIngredientType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries


class BiomeIngredient(private val biome: Biome) : IIngredientType<Biome> {
    fun getBiome() = this.biome
    override fun getIngredientClass() = Biome::class.java

    fun getResourceLocation(): ResourceLocation? {
        return ForgeRegistries.BIOMES.getKey(this.getBiome())
    }

    fun getUniqueString() = getBiome().toString()

    fun getStringTextComponent(): ITextComponent {
        val rl = ForgeRegistries.BIOMES.getKey(this.biome) ?: return StringTextComponent(getUniqueString())
        return TranslationTextComponent("biome." + rl.namespace + '.' + rl.path.replace('/', '.'))
    }

    fun getRepresentativeItemStack(): ItemStack {
        return when (biome.biomeCategory) {
            Biome.Category.TAIGA -> ItemStack(Items.SPRUCE_SAPLING)
            Biome.Category.EXTREME_HILLS -> ItemStack(Items.OAK_SAPLING)
            Biome.Category.JUNGLE -> ItemStack(Items.JUNGLE_SAPLING)
            Biome.Category.MESA -> ItemStack(Items.OAK_SAPLING)
            Biome.Category.PLAINS -> ItemStack(Items.OAK_SAPLING)
            Biome.Category.SAVANNA -> ItemStack(Items.ACACIA_LEAVES)
            Biome.Category.ICY -> ItemStack(Items.ICE)
            Biome.Category.THEEND -> ItemStack(Items.END_STONE)
            Biome.Category.BEACH -> ItemStack(Items.SAND)
            Biome.Category.FOREST -> ItemStack(Items.OAK_SAPLING)
            Biome.Category.OCEAN -> ItemStack(Items.TUBE_CORAL)
            Biome.Category.DESERT -> ItemStack(Items.CACTUS)
            Biome.Category.RIVER -> ItemStack(Items.WATER_BUCKET)
            Biome.Category.SWAMP -> ItemStack(Items.LILY_PAD)
            Biome.Category.MUSHROOM -> ItemStack(Items.RED_MUSHROOM)
            Biome.Category.NETHER -> ItemStack(Items.NETHERRACK)
            else -> ItemStack(Items.DIRT)
        }
    }

    companion object {
        val TYPE = IIngredientType { BiomeIngredient::class.java }
        val HELPER = BiomeIngredientHelper()
        val RENDERER = BiomeIngredientRenderer()

        fun getIngredients(): List<BiomeIngredient> =
            ForgeRegistries.BIOMES.values.map { biome -> BiomeIngredient(biome) }

        fun fromResourceLocation(rl: ResourceLocation): BiomeIngredient? =
            ForgeRegistries.BIOMES.getValue(rl)?.let { BiomeIngredient(it) }

    }
}