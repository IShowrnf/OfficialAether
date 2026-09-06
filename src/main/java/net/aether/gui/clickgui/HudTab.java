package net.aether.gui.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.aether.Aether;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;
import net.aether.hud.HudElement;

public class HudTab extends GuiTab
{
    private static final double ROW_HEIGHT = 24.0D;

    private final Map<HudElement, Float> animations = new HashMap<HudElement, Float>();

    private HudElement dragged;
    private double dragOffsetX;
    private double dragOffsetY;

    public String getTitle()
    {
        return "Hud";
    }

    public List<String> getSidebarItems()
    {
        return new ArrayList<String>();
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        FontManager fonts = FontManager.getInstance();
        double listWidth = Math.min(200.0D, this.width * 0.4D);
        fonts.large().drawString("HUD ELEMENTS", (float)(this.x + 8.0D), (float)(this.y + 8.0D), Theme.ACCENT_LIGHT);
        fonts.small().drawString("Toggle elements on the left, drag them in the preview on the right.", (float)(this.x + 8.0D), (float)(this.y + 21.0D), Theme.TEXT_MUTED);

        double cursor = this.y + 36.0D;

        for (HudElement element : Aether.getInstance().getHudManager().getElements())
        {
            boolean hovered = Widgets.isHovered(mouseX, mouseY, this.x + 8.0D, cursor, listWidth, ROW_HEIGHT);
            RenderUtils.roundedRect(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
            RenderUtils.roundedRectOutline(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, 1.0F, element.isEnabled() ? Theme.BORDER_ACCENT : Theme.BORDER);
            fonts.medium().drawString(element.getName(), (float)(this.x + 16.0D), (float)(cursor + 4.0D), Theme.TEXT);
            fonts.small().drawString("x " + (int)element.getX() + "  y " + (int)element.getY(), (float)(this.x + 16.0D), (float)(cursor + 14.0D), Theme.TEXT_MUTED);

            float current = this.animations.containsKey(element) ? this.animations.get(element).floatValue() : (element.isEnabled() ? 1.0F : 0.0F);
            float next = Widgets.approach(current, element.isEnabled() ? 1.0F : 0.0F, 0.25F);
            this.animations.put(element, Float.valueOf(next));
            Widgets.toggle(this.x + listWidth - 18.0D, cursor + 7.0D, 20.0D, 11.0D, next);
            cursor += ROW_HEIGHT + 4.0D;
        }

        double previewX = this.x + listWidth + 24.0D;
        double previewWidth = this.x + this.width - previewX - 8.0D;
        double previewHeight = this.height - 44.0D;
        Widgets.panel(previewX, this.y + 36.0D, previewWidth, previewHeight);
        fonts.small().drawString("PREVIEW", (float)(previewX + 6.0D), (float)(this.y + 40.0D), Theme.TEXT_MUTED);
        this.renderPreview(mouseX, mouseY, previewX, this.y + 36.0D, previewWidth, previewHeight);
    }

    private void renderPreview(int mouseX, int mouseY, double previewX, double previewY, double previewWidth, double previewHeight)
    {
        FontManager fonts = FontManager.getInstance();
        double scaleX = previewWidth / (double)mc.displayWidth * 2.0D;
        double scaleY = previewHeight / (double)mc.displayHeight * 2.0D;

        for (HudElement element : Aether.getInstance().getHudManager().getElements())
        {
            if (!element.isEnabled())
            {
                continue;
            }

            double drawX = previewX + element.getX() * scaleX;
            double drawY = previewY + element.getY() * scaleY;
            double boxWidth = Math.max(28.0D, (double)fonts.small().getStringWidth(element.getName()) + 8.0D);
            RenderUtils.roundedRect(drawX, drawY, boxWidth, 11.0D, 2.0D, Theme.ACCENT_FAINT);
            RenderUtils.roundedRectOutline(drawX, drawY, boxWidth, 11.0D, 2.0D, 1.0F, Theme.ACCENT);
            fonts.small().drawString(element.getName(), (float)(drawX + 4.0D), (float)(drawY + 2.0D), Theme.TEXT);

            if (this.dragged == element)
            {
                element.setPosition((((double)mouseX - this.dragOffsetX) - previewX) / scaleX, (((double)mouseY - this.dragOffsetY) - previewY) / scaleY);
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        FontManager fonts = FontManager.getInstance();
        double listWidth = Math.min(200.0D, this.width * 0.4D);
        double cursor = this.y + 36.0D;

        for (HudElement element : Aether.getInstance().getHudManager().getElements())
        {
            if (Widgets.isHovered(mouseX, mouseY, this.x + 8.0D, cursor, listWidth, ROW_HEIGHT))
            {
                element.setEnabled(!element.isEnabled());
                return;
            }

            cursor += ROW_HEIGHT + 4.0D;
        }

        double previewX = this.x + listWidth + 24.0D;
        double previewWidth = this.x + this.width - previewX - 8.0D;
        double previewHeight = this.height - 44.0D;
        double scaleX = previewWidth / (double)mc.displayWidth * 2.0D;
        double scaleY = previewHeight / (double)mc.displayHeight * 2.0D;

        for (HudElement element : Aether.getInstance().getHudManager().getElements())
        {
            if (!element.isEnabled())
            {
                continue;
            }

            double drawX = previewX + element.getX() * scaleX;
            double drawY = this.y + 36.0D + element.getY() * scaleY;
            double boxWidth = Math.max(28.0D, (double)fonts.small().getStringWidth(element.getName()) + 8.0D);

            if (Widgets.isHovered(mouseX, mouseY, drawX, drawY, boxWidth, 11.0D))
            {
                this.dragged = element;
                this.dragOffsetX = (double)mouseX - drawX;
                this.dragOffsetY = (double)mouseY - drawY;
                return;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        this.dragged = null;
    }

    public void onClose()
    {
        this.dragged = null;
    }
}
