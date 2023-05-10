package com.minmax.ultradex.gui;

import com.minmax.ultradex.UltraDex;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class DexScreen extends Screen {

    private static final int WIDTH = 179;
    private static final int HEIGHT = 151;
    protected DexScreen() {
        super(new StringTextComponent("Dex Screen"));
    }

    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        addButton(new Button(relX + 10, relY + 10, 160, 20, new StringTextComponent("Skeleton"), button -> spawn("minecraft:skeleton")));
        addButton(new Button(relX + 10, relY + 37, 160, 20, new StringTextComponent("Zombie"), button -> spawn("minecraft:zombie")));
        addButton(new Button(relX + 10, relY + 64, 160, 20, new StringTextComponent("Cow"), button -> spawn("minecraft:cow")));
        addButton(new Button(relX + 10, relY + 91, 160, 20, new StringTextComponent("Sheep"), button -> spawn("minecraft:sheep")));
        addButton(new Button(relX + 10, relY + 118, 160, 20, new StringTextComponent("Chicken"), button -> spawn("minecraft:chicken")));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void spawn(String id) {
        UltraDex.LOGGER.debug("spawn(" + id +")");
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bind(GUI);
//        int relX = (this.width - WIDTH) / 2;
//        int relY = (this.height - HEIGHT) / 2;
//        this.blit(matrixStack, relX, relY, 0, 0, WIDTH, HEIGHT);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    public static void open() {
        Minecraft.getInstance().setScreen(new DexScreen());
    }
}
