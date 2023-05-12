package com.minmax.ultradex;

import com.minmax.ultradex.setup.ClientSetup;
import com.minmax.ultradex.setup.CommonSetup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UltraDex.MOD_ID)
public class UltraDex {
    public static final String MOD_ID = "ultradex";
    public static final Logger LOGGER = LogManager.getLogger();


    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public UltraDex() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
