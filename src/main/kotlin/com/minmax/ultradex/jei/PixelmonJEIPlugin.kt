package com.minmax.ultradex.jei

import com.minmax.ultradex.UltraDex
import com.minmax.ultradex.jei.categories.PokemonDropCategory
import com.minmax.ultradex.jei.categories.PokemonEvolutionCategory
import com.minmax.ultradex.jei.categories.PokemonSpawnCategory
import com.minmax.ultradex.jei.ingredients.biome.BiomeIngredient
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.registires.PokemonRegistries
import com.minmax.ultradex.jei.util.ReloadUtil
import com.minmax.ultradex.reference.Reference
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IModIngredientRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.util.ResourceLocation

@JeiPlugin
class PixelmonJEIPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation = UID

    override fun registerIngredients(registration: IModIngredientRegistration) {
        UltraDex.LOGGER.debug("registerIngredients()")
        registration.register(
            PokemonIngredient.TYPE,
            PokemonRegistries.getPokemons(),
            PokemonIngredient.HELPER,
            PokemonIngredient.RENDERER,
        )
        registration.register(
            BiomeIngredient.TYPE,
            PokemonRegistries.getBiomes(),
            BiomeIngredient.HELPER,
            BiomeIngredient.RENDERER,
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        UltraDex.LOGGER.debug("registerCategories()")
        registration.addRecipeCategories(PokemonEvolutionCategory(registration.jeiHelpers.guiHelper))
        registration.addRecipeCategories(PokemonSpawnCategory(registration.jeiHelpers.guiHelper))
        registration.addRecipeCategories(PokemonDropCategory(registration.jeiHelpers.guiHelper))
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        UltraDex.LOGGER.debug("reloadRegistries()")
        ReloadUtil.reloadPixelmonRegistries()

        UltraDex.LOGGER.debug("registerRecipes()")
        registration.addRecipes(PokemonRegistries.getDropsRecipes(), POKEMON_DROPS)
        registration.addRecipes(PokemonRegistries.getEvoRecipes(), POKEMON_EVOLUTIONS)
        registration.addRecipes(PokemonRegistries.getSpawnRecipes(), POKEMON_SPAWNING)
    }


    companion object {
        private val UID = ResourceLocation(Reference.ID, "main")
        val POKEMON_DROPS = ResourceLocation(Reference.ID, "pokemon_drops")
        val POKEMON_EVOLUTIONS = ResourceLocation(Reference.ID, "pokemon_evolutions")
        val POKEMON_SPAWNING = ResourceLocation(Reference.ID, "pokemon_spawning")
    }
}
