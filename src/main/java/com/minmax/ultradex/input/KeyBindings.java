package com.minmax.ultradex.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;


public class KeyBindings {
    public static KeyBinding dexKey;

    public static void init() {
        dexKey = new KeyBinding("key.ultradex.dex",
                KeyConflictContext.IN_GAME,
                InputMappings.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "key.categories.misc");
        ClientRegistry.registerKeyBinding(dexKey);

    }
}
