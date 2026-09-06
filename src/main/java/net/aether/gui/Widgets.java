package net.aether.gui;

import net.aether.gui.font.FontManager;

public class Widgets
{
    public static boolean isHovered(int mouseX, int mouseY, double x, double y, double width, double height)
    {
        return (double)mouseX >= x && (double)mouseX <= x + width && (double)mouseY >= y && (double)mouseY <= y + height;
    }

    /**
     * Pill toggle switch. animation is 0 (off) to 1 (on).
     */
    public static void toggle(double x, double y, double width, double height, float animation)
    {
        int track = RenderUtils.blend(Theme.TOGGLE_OFF, Theme.ACCENT, animation);
        RenderUtils.roundedRect(x, y, width, height, height / 2.0D, track);

        if (animation > 0.05F)
        {
            RenderUtils.roundedRect(x, y, width, height, height / 2.0D, RenderUtils.withAlpha(Theme.ACCENT_LIGHT, (int)(60.0F * animation)));
        }

        double knobRadius = height / 2.0D - 1.5D;
        double knobX = x + knobRadius + 1.5D + (width - (knobRadius + 1.5D) * 2.0D) * (double)animation;
        RenderUtils.circle(knobX, y + height / 2.0D, knobRadius, RenderUtils.blend(Theme.TOGGLE_KNOB_OFF, Theme.TOGGLE_KNOB, animation));
    }

    public static void slider(double x, double y, double width, double height, double fraction)
    {
        double centerY = y + height / 2.0D;
        RenderUtils.roundedRect(x, centerY - 1.0D, width, 2.0D, 1.0D, Theme.TRACK);
        double filled = width * Math.max(0.0D, Math.min(1.0D, fraction));
        RenderUtils.roundedRect(x, centerY - 1.0D, filled, 2.0D, 1.0D, Theme.ACCENT);
        RenderUtils.circle(x + filled, centerY, 3.5D, Theme.ACCENT_LIGHT);
    }

    public static void dropdown(double x, double y, double width, double height, String value, boolean open, boolean hovered)
    {
        RenderUtils.roundedRect(x, y, width, height, 3.0D, hovered ? Theme.CARD_HOVER : Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(x, y, width, height, 3.0D, 1.0F, open ? Theme.ACCENT : Theme.BORDER);
        FontManager.getInstance().regular().drawString(value, (float)(x + 6.0D), (float)(y + height / 2.0D - FontManager.getInstance().regular().getHeight() / 2.0F + 1.0F), Theme.TEXT);
        chevron(x + width - 9.0D, y + height / 2.0D, open);
    }

    public static void chevron(double centerX, double centerY, boolean flipped)
    {
        double size = 2.0D;

        for (int i = 0; i < 3; ++i)
        {
            double offset = (double)i * 1.4D;
            double dy = flipped ? -offset : offset;
            RenderUtils.rect(centerX - size + (double)i * 1.4D, centerY - 1.0D + dy, 1.0D, 1.0D, Theme.TEXT_SECONDARY);
            RenderUtils.rect(centerX + size - (double)i * 1.4D, centerY - 1.0D + dy, 1.0D, 1.0D, Theme.TEXT_SECONDARY);
        }
    }

    public static void button(double x, double y, double width, double height, String label, boolean hovered, boolean primary)
    {
        int background = primary ? (hovered ? Theme.ACCENT_LIGHT : Theme.ACCENT) : (hovered ? Theme.CARD_HOVER : Theme.PANEL_ELEVATED);
        RenderUtils.roundedRect(x, y, width, height, 3.0D, background);

        if (!primary)
        {
            RenderUtils.roundedRectOutline(x, y, width, height, 3.0D, 1.0F, Theme.BORDER);
        }

        FontManager.getInstance().medium().drawCenteredString(label, (float)(x + width / 2.0D), (float)(y + height / 2.0D - FontManager.getInstance().medium().getHeight() / 2.0F + 1.0F), primary ? Theme.TEXT : Theme.TEXT_SECONDARY);
    }

    public static void panel(double x, double y, double width, double height)
    {
        RenderUtils.roundedRect(x, y, width, height, 5.0D, Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(x, y, width, height, 5.0D, 1.0F, Theme.BORDER);
    }

    public static float approach(float current, float target, float speed)
    {
        if (Math.abs(target - current) < 0.01F)
        {
            return target;
        }

        return current + (target - current) * speed;
    }
}
