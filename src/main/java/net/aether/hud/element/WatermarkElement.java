package net.aether.hud.element;

import net.aether.Aether;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.font.FontManager;
import net.aether.hud.HudElement;
import net.minecraft.client.Minecraft;

public class WatermarkElement extends HudElement
{
    public WatermarkElement()
    {
        super("Watermark", 4.0D, 4.0D, true);
    }

    public void render()
    {
        FontManager fonts = FontManager.getInstance();
        String label = "Aether";
        String version = " v" + Aether.VERSION + " | " + Minecraft.getDebugFPS() + " fps";
        float width = fonts.medium().getStringWidth(label) + fonts.small().getStringWidth(version) + 12.0F;
        RenderUtils.roundedRect(this.getX(), this.getY(), (double)width, 14.0D, 3.0D, 0xB0000000);
        RenderUtils.roundedRect(this.getX(), this.getY(), 2.0D, 14.0D, 1.0D, Theme.ACCENT);
        float cursor = fonts.medium().drawString(label, (float)(this.getX() + 6.0D), (float)(this.getY() + 3.0D), Theme.ACCENT_LIGHT);
        fonts.small().drawString(version, cursor, (float)(this.getY() + 4.0D), Theme.TEXT_SECONDARY);
    }
}
