package com.minmax.ultradex.jei.pokemon

import com.pixelmonmod.api.pokemon.PokemonSpecification
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance
import com.pixelmonmod.pixelmon.enums.EnumGrowth
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
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

object PokemonUtils {
    fun getScale(livingEntity: LivingEntity): Float {
        val width = livingEntity.bbWidth
        val height = livingEntity.bbHeight
        return if (width <= height) {
            if (height < 0.9) 50.0f else if (height < 1) 35.0f else if (height < 1.8) 33.0f else if (height < 2) 32.0f else if (height < 3) 24.0f else if (height < 4) 20.0f else 10.0f
        } else {
            if (width < 1) 38.0f else if (width < 2) 27.0f else if (width < 3) 13.0f else 9.0f
        }
    }

    fun getOffsetY(livingEntity: LivingEntity): Int {
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

    fun getTooltipText(item: ItemWithChance): List<ITextComponent> {
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

    fun createOrdinaryPokemon(pokemonSpec: PokemonSpecification): Pokemon {
        val pokemon = pokemonSpec.create()
        pokemon.growth = EnumGrowth.Ordinary;
        return pokemon
    }
}