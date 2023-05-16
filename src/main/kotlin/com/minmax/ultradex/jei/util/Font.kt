package com.minmax.ultradex.jei.util

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

class Font private constructor(private val isSmall: Boolean) {
    fun printString(matrixStack: MatrixStack, text: String, x: Int, y: Int, color: Int = 8, shadow: Boolean = false) {
        printString(matrixStack, StringTextComponent(text), x, y, color, shadow)
    }

    fun printString(
        matrixStack: MatrixStack,
        text: ITextComponent,
        x: Int,
        y: Int,
        color: Int = 8,
        shadow: Boolean = false
    ) {
        doTransform(matrixStack, x, y)
        if (shadow) {
            Minecraft.getInstance().font.drawShadow(matrixStack, text, 0f, 0f, color)
        } else {
            Minecraft.getInstance().font.draw(matrixStack, text, 0f, 0f, color)
        }
        matrixStack.popPose()
    }

    private fun doTransform(matrixStack: MatrixStack, x: Int, y: Int) {
        matrixStack.pushPose()
        matrixStack.translate(x.toDouble(), y.toDouble(), 0.0)
        if (isSmall) {
            matrixStack.scale(SCALING, SCALING, 1f)
        }
    }

    companion object {
        val small = Font(true)
        val normal = Font(false)
        private const val SCALING = 0.8f
    }
}
