package com.minmax.ultradex.jei.PixelmonDrop;

import com.minmax.ultradex.config.Settings;
import com.minmax.ultradex.jei.PixelmonJEIPlugin;
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.reference.Resources;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;


public class PokemonCategory extends BlankJEIRecipeCategory<PokemonWrapper> {
    protected static final int X_FIRST_ITEM = 97;
    protected static final int Y_FIRST_ITEM = 43;

    public PokemonCategory() {
        super(PixelmonJEIPlugin.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 16, 16, 16));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return PixelmonJEIPlugin.POKEMON;
    }

    @Nonnull
    @Override
    public Class<? extends PokemonWrapper> getRecipeClass() {
        return PokemonWrapper.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Pokemon";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return Resources.Gui.Jei.MOB;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull PokemonWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        int xOffset = 0;
        int slot = 0;
        for (int i = 0; i < Settings.ITEMS_PER_ROW; i++) {
            int yOffset = 0;
            for (int ii = 0; ii < Settings.ITEMS_PER_COLUMN; ii++) {
                recipeLayout.getItemStacks().init(slot++, false, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset);
                yOffset += 80 / Settings.ITEMS_PER_COLUMN;
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW;
        }
        recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
        List<ItemWithChance> drops = recipeWrapper.getDrops();
        int slotIndex = Math.min(drops.size(), Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN);
        for (int i = 0; i < slotIndex; i++)
            recipeLayout.getItemStacks().set(i,drops.get(i).getItemStack());
    }
}
