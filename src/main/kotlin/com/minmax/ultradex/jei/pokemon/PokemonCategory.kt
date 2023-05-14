package com.minmax.ultradex.jei.pokemon

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.jei.PixelmonJEIPlugin
import jeresources.jei.BlankJEIRecipeCategory
import jeresources.reference.Resources
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import net.minecraft.util.ResourceLocation
import javax.annotation.Nonnull

class PokemonCategory(guiHelper: IGuiHelper) : BlankJEIRecipeCategory<PokemonWrapper>(
    guiHelper.createDrawable(
        Resources.Gui.Jei.TABS, 16, 16, 16, 16
    )
) {
    @Nonnull
    override fun getUid(): ResourceLocation {
        return PixelmonJEIPlugin.POKEMON
    }

    @Nonnull
    override fun getRecipeClass(): Class<out PokemonWrapper> {
        return PokemonWrapper::class.java
    }

    @Suppress("OVERRIDE_DEPRECATION")
    @Nonnull
    override fun getTitle(): String {
        return "Pokemon"
    }

    @Nonnull
    override fun getBackground(): IDrawable {
        return Resources.Gui.Jei.MOB
    }

    override fun setRecipe(
        @Nonnull recipeLayout: IRecipeLayout,
        @Nonnull recipeWrapper: PokemonWrapper,
        @Nonnull ingredients: IIngredients
    ) {
        var xOffset = 0
        var slot = 0
        for (i in 0 until Settings.ITEMS_PER_ROW) {
            var yOffset = 0
            for (ii in 0 until Settings.ITEMS_PER_COLUMN) {
                recipeLayout.itemStacks.init(slot++, false, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset)
                yOffset += 80 / Settings.ITEMS_PER_COLUMN
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW
        }
        recipeLayout.itemStacks.addTooltipCallback(recipeWrapper)
        val drops = recipeWrapper.drops
        val slotIndex = Math.min(drops.size, Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN)
        for (i in 0 until slotIndex) recipeLayout.itemStacks[i] = drops[i].itemStack
    }

    companion object {
        protected const val X_FIRST_ITEM = 97
        protected const val Y_FIRST_ITEM = 43
    }
}
