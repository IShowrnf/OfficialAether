package net.aether.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

/**
 * Rasterises a java.awt font into a single OpenGL texture atlas so the client UI can use a modern
 * typeface instead of the vanilla bitmap font.
 */
public class TrueTypeFont
{
    private static final int CHARS = 256;
    private static final int GRID = 16;
    private static final int PADDING = 2;

    private final int[] charWidths = new int[CHARS];
    private final int cellWidth;
    private final int cellHeight;
    private final int atlasWidth;
    private final int atlasHeight;
    private final int fontHeight;
    private final int ascent;
    private final DynamicTexture texture;

    public TrueTypeFont(Font font)
    {
        BufferedImage probe = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D probeGraphics = probe.createGraphics();
        probeGraphics.setFont(font);
        FontMetrics metrics = probeGraphics.getFontMetrics();

        int widest = 0;

        for (int i = 0; i < CHARS; ++i)
        {
            this.charWidths[i] = metrics.charWidth((char)i);
            widest = Math.max(widest, this.charWidths[i]);
        }

        probeGraphics.dispose();

        this.fontHeight = metrics.getHeight();
        this.ascent = metrics.getAscent();
        this.cellWidth = widest + PADDING * 2;
        this.cellHeight = this.fontHeight + PADDING * 2;
        this.atlasWidth = this.cellWidth * GRID;
        this.atlasHeight = this.cellHeight * GRID;

        BufferedImage atlas = new BufferedImage(this.atlasWidth, this.atlasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = atlas.createGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, this.atlasWidth, this.atlasHeight);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        for (int i = 0; i < CHARS; ++i)
        {
            int column = i % GRID;
            int row = i / GRID;
            graphics.drawString(String.valueOf((char)i), column * this.cellWidth + PADDING, row * this.cellHeight + PADDING + this.ascent);
        }

        graphics.dispose();
        this.texture = new DynamicTexture(atlas);
    }

    /**
     * Draws the string at GUI scale 1 pixel per font pixel, using the client's 2x supersampled atlas.
     */
    public float drawString(String text, float x, float y, int color)
    {
        if (text == null)
        {
            return x;
        }

        float alpha = (float)(color >> 24 & 255) / 255.0F;

        if (alpha == 0.0F)
        {
            alpha = 1.0F;
        }

        float red = (float)(color >> 16 & 255) / 255.0F;
        float green = (float)(color >> 8 & 255) / 255.0F;
        float blue = (float)(color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.bindTexture(this.texture.getGlTextureId());
        GlStateManager.color(red, green, blue, alpha);

        float cursor = x;
        GL11.glBegin(GL11.GL_QUADS);

        for (int i = 0; i < text.length(); ++i)
        {
            char character = text.charAt(i);

            if (character >= CHARS)
            {
                character = '?';
            }

            int column = character % GRID;
            int row = character / GRID;
            float u0 = (float)(column * this.cellWidth) / (float)this.atlasWidth;
            float v0 = (float)(row * this.cellHeight) / (float)this.atlasHeight;
            float u1 = (float)((column + 1) * this.cellWidth) / (float)this.atlasWidth;
            float v1 = (float)((row + 1) * this.cellHeight) / (float)this.atlasHeight;
            float drawX = cursor - (float)PADDING * 0.5F;
            float width = (float)this.cellWidth * 0.5F;
            float height = (float)this.cellHeight * 0.5F;

            GL11.glTexCoord2f(u0, v0);
            GL11.glVertex2f(drawX, y);
            GL11.glTexCoord2f(u0, v1);
            GL11.glVertex2f(drawX, y + height);
            GL11.glTexCoord2f(u1, v1);
            GL11.glVertex2f(drawX + width, y + height);
            GL11.glTexCoord2f(u1, v0);
            GL11.glVertex2f(drawX + width, y);

            cursor += (float)this.charWidths[character] * 0.5F;
        }

        GL11.glEnd();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        return cursor;
    }

    public float drawCenteredString(String text, float centerX, float y, int color)
    {
        return this.drawString(text, centerX - this.getStringWidth(text) / 2.0F, y, color);
    }

    public float getStringWidth(String text)
    {
        if (text == null)
        {
            return 0.0F;
        }

        float width = 0.0F;

        for (int i = 0; i < text.length(); ++i)
        {
            char character = text.charAt(i);
            width += (float)this.charWidths[character >= CHARS ? '?' : character] * 0.5F;
        }

        return width;
    }

    public float getHeight()
    {
        return (float)this.fontHeight * 0.5F;
    }

    /**
     * Trims the string so it fits within maxWidth, appending an ellipsis when it had to be cut.
     */
    public String trimToWidth(String text, float maxWidth)
    {
        if (this.getStringWidth(text) <= maxWidth)
        {
            return text;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < text.length(); ++i)
        {
            if (this.getStringWidth(builder.toString() + text.charAt(i) + "...") > maxWidth)
            {
                break;
            }

            builder.append(text.charAt(i));
        }

        return builder.toString() + "...";
    }
}
