package com.minmax.ultradex.jei;

import com.minmax.ultradex.UltraDex;
import com.minmax.ultradex.jei.PixelmonDrop.PokemonCategory;
import com.minmax.ultradex.jei.PixelmonDrop.PokemonWrapper;
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import jeresources.reference.Reference;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.minmax.ultradex.UltraDex.LOGGER;

@JeiPlugin
public class PixelmonJEIPlugin implements IModPlugin {
    private static final ResourceLocation UID = UltraDex.rl("main");

    public static final ResourceLocation POKEMON = new ResourceLocation(Reference.ID, "pokemon");
    public static final ResourceLocation[] CATEGORIES = {POKEMON};
    private static IJeiHelpers jeiHelpers;
    private static IJeiRuntime jeiRuntime;

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        PixelmonJEIPlugin.jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new PokemonCategory());
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        registration.addRecipes(getSpecies().map(PokemonWrapper::new).collect(Collectors.toList()), POKEMON);
    }

    @Nonnull
    private Stream<PokemonDropInformation> getSpecies() {
        List<Species> allSpecies = PixelmonSpecies.getAll();
        LOGGER.debug("allSpecies.size(): " + allSpecies.size());
        return allSpecies.stream()
                .flatMap(species ->
                        Optional.ofNullable(DropItemRegistry.pokemonDrops.get(species))
                                .map(Collection::stream)
                                .orElseGet(Stream::empty)
                );
    }

    public static IJeiHelpers getJeiHelpers() {
        return jeiHelpers;
    }
}
