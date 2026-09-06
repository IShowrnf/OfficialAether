package net.aether.gui.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.aether.Aether;
import net.aether.config.ConfigManager;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;

public class ConfigsTab extends GuiTab
{
    private static final double ROW_HEIGHT = 26.0D;

    public String getTitle()
    {
        return "Configs";
    }

    public List<String> getSidebarItems()
    {
        return new ArrayList<String>();
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        FontManager fonts = FontManager.getInstance();
        ConfigManager configManager = Aether.getInstance().getConfigManager();
        fonts.large().drawString("CONFIGS", (float)(this.x + 8.0D), (float)(this.y + 8.0D), Theme.ACCENT_LIGHT);
        fonts.small().drawString("Saved in .minecraft/aether/configs", (float)(this.x + 8.0D), (float)(this.y + 21.0D), Theme.TEXT_MUTED);

        double listWidth = this.width - 16.0D;
        double cursor = this.y + 36.0D;

        for (String name : configManager.getConfigNames())
        {
            boolean active = name.equals(configManager.getActiveConfig());
            boolean hovered = Widgets.isHovered(mouseX, mouseY, this.x + 8.0D, cursor, listWidth, ROW_HEIGHT);
            RenderUtils.roundedRect(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
            RenderUtils.roundedRectOutline(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, 1.0F, active ? Theme.BORDER_ACCENT : Theme.BORDER);
            fonts.medium().drawString(name, (float)(this.x + 18.0D), (float)(cursor + 5.0D), Theme.TEXT);
            fonts.small().drawString(active ? "active" : "click to load", (float)(this.x + 18.0D), (float)(cursor + 15.0D), active ? Theme.ACCENT_LIGHT : Theme.TEXT_MUTED);
            Widgets.button(this.x + listWidth - 42.0D, cursor + 6.0D, 44.0D, 14.0D, "LOAD", Widgets.isHovered(mouseX, mouseY, this.x + listWidth - 42.0D, cursor + 6.0D, 44.0D, 14.0D), active);
            cursor += ROW_HEIGHT + 4.0D;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        ConfigManager configManager = Aether.getInstance().getConfigManager();
        double listWidth = this.width - 16.0D;
        double cursor = this.y + 36.0D;

        for (String name : configManager.getConfigNames())
        {
            if (Widgets.isHovered(mouseX, mouseY, this.x + 8.0D, cursor, listWidth, ROW_HEIGHT))
            {
                configManager.load(name);
                return;
            }

            cursor += ROW_HEIGHT + 4.0D;
        }
    }
}
