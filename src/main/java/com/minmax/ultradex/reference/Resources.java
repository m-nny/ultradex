package com.minmax.ultradex.reference;

import com.minmax.ultradex.jei.BackgroundDrawable;
import net.minecraft.util.ResourceLocation;

public final class Resources {
    public static final class Gui {
        public static final class Jei {
            public static final BackgroundDrawable MOB = new BackgroundDrawable(Textures.Gui.Jei.MOB, 163, 120);
//            public static final ResourceLocation TABS = new ResourceLocation(Reference.ID, Textures.Gui.Jei.TABS);
        }
    }

    public static final class Vanilla {
        public static final ResourceLocation FONT = new ResourceLocation("textures/font/ascii.png");
        public static final ResourceLocation CHEST = new ResourceLocation("textures/entity/chest/normal.png");
    }
}