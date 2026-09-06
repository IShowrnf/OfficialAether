package net.aether.gui;

import net.aether.module.Category;

/**
 * Small vector glyphs drawn from primitives so the UI does not depend on external icon textures.
 */
public class Icons
{
    public static void category(Category category, double x, double y, double size, int color)
    {
        switch (category)
        {
            case COMBAT:
                swords(x, y, size, color);
                break;
            case MOVEMENT:
                runner(x, y, size, color);
                break;
            case RENDER:
                eye(x, y, size, color);
                break;
            case EXPLOIT:
                bug(x, y, size, color);
                break;
            case PLAYER:
                person(x, y, size, color);
                break;
            case WORLD:
                globe(x, y, size, color);
                break;
            case MISC:
            default:
                dots(x, y, size, color);
        }
    }

    public static void swords(double x, double y, double size, int color)
    {
        diagonal(x + size * 0.15D, y + size * 0.85D, x + size * 0.85D, y + size * 0.15D, color);
        diagonal(x + size * 0.85D, y + size * 0.85D, x + size * 0.15D, y + size * 0.15D, color);
    }

    public static void runner(double x, double y, double size, int color)
    {
        RenderUtils.circle(x + size * 0.55D, y + size * 0.18D, size * 0.12D, color);
        diagonal(x + size * 0.2D, y + size * 0.55D, x + size * 0.7D, y + size * 0.4D, color);
        diagonal(x + size * 0.45D, y + size * 0.45D, x + size * 0.3D, y + size * 0.85D, color);
        diagonal(x + size * 0.5D, y + size * 0.5D, x + size * 0.8D, y + size * 0.8D, color);
    }

    public static void eye(double x, double y, double size, int color)
    {
        RenderUtils.roundedRectOutline(x + size * 0.05D, y + size * 0.28D, size * 0.9D, size * 0.44D, size * 0.22D, 1.0F, color);
        RenderUtils.circle(x + size * 0.5D, y + size * 0.5D, size * 0.14D, color);
    }

    public static void bug(double x, double y, double size, int color)
    {
        RenderUtils.roundedRect(x + size * 0.28D, y + size * 0.22D, size * 0.44D, size * 0.56D, size * 0.2D, color);
        RenderUtils.rect(x + size * 0.08D, y + size * 0.38D, size * 0.16D, 1.0D, color);
        RenderUtils.rect(x + size * 0.76D, y + size * 0.38D, size * 0.16D, 1.0D, color);
        RenderUtils.rect(x + size * 0.08D, y + size * 0.62D, size * 0.16D, 1.0D, color);
        RenderUtils.rect(x + size * 0.76D, y + size * 0.62D, size * 0.16D, 1.0D, color);
    }

    public static void person(double x, double y, double size, int color)
    {
        RenderUtils.circle(x + size * 0.5D, y + size * 0.28D, size * 0.17D, color);
        RenderUtils.roundedRect(x + size * 0.22D, y + size * 0.55D, size * 0.56D, size * 0.32D, size * 0.16D, color);
    }

    public static void globe(double x, double y, double size, int color)
    {
        RenderUtils.roundedRectOutline(x + size * 0.08D, y + size * 0.08D, size * 0.84D, size * 0.84D, size * 0.42D, 1.0F, color);
        RenderUtils.rect(x + size * 0.08D, y + size * 0.49D, size * 0.84D, 1.0D, color);
        RenderUtils.roundedRectOutline(x + size * 0.32D, y + size * 0.08D, size * 0.36D, size * 0.84D, size * 0.18D, 1.0F, color);
    }

    public static void dots(double x, double y, double size, int color)
    {
        RenderUtils.circle(x + size * 0.22D, y + size * 0.5D, size * 0.08D, color);
        RenderUtils.circle(x + size * 0.5D, y + size * 0.5D, size * 0.08D, color);
        RenderUtils.circle(x + size * 0.78D, y + size * 0.5D, size * 0.08D, color);
    }

    public static void search(double x, double y, double size, int color)
    {
        RenderUtils.roundedRectOutline(x + size * 0.1D, y + size * 0.1D, size * 0.6D, size * 0.6D, size * 0.3D, 1.0F, color);
        diagonal(x + size * 0.62D, y + size * 0.62D, x + size * 0.9D, y + size * 0.9D, color);
    }

    public static void folder(double x, double y, double size, int color)
    {
        RenderUtils.roundedRect(x + size * 0.1D, y + size * 0.2D, size * 0.35D, size * 0.14D, 1.0D, color);
        RenderUtils.roundedRect(x + size * 0.1D, y + size * 0.3D, size * 0.8D, size * 0.5D, 1.5D, color);
    }

    public static void gear(double x, double y, double size, int color)
    {
        RenderUtils.circle(x + size * 0.5D, y + size * 0.5D, size * 0.32D, color);
        RenderUtils.circle(x + size * 0.5D, y + size * 0.5D, size * 0.14D, Theme.PANEL_ELEVATED);

        for (int i = 0; i < 4; ++i)
        {
            double angle = Math.toRadians((double)i * 90.0D + 45.0D);
            RenderUtils.circle(x + size * 0.5D + Math.cos(angle) * size * 0.34D, y + size * 0.5D + Math.sin(angle) * size * 0.34D, size * 0.08D, color);
        }
    }

    public static void cloud(double x, double y, double size, int color)
    {
        RenderUtils.circle(x + size * 0.35D, y + size * 0.55D, size * 0.2D, color);
        RenderUtils.circle(x + size * 0.6D, y + size * 0.5D, size * 0.25D, color);
        RenderUtils.roundedRect(x + size * 0.2D, y + size * 0.55D, size * 0.6D, size * 0.2D, size * 0.1D, color);
    }

    public static void logo(double x, double y, double size, int color)
    {
        diagonal(x + size * 0.08D, y + size * 0.92D, x + size * 0.5D, y + size * 0.06D, color);
        diagonal(x + size * 0.92D, y + size * 0.92D, x + size * 0.5D, y + size * 0.06D, color);
        RenderUtils.rect(x + size * 0.3D, y + size * 0.6D, size * 0.4D, size * 0.09D, color);
    }

    private static void diagonal(double x1, double y1, double x2, double y2, int color)
    {
        int steps = (int)Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1)) * 2 + 1;

        for (int i = 0; i <= steps; ++i)
        {
            double progress = (double)i / (double)steps;
            RenderUtils.rect(x1 + (x2 - x1) * progress, y1 + (y2 - y1) * progress, 1.0D, 1.0D, color);
        }
    }
}
