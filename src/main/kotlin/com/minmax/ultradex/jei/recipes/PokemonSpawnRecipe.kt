package com.minmax.ultradex.jei.recipes

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.jei.ingredients.biome.BiomeIngredient
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.util.Font
import com.minmax.ultradex.jei.util.PokemonUtils
import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon
import com.pixelmonmod.pixelmon.api.util.ITranslatable
import com.pixelmonmod.pixelmon.api.world.WeatherType
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension
import net.minecraft.util.text.IFormattableTextComponent

class PokemonSpawnRecipe(spawnInfo: SpawnInfoPokemon, val type: PokemonSpawnType) : IRecipeCategoryExtension {
    val pokemon = PokemonUtils.getOrdinaryPokemon(spawnInfo.pokemonSpec)

    private val weathers: String =
        spawnInfo.condition.weathers?.joinToString(", ", transform = WeatherType::getLocalizedName) ?: ""
    private val times: String =
        spawnInfo.condition.times?.joinToString(", ", transform = ITranslatable::getLocalizedName) ?: ""
    val biomes: List<BiomeIngredient> =
        spawnInfo.condition.biomes.mapNotNull(BiomeIngredient.Companion::fromResourceLocation)
    private val rarity: IFormattableTextComponent = rarityString(spawnInfo.rarity)

    fun getPokemonIngredient() = PokemonIngredient(pokemon)

    override fun setIngredients(ingredients: IIngredients) {
        ingredients.setInputs(BiomeIngredient.TYPE, biomes)
        ingredients.setOutput(PokemonIngredient.TYPE, getPokemonIngredient())
    }

    override fun drawInfo(
        recipeWidth: Int,
        recipeHeight: Int,
        matrixStack: MatrixStack,
        mouseX: Double,
        mouseY: Double
    ) {
        val xPos = Settings.JeiPositions.X_TEXT
        var yPos = Settings.JeiPositions.Y_TEXT
        Font.small.printString(matrixStack, rarity, xPos, yPos)
        yPos += 8
        if (times.isNotEmpty()) {
            Font.small.printString(matrixStack, "Time: $times", xPos, yPos)
            yPos += 8
        }
        if (type != PokemonSpawnType.STANDARD) {
            Font.small.printString(matrixStack, "Type: ${type.type}", xPos, yPos)
            yPos += 8
        }
        if (weathers.isNotEmpty()) {
            Font.small.printString(matrixStack, "Weather: $weathers", xPos, yPos)
            yPos += 8
        }
    }

}