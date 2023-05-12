package com.minmax.ultradex.jei.PixelmonDrop;

import com.minmax.ultradex.config.Settings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance;
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation;
import com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper;
import jeresources.compatibility.CompatBase;
import jeresources.util.CollectionHelper;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class PokemonWrapper implements IRecipeCategoryExtension, ITooltipCallback<ItemStack> {
    private final PokemonDropInformation pokemonDropInformation;
    private final Pokemon pokemon;
    private final LivingEntity livingEntity;
    private float scale;
    private final List<String> biomes;
    private final List<ItemWithChance> drops;

    private int offsetY;

    public PokemonWrapper(PokemonDropInformation pokemonDropInformation) {
        this.pokemonDropInformation = pokemonDropInformation;
        this.pokemon = this.pokemonDropInformation.getPokemonSpec().create();
        this.biomes = PokemonSpawnHelper.getBiomes(this.pokemon);
        this.drops = pokemonDropInformation.getDrops();
        this.livingEntity = this.pokemonDropInformation.getPokemonSpec().create(CompatBase.getWorld());
        this.scale = getScale(this.livingEntity);
        this.offsetY = getOffsetY(this.livingEntity);
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.getDrops().stream().map(ItemWithChance::getItemStack).collect(Collectors.toList()));
    }

    public List<ItemWithChance> getDrops() {
        return this.drops;
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        LivingEntity livingEntity = this.livingEntity;

        RenderHelper.scissor(matrixStack, 7, 43, 59, 79);
        this.scale = getScale(this.livingEntity);
        this.offsetY = getOffsetY(this.livingEntity);
        RenderHelper.renderEntity(
                matrixStack,
                37, 105 - offsetY, scale,
                38 - mouseX,
                70 - offsetY - mouseY,
                livingEntity
        );
        RenderHelper.stopScissor();


        String mobName = PokemonHelper.getFullName(pokemon).getString();
        if (Settings.showDevData) {
            String entityString = livingEntity.getStringUUID();
            mobName += " (" + entityString + ")";
        }
        Font.normal.print(matrixStack, mobName, 7, 2);
        Font.normal.print(matrixStack, "Form: " + pokemon.getForm().getName(), 7, 12);
        Font.normal.print(matrixStack, "Biomes", 7, 22);
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltipStrings(double mouseX, double mouseY) {
        if (isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(StringTextComponent::new, this.biomes.toArray(new String[0]));
        return Collections.emptyList();
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<ITextComponent> tooltip) {
        List<ITextComponent> list = getToolTip(ingredient);
        if (list != null)
            tooltip.addAll(list);
    }

    public List<ITextComponent> getToolTip(ItemStack stack) {
        for (ItemWithChance item : this.getDrops()) {
            if (stack.sameItem(item.getItem()))
                return getTooltipText(item);
        }
        return null;
    }

    private boolean isOnBiome(double mouseX, double mouseY) {
        return 2 <= mouseX
                && mouseX < 165
                && 22 <= mouseY
                && mouseY < 22 + 10;
    }

    private static float getScale(@Nonnull LivingEntity LivingEntity) {
        float width = LivingEntity.getBbWidth();
        float height = LivingEntity.getBbHeight();
        if (width <= height) {
            if (height < 0.9) return 50.0F;
            else if (height < 1) return 35.0F;
            else if (height < 1.8) return 33.0F;
            else if (height < 2) return 32.0F;
            else if (height < 3) return 24.0F;
            else if (height < 4) return 20.0F;
            else return 10.0F;
        } else {
            if (width < 1) return 38.0F;
            else if (width < 2) return 27.0F;
            else if (width < 3) return 13.0F;
            else return 9.0F;
        }
    }


    private static int getOffsetY(@Nonnull LivingEntity livingEntity) {
        int offsetY = 0;
        if (livingEntity instanceof SquidEntity) offsetY = 20;
        else if (livingEntity instanceof TurtleEntity) offsetY = 10;
        else if (livingEntity instanceof WitchEntity) offsetY = -5;
        else if (livingEntity instanceof GhastEntity) offsetY = 15;
        else if (livingEntity instanceof WitherEntity) offsetY = -15;
        else if (livingEntity instanceof EnderDragonEntity) offsetY = 15;
        else if (livingEntity instanceof EndermanEntity) offsetY = -10;
        else if (livingEntity instanceof GolemEntity) offsetY = -10;
        else if (livingEntity instanceof AnimalEntity) offsetY = -20;
        else if (livingEntity instanceof VillagerEntity) offsetY = -15;
        else if (livingEntity instanceof WanderingTraderEntity) offsetY = -15;
        else if (livingEntity instanceof BlazeEntity) offsetY = -10;
        else if (livingEntity instanceof CreeperEntity) offsetY = -15;
        return offsetY;
    }

    private static List<ITextComponent> getTooltipText(ItemWithChance item) {
        return Collections.singletonList(new StringTextComponent(formatChance(item.getChance()) + " %"));
    }

    private static String formatChance(double chance) {
        chance *= 100;
        if (chance < 10) {
            return java.lang.String.format("%.1f", chance);
        } else {
            return String.format("%2d", (int) chance);
        }
    }

}
