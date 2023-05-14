package com.minmax.ultradex.jei.pokemon

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.pokemon.PokemonSpawnHelper.getBiomes
import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation
import com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper
import jeresources.compatibility.CompatBase
import jeresources.util.Font
import jeresources.util.RenderHelper
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.ingredient.ITooltipCallback
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.merchant.villager.VillagerEntity
import net.minecraft.entity.merchant.villager.WanderingTraderEntity
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.GolemEntity
import net.minecraft.entity.passive.SquidEntity
import net.minecraft.entity.passive.TurtleEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import java.util.stream.Collectors
import javax.annotation.Nonnull

class PokemonWrapper(pokemonDropInformation: PokemonDropInformation) : IRecipeCategoryExtension,
    ITooltipCallback<ItemStack> {
    private val pokemon: Pokemon
    private val livingEntity: LivingEntity
    private var scale: Float
    private val biomes: List<String>

    @JvmField
    val drops: List<ItemWithChance>
    private var offsetY: Int

    init {
        pokemon = pokemonDropInformation.pokemonSpec.create()
        biomes = getBiomes(pokemon)
        drops = pokemonDropInformation.drops
        livingEntity = pokemonDropInformation.pokemonSpec.create(CompatBase.getWorld())
        scale = getScale(livingEntity)
        offsetY = getOffsetY(livingEntity)
    }

    override fun setIngredients(@Nonnull ingredients: IIngredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, drops.stream().map { obj: ItemWithChance -> obj.itemStack }
            .collect(Collectors.toList()))
    }

    override fun drawInfo(
        recipeWidth: Int,
        recipeHeight: Int,
        @Nonnull matrixStack: MatrixStack,
        mouseX: Double,
        mouseY: Double
    ) {
        val livingEntity = livingEntity
        RenderHelper.scissor(matrixStack, 7, 43, 59, 79)
        scale = getScale(this.livingEntity)
        offsetY = getOffsetY(this.livingEntity)
        RenderHelper.renderEntity(
            matrixStack,
            37, 105 - offsetY, scale.toDouble(),
            38 - mouseX,
            70 - offsetY - mouseY,
            livingEntity
        )
        RenderHelper.stopScissor()
        var mobName = PokemonHelper.getFullName(pokemon).string
        if (Settings.showDevData) {
            val entityString = livingEntity.stringUUID
            mobName += " ($entityString)"
        }
        Font.normal.print(matrixStack, mobName, 7, 2)
        Font.normal.print(matrixStack, "Form: " + pokemon.form.name, 7, 12)
        Font.normal.print(matrixStack, "Biomes", 7, 22)
    }

    @Nonnull
    override fun getTooltipStrings(mouseX: Double, mouseY: Double): List<ITextComponent> {
        if (!isOnBiome(mouseX, mouseY)) {
            return emptyList()
        }
        return biomes.stream().map { biome -> StringTextComponent(biome) }.collect(Collectors.toList())
    }

    override fun onTooltip(
        slotIndex: Int,
        input: Boolean,
        @Nonnull ingredient: ItemStack,
        @Nonnull tooltip: MutableList<ITextComponent>
    ) {
        val list = getToolTip(ingredient)
        if (list != null) tooltip.addAll(list)
    }

    private fun getToolTip(stack: ItemStack): List<ITextComponent>? {
        for (item in drops) {
            if (stack.sameItem(item.item)) return getTooltipText(item)
        }
        return null
    }

    private fun isOnBiome(mouseX: Double, mouseY: Double): Boolean {
        return 2 <= mouseX && mouseX < 165 && 22 <= mouseY && mouseY < 22 + 10
    }

    companion object {
        private fun getScale(@Nonnull livingEntity: LivingEntity): Float {
            val width = livingEntity.bbWidth
            val height = livingEntity.bbHeight
            return if (width <= height) {
                if (height < 0.9) 50.0f else if (height < 1) 35.0f else if (height < 1.8) 33.0f else if (height < 2) 32.0f else if (height < 3) 24.0f else if (height < 4) 20.0f else 10.0f
            } else {
                if (width < 1) 38.0f else if (width < 2) 27.0f else if (width < 3) 13.0f else 9.0f
            }
        }

        private fun getOffsetY(@Nonnull livingEntity: LivingEntity): Int {
            var offsetY = 0
            when (livingEntity) {
                is SquidEntity -> offsetY = 20
                is TurtleEntity -> offsetY = 10
                is WitchEntity -> offsetY = -5
                is GhastEntity -> offsetY = 15
                is WitherEntity -> offsetY = -15
                is EnderDragonEntity -> offsetY = 15
                is EndermanEntity -> offsetY = -10
                is GolemEntity -> offsetY = -10
                is AnimalEntity -> offsetY = -20
                is VillagerEntity -> offsetY = -15
                is WanderingTraderEntity -> offsetY = -15
                is BlazeEntity -> offsetY = -10
                is CreeperEntity -> offsetY = -15
            }
            return offsetY
        }

        private fun getTooltipText(item: ItemWithChance): List<ITextComponent> {
            return listOf<ITextComponent>(StringTextComponent(formatChance(item.chance) + " %"))
        }

        private fun formatChance(chance: Double): String {
            val chance100 = chance * 100.0
            return if (chance100 < 10) {
                String.format("%.1f", chance100)
            } else {
                String.format("%2d", chance100.toInt())
            }
        }
    }
}
