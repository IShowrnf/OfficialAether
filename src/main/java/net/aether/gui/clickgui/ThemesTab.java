package net.aether.gui.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;

public class ThemesTab extends GuiTab
{
    private static final String[] NAMES = new String[] {"Aether", "Nebula", "Ember", "Mint", "Ice", "Rose"};
    private static final int[] COLORS = new int[] {0xFF8B5CF6, 0xFF6366F1, 0xFFF97316, 0xFF10B981, 0xFF38BDF8, 0xFFF43F5E};

    private static int selected;

    public String getTitle()
    {
        return "Themes";
    }

    public List<String> getSidebarItems()
    {
        return new ArrayList<String>();
    }

    public static int accent()
    {
        return COLORS[selected];
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        FontManager fonts = FontManager.getInstance();
        fonts.large().drawString("THEMES", (float)(this.x + 8.0D), (float)(this.y + 8.0D), Theme.ACCENT_LIGHT);
        fonts.small().drawString("Accent colour used across the interface and HUD.", (float)(this.x + 8.0D), (float)(this.y + 21.0D), Theme.TEXT_MUTED);

        double cardWidth = 88.0D;
        double cardHeight = 52.0D;
        double perRow = Math.max(1.0D, Math.floor((this.width - 16.0D) / (cardWidth + 8.0D)));

        for (int i = 0; i < NAMES.length; ++i)
        {
            double column = (double)(i % (int)perRow);
            double row = Math.floor((double)i / perRow);
            double cardX = this.x + 8.0D + column * (cardWidth + 8.0D);
            double cardY = this.y + 36.0D + row * (cardHeight + 8.0D);
            boolean hovered = Widgets.isHovered(mouseX, mouseY, cardX, cardY, cardWidth, cardHeight);
            RenderUtils.roundedRect(cardX, cardY, cardWidth, cardHeight, 5.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
            RenderUtils.roundedRectOutline(cardX, cardY, cardWidth, cardHeight, 5.0D, 1.0F, i == selected ? COLORS[i] : Theme.BORDER);
            RenderUtils.gradientRectHorizontal(cardX + 8.0D, cardY + 10.0D, cardWidth - 16.0D, 14.0D, COLORS[i], RenderUtils.withAlpha(COLORS[i], 40));
            fonts.medium().drawString(NAMES[i], (float)(cardX + 8.0D), (float)(cardY + 30.0D), Theme.TEXT);
            fonts.small().drawString(i == selected ? "selected" : "click to apply", (float)(cardX + 8.0D), (float)(cardY + 40.0D), i == selected ? Theme.ACCENT_LIGHT : Theme.TEXT_MUTED);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        double cardWidth = 88.0D;
        double cardHeight = 52.0D;
        double perRow = Math.max(1.0D, Math.floor((this.width - 16.0D) / (cardWidth + 8.0D)));

        for (int i = 0; i < NAMES.length; ++i)
        {
            double column = (double)(i % (int)perRow);
            double row = Math.floor((double)i / perRow);
            double cardX = this.x + 8.0D + column * (cardWidth + 8.0D);
            double cardY = this.y + 36.0D + row * (cardHeight + 8.0D);

            if (Widgets.isHovered(mouseX, mouseY, cardX, cardY, cardWidth, cardHeight))
            {
                selected = i;
                return;
            }
        }
    }
}
