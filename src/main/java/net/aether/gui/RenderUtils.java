package net.aether.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderUtils
{
    private static final int CIRCLE_SEGMENTS = 12;

    private static void bindColor(int color)
    {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        GlStateManager.color(r, g, b, a);
    }

    private static void begin()
    {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    }

    private static void end()
    {
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void rect(double x, double y, double width, double height, int color)
    {
        begin();
        bindColor(color);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
        end();
    }

    /**
     * Vertical two-stop gradient (top colour to bottom colour).
     */
    public static void gradientRect(double x, double y, double width, double height, int top, int bottom)
    {
        begin();
        GL11.glBegin(GL11.GL_QUADS);
        bindColor(top);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + width, y);
        bindColor(bottom);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x, y + height);
        GL11.glEnd();
        end();
    }

    /**
     * Horizontal two-stop gradient (left colour to right colour).
     */
    public static void gradientRectHorizontal(double x, double y, double width, double height, int left, int right)
    {
        begin();
        GL11.glBegin(GL11.GL_QUADS);
        bindColor(left);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + height);
        bindColor(right);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glEnd();
        end();
    }

    public static void roundedRect(double x, double y, double width, double height, double radius, int color)
    {
        double r = Math.min(radius, Math.min(width, height) / 2.0D);
        begin();
        bindColor(color);
        GL11.glBegin(GL11.GL_POLYGON);
        traceRoundedOutline(x, y, width, height, r);
        GL11.glEnd();
        end();
    }

    public static void roundedRectOutline(double x, double y, double width, double height, double radius, float lineWidth, int color)
    {
        double r = Math.min(radius, Math.min(width, height) / 2.0D);
        begin();
        bindColor(color);
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        traceRoundedOutline(x, y, width, height, r);
        GL11.glEnd();
        GL11.glLineWidth(1.0F);
        end();
    }

    private static void traceRoundedOutline(double x, double y, double width, double height, double r)
    {
        corner(x + width - r, y + height - r, r, 0.0D);
        corner(x + r, y + height - r, r, 90.0D);
        corner(x + r, y + r, r, 180.0D);
        corner(x + width - r, y + r, r, 270.0D);
    }

    private static void corner(double cx, double cy, double r, double startAngle)
    {
        for (int i = 0; i <= CIRCLE_SEGMENTS; ++i)
        {
            double angle = Math.toRadians(startAngle + 90.0D * (double)i / (double)CIRCLE_SEGMENTS);
            GL11.glVertex2d(cx + Math.cos(angle) * r, cy + Math.sin(angle) * r);
        }
    }

    public static void circle(double cx, double cy, double radius, int color)
    {
        begin();
        bindColor(color);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(cx, cy);

        for (int i = 0; i <= 40; ++i)
        {
            double angle = Math.toRadians((double)i * 9.0D);
            GL11.glVertex2d(cx + Math.cos(angle) * radius, cy + Math.sin(angle) * radius);
        }

        GL11.glEnd();
        end();
    }

    /**
     * Enables a scissor box using GUI-space (scaled) coordinates.
     */
    public static void beginScissor(double x, double y, double width, double height)
    {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double scale = (double)resolution.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(x * scale), (int)(((double)resolution.getScaledHeight() - (y + height)) * scale), (int)(width * scale), (int)(height * scale));
    }

    public static void endScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static int withAlpha(int color, int alpha)
    {
        return alpha << 24 | color & 0xFFFFFF;
    }

    public static int blend(int from, int to, float progress)
    {
        float p = Math.max(0.0F, Math.min(1.0F, progress));
        int a = (int)((float)(from >> 24 & 255) + (float)((to >> 24 & 255) - (from >> 24 & 255)) * p);
        int r = (int)((float)(from >> 16 & 255) + (float)((to >> 16 & 255) - (from >> 16 & 255)) * p);
        int g = (int)((float)(from >> 8 & 255) + (float)((to >> 8 & 255) - (from >> 8 & 255)) * p);
        int b = (int)((float)(from & 255) + (float)((to & 255) - (from & 255)) * p);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int rainbow(int offset, float saturation, float brightness)
    {
        float hue = (float)((System.currentTimeMillis() + (long)offset) % 4000L) / 4000.0F;
        return Color.HSBtoRGB(hue, saturation, brightness) | 0xFF000000;
    }
}
