package dev.lasm.betterp2p.client.gui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

// Fleet port (26.1): GUI drawing is extracted into render states; the immediate Tesselator/BufferUploader path is gone,
// GuiGraphicsExtractor#blit covers the textured quad.
fun GuiGraphicsExtractor.drawTexturedQuad(
    texture: Identifier,
    x0: Float,
    y0: Float,
    x1: Float,
    y1: Float,
    u0: Float,
    v0: Float,
    u1: Float,
    v1: Float
) {
    blit(texture, x0.toInt(), y0.toInt(), x1.toInt(), y1.toInt(), u0, u1, v0, v1)
}

fun GuiGraphicsExtractor.drawIcon(srcX: Int, srcY: Int, x: Int, y: Int, width: Int = 16, height: Int = 16) {
    blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, srcX.toFloat(), srcY.toFloat(), width, height, 288, 264)
}

fun AbstractWidget.isClicked(mouseX: Double, mouseY: Double) =
    this.isActive &&
        this.visible &&
        (mouseX >= x.toDouble()) &&
        (mouseY >= y.toDouble()) &&
        (mouseX < (this.x + this.width).toDouble()) &&
        (mouseY < (this.y + this.height).toDouble())

fun drawBlockIcon(
    graphics: GuiGraphicsExtractor,
    icon: Identifier,
    overlay: Identifier =
        Identifier.fromNamespaceAndPath("ae2", "textures/part/p2p_tunnel_front.png"),
    x: Int,
    y: Int,
    width: Int = 16,
    height: Int = 16
) {
    graphics.drawTexturedQuad(
        icon,
        x0 = x.toFloat() + 2,
        y0 = y.toFloat() + 2,
        x1 = x.toFloat() + width - 2,
        y1 = y.toFloat() + height - 2,
        u0 = 0.0f,
        v0 = 0.0f,
        u1 = 1.0f,
        v1 = 1.0f
    )

    graphics.drawTexturedQuad(
        overlay,
        x0 = x.toFloat(),
        y0 = y.toFloat(),
        x1 = x.toFloat() + width,
        y1 = y.toFloat() + height,
        u0 = 0.0f,
        v0 = 0.0f,
        u1 = 1.0f,
        v1 = 1.0f
    )
}
