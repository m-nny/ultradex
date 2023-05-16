package com.minmax.ultradex.jei.util

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.vector.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import kotlin.math.atan
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
object RenderHelper {
    fun renderEntity(
        matrixStack: MatrixStack,
        x: Int,
        y: Int,
        scale: Double,
        yaw: Double,
        pitch: Double,
        livingEntity: LivingEntity
    ) {
        if (livingEntity.level == null) livingEntity.level = Minecraft.getInstance().level
        RenderSystem.pushMatrix()
        RenderSystem.multMatrix(matrixStack.last().pose())
        RenderSystem.translatef(x.toFloat(), y.toFloat(), 50.0f)
        RenderSystem.scalef(-scale.toFloat(), scale.toFloat(), scale.toFloat())
        val mobMatrix = MatrixStack()
        mobMatrix.mulPose(Vector3f.ZP.rotationDegrees(180.0f))
        RenderSystem.rotatef(atan(pitch / 40.0f).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        livingEntity.yo = (atan(yaw / 40.0f).toFloat() * 20.0f).toDouble()
        livingEntity.yRot = atan(yaw / 40.0f).toFloat() * 40.0f
        livingEntity.xRot = atan(pitch / 40.0f).toFloat() * 20.0f
        livingEntity.yHeadRot = livingEntity.yRot
        livingEntity.yHeadRotO = livingEntity.yRot
        mobMatrix.translate(0.0, livingEntity.y, 0.0)
        val entityRendererManager = Minecraft.getInstance().entityRenderDispatcher
        entityRendererManager.setRenderShadow(false)
        val renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource()
        RenderSystem.runAsFancy {
            entityRendererManager.render(
                livingEntity,
                0.0,
                0.0,
                0.0,
                0.0f,
                1.0f,
                mobMatrix,
                renderTypeBuffer,
                15728880
            )
        }
        renderTypeBuffer.endBatch()
        entityRendererManager.setRenderShadow(true)
        RenderSystem.popMatrix()
    }

    fun scissor(matrixStack: MatrixStack, x: Int, y: Int, w: Int, h: Int) {
        val scale = Minecraft.getInstance().window.guiScale
        val xyzTranslation = getGLTranslation(matrixStack, scale)
        val scissorX = (xyzTranslation[0] + x * scale).roundToInt()
        val scissorY = (Minecraft.getInstance().window.height - y * scale - h * scale - xyzTranslation[1]).roundToInt()
        val scissorW = (w * scale).roundToInt()
        val scissorH = (h * scale).roundToInt()
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH)
    }

    fun stopScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

    private fun getGLTranslation(matrixStack: MatrixStack, scale: Double): DoubleArray {
        val matrix = matrixStack.last().pose()
        val buf = BufferUtils.createFloatBuffer(16)
        matrix.store(buf)
        // { x, y, z }
        return doubleArrayOf(
            buf[getIndexFloatBuffer(0, 3)] * scale,
            buf[getIndexFloatBuffer(1, 3)] * scale,
            buf[getIndexFloatBuffer(2, 3)] * scale
        )
    }

    private fun getIndexFloatBuffer(x: Int, y: Int): Int {
        return y * 4 + x
    }

    fun renderItemStack(matrixStack: MatrixStack, xPosition: Int, yPosition: Int, ingredient: ItemStack) {
        RenderSystem.pushMatrix()
        RenderSystem.multMatrix(matrixStack.last().pose())
        RenderSystem.enableDepthTest()
        RenderHelper.turnBackOn()
        val itemRenderer = Minecraft.getInstance().itemRenderer
        itemRenderer.renderGuiItem(ingredient, xPosition, yPosition)
        RenderSystem.disableBlend()
        RenderHelper.turnOff()
        RenderSystem.popMatrix()
    }
}
