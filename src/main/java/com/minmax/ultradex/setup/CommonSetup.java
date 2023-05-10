package com.minmax.ultradex.setup;

import com.minmax.ultradex.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.minmax.ultradex.UltraDex.MOD_ID;
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("UltraDex") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.FIRSTBLOCK);
        }
    };
    public static void init(final FMLCommonSetupEvent event) {

    }
}
