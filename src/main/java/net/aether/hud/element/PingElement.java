package net.aether.hud.element;

import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.font.FontManager;
import net.aether.hud.HudElement;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingElement extends HudElement
{
    public PingElement()
    {
        super("Ping", 0.0D, 0.0D, true);
    }

    public void render()
    {
        ScaledResolution resolution = new ScaledResolution(mc);
        String text = this.getPing() + " ms";
        float width = FontManager.getInstance().medium().getStringWidth(text) + 10.0F;
        double drawX = this.getX() > 0.0D ? this.getX() : (double)resolution.getScaledWidth() - (double)width - 4.0D;
        double drawY = this.getY() > 0.0D ? this.getY() : (double)resolution.getScaledHeight() - 16.0D;
        RenderUtils.roundedRect(drawX, drawY, (double)width, 13.0D, 3.0D, 0xB0000000);
        FontManager.getInstance().medium().drawString(text, (float)(drawX + 5.0D), (float)(drawY + 3.0D), Theme.GREEN);
    }

    private int getPing()
    {
        if (mc.getConnection() == null || mc.player == null)
        {
            return 0;
        }

        NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUniqueID());
        return info == null ? 0 : info.getResponseTime();
    }
}
