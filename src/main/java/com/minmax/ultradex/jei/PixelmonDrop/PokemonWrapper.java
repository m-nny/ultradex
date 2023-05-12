package com.minmax.ultradex.jei.PixelmonDrop;

import com.minmax.ultradex.config.Settings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance;
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation;
import jeresources.compatibility.CompatBase;
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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonWrapper implements IRecipeCategoryExtension, ITooltipCallback<ItemStack> {
    private final PokemonDropInformation pokemonDropInformation;
    private float scale;

    private int offsetY;

    PokemonWrapper(PokemonDropInformation pokemonDropInformation) {
        this.pokemonDropInformation = pokemonDropInformation;
        this.scale = getScale(this.getLivingEntity());
        this.offsetY = getOffsetY(this.getLivingEntity());
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.pokemonDropInformation.getDrops().stream().map(ItemWithChance::getItemStack).collect(Collectors.toList()));
    }

    public List<ItemWithChance> getDrops() {
        return this.pokemonDropInformation.getDrops();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        LivingEntity livingEntity = this.getLivingEntity();

        RenderHelper.scissor(matrixStack, 7, 43, 59, 79);
        this.scale = getScale(this.getLivingEntity());
        this.offsetY = getOffsetY(this.getLivingEntity());
        RenderHelper.renderEntity(
                matrixStack,
                37, 105 - offsetY, scale,
                38 - mouseX,
                70 - offsetY - mouseY,
                livingEntity
        );
        RenderHelper.stopScissor();

        String mobName = "Vulpix"; // TODO(minmax): get actual name
        if (Settings.showDevData) {
            String entityString = livingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(matrixStack, mobName, 7, 2);
//        Font.normal.print(matrixStack, this.mob.getBiomes().length > 1 ? TranslationHelper.translateAndFormat("jer.mob.biome") : TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + this.mob.getBiomes()[0], 7, 12);
//        Font.normal.print(matrixStack, this.mob.getLightLevel(), 7, 22);
//        Font.normal.print(matrixStack,  ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<ITextComponent> tooltip) {

    }

    private static float getScale(LivingEntity LivingEntity) {
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

    private LivingEntity getLivingEntity() {
        return this.pokemonDropInformation.getPokemonSpec().create(CompatBase.getWorld());
    }


    private static int getOffsetY(LivingEntity livingEntity) {
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

}
