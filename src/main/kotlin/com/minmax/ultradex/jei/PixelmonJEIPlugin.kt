package com.minmax.ultradex.jei

import com.minmax.ultradex.UltraDex.LOGGER
import com.minmax.ultradex.UltraDex.rl
import com.minmax.ultradex.jei.pokemon.PokemonCategory
import com.minmax.ultradex.jei.pokemon.PokemonWrapper
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry
import jeresources.reference.Reference
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.util.ResourceLocation
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.annotation.Nonnull

@JeiPlugin
class PixelmonJEIPlugin : IModPlugin {
    @Nonnull
    override fun getPluginUid(): ResourceLocation {
        return UID
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(PokemonCategory(registration.jeiHelpers.guiHelper))
    }

    override fun registerRecipes(@Nonnull registration: IRecipeRegistration) {
        registration.addRecipes(species.map { pokemonDropInformation -> PokemonWrapper(pokemonDropInformation) }
            .collect(Collectors.toList()), POKEMON)
    }

    @get:Nonnull
    private val species: Stream<PokemonDropInformation>
        get() {
            val allSpecies = PixelmonSpecies.getAll()
            LOGGER.debug("allSpecies.size(): " + allSpecies.size)
            return allSpecies.stream()
                .flatMap { species ->
                    DropItemRegistry.pokemonDrops[species]?.stream() ?: Stream.empty()
                }
        }

    companion object {
        private val UID = rl("main")

        @JvmField
        val POKEMON = ResourceLocation(Reference.ID, "pokemon")

    }
}
