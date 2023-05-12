package com.minmax.ultradex.jei;

import com.minmax.ultradex.UltraDex;
import com.minmax.ultradex.jei.PixelmonDrop.PokemonCategory;
import com.minmax.ultradex.jei.PixelmonDrop.PokemonWrapper;
import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        registration.addRecipes(asRecipes(getVulpixes(), PokemonWrapper::new), POKEMON);
    }

    @Nonnull
    private List<PokemonDropInformation> getVulpixes() {
        RegistryValue<Species> vulpixSpecies = PixelmonSpecies.VULPIX;
        LOGGER.debug("vulpixSpecies: " + vulpixSpecies.toString());
        if (!vulpixSpecies.isInitialized()) {
            LOGGER.debug("vulpixSpecies not initialized");
            return Collections.emptyList();
        }

        Optional<Species> species = PixelmonSpecies.VULPIX.getValue();
        if (!species.isPresent()) {
            LOGGER.debug("vulpixSpecies not present");
            return Collections.emptyList();
        }
        Pokemon pokemon = PokemonFactory.create(species.get());
        LOGGER.debug("pokemon: " + pokemon.getDisplayName());
        Set<PokemonDropInformation> dropsSet = DropItemRegistry.pokemonDrops.get(species.get());
        LOGGER.debug("drops: " + (dropsSet));
        if (dropsSet == null) {
            return Collections.emptyList();
        }
        List<PokemonDropInformation> drops = new ArrayList<>(dropsSet);
        LOGGER.debug("drops: " + drops);
        if (drops.size() > 0) {
            LOGGER.debug("drops[0].getDrops(): " + drops.get(0).getDrops());
        }
        return drops;
    }

    public static void resetCategories() {
        if (jeiRuntime != null) {
            for (ResourceLocation category : CATEGORIES)
                jeiRuntime.getRecipeManager().unhideRecipeCategory(category);
        }
    }

    public static IJeiHelpers getJeiHelpers() {
        return jeiHelpers;
    }

    private static <T, R> Collection<R> asRecipes(Collection<T> collection, Function<T, R> transformer) {
        return collection.stream().map(transformer).collect(Collectors.toList());
    }
}
