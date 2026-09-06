package net.aether.hud.element;

import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.font.FontManager;
import net.aether.hud.HudElement;
import net.minecraft.client.gui.ScaledResolution;

public class CoordinatesElement extends HudElement
{
    public CoordinatesElement()
    {
        super("Coordinates", 4.0D, 0.0D, true);
    }

    public void render()
    {
        ScaledResolution resolution = new ScaledResolution(mc);
        String text = String.format("XYZ %.0f %.0f %.0f", Double.valueOf(mc.player.posX), Double.valueOf(mc.player.posY), Double.valueOf(mc.player.posZ));
        float width = FontManager.getInstance().medium().getStringWidth(text) + 10.0F;
        double drawY = this.getY() > 0.0D ? this.getY() : (double)resolution.getScaledHeight() - 16.0D;
        RenderUtils.roundedRect(this.getX(), drawY, (double)width, 13.0D, 3.0D, 0xB0000000);
        FontManager.getInstance().medium().drawString(text, (float)(this.getX() + 5.0D), (float)(drawY + 3.0D), Theme.TEXT);
    }
}
