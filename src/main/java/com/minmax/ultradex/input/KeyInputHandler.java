package com.minmax.ultradex.input;

import com.minmax.ultradex.UltraDex;
import com.minmax.ultradex.gui.DexScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().screen == null) {
            if (KeyBindings.dexKey.isDown()) {
                UltraDex.LOGGER.debug("dexKey pressed");
                DexScreen.open();
            }
        }
    }
}
