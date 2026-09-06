package net.aether.gui.clickgui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;
import net.minecraft.client.Minecraft;

public class ScriptsTab extends GuiTab
{
    private static final double ROW_HEIGHT = 24.0D;

    public String getTitle()
    {
        return "Scripts";
    }

    public List<String> getSidebarItems()
    {
        return new ArrayList<String>();
    }

    private List<String> getScripts()
    {
        List<String> scripts = new ArrayList<String>();
        File directory = new File(Minecraft.getMinecraft().mcDataDir, "aether/scripts");

        if (!directory.exists())
        {
            directory.mkdirs();
        }

        File[] files = directory.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    scripts.add(file.getName());
                }
            }
        }

        return scripts;
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        FontManager fonts = FontManager.getInstance();
        fonts.large().drawString("SCRIPTS", (float)(this.x + 8.0D), (float)(this.y + 8.0D), Theme.ACCENT_LIGHT);
        fonts.small().drawString("Drop scripts into .minecraft/aether/scripts", (float)(this.x + 8.0D), (float)(this.y + 21.0D), Theme.TEXT_MUTED);

        List<String> scripts = this.getScripts();
        double listWidth = this.width - 16.0D;
        double cursor = this.y + 36.0D;

        if (scripts.isEmpty())
        {
            Widgets.panel(this.x + 8.0D, cursor, listWidth, 48.0D);
            fonts.regular().drawCenteredString("No scripts loaded", (float)(this.x + 8.0D + listWidth / 2.0D), (float)(cursor + 20.0D), Theme.TEXT_MUTED);
            return;
        }

        for (String script : scripts)
        {
            boolean hovered = Widgets.isHovered(mouseX, mouseY, this.x + 8.0D, cursor, listWidth, ROW_HEIGHT);
            RenderUtils.roundedRect(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
            RenderUtils.roundedRectOutline(this.x + 8.0D, cursor, listWidth, ROW_HEIGHT, 4.0D, 1.0F, Theme.BORDER);
            fonts.medium().drawString(script, (float)(this.x + 18.0D), (float)(cursor + 8.0D), Theme.TEXT);
            cursor += ROW_HEIGHT + 4.0D;
        }
    }
}
