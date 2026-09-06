package net.aether.gui.clickgui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.aether.Aether;
import net.aether.gui.Icons;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;
import net.aether.gui.font.TrueTypeFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ClickGui extends GuiScreen
{
    public static final double TITLE_BAR_HEIGHT = 26.0D;
    public static final double BOTTOM_BAR_HEIGHT = 24.0D;
    public static final double SIDEBAR_WIDTH = 82.0D;
    public static final double PADDING = 8.0D;

    private static int activeTab;
    private static int selectedConfigIndex;

    private final List<GuiTab> tabs = new ArrayList<GuiTab>();

    private double windowX;
    private double windowY;
    private double windowWidth;
    private double windowHeight;
    private boolean configDropdownOpen;

    public ClickGui()
    {
        this.tabs.add(new ModsTab());
        this.tabs.add(new HudTab());
        this.tabs.add(new ConfigsTab());
        this.tabs.add(new ScriptsTab());
        this.tabs.add(new ThemesTab());
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        this.currentTab().onClose();
        Aether.getInstance().getConfigManager().save(Aether.getInstance().getConfigManager().getActiveConfig());
    }

    private GuiTab currentTab()
    {
        return this.tabs.get(activeTab);
    }

    private void layout()
    {
        this.windowWidth = Math.min((double)this.width - 20.0D, Math.max(420.0D, (double)this.width * 0.92D));
        this.windowHeight = Math.min((double)this.height - 16.0D, Math.max(240.0D, (double)this.height * 0.9D));
        this.windowX = ((double)this.width - this.windowWidth) / 2.0D;
        this.windowY = ((double)this.height - this.windowHeight) / 2.0D;

        double sidebar = this.currentTab().getSidebarItems().isEmpty() ? 0.0D : SIDEBAR_WIDTH;
        double bodyX = this.windowX + sidebar;
        double bodyY = this.windowY + TITLE_BAR_HEIGHT;
        double bodyWidth = this.windowWidth - sidebar;
        double bodyHeight = this.windowHeight - TITLE_BAR_HEIGHT - BOTTOM_BAR_HEIGHT;
        this.currentTab().setBounds(bodyX, bodyY, bodyWidth, bodyHeight);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.layout();
        RenderUtils.rect(0.0D, 0.0D, (double)this.width, (double)this.height, 0xC0000000);
        RenderUtils.roundedRect(this.windowX, this.windowY, this.windowWidth, this.windowHeight, 6.0D, Theme.WINDOW_BACKGROUND);
        RenderUtils.roundedRectOutline(this.windowX, this.windowY, this.windowWidth, this.windowHeight, 6.0D, 1.0F, Theme.BORDER);

        this.drawTitleBar(mouseX, mouseY);
        this.drawSidebar(mouseX, mouseY);
        this.currentTab().render(mouseX, mouseY, partialTicks);
        this.drawBottomBar(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTitleBar(int mouseX, int mouseY)
    {
        FontManager fonts = FontManager.getInstance();
        double barBottom = this.windowY + TITLE_BAR_HEIGHT;
        RenderUtils.rect(this.windowX + 1.0D, barBottom - 1.0D, this.windowWidth - 2.0D, 1.0D, Theme.BORDER);

        Icons.logo(this.windowX + PADDING, this.windowY + 6.0D, 14.0D, Theme.ACCENT);
        fonts.title().drawString("AETHER", (float)(this.windowX + PADDING + 18.0D), (float)(this.windowY + 5.0D), Theme.TEXT);
        fonts.small().drawString("CLIENT", (float)(this.windowX + PADDING + 19.0D), (float)(this.windowY + 15.0D), Theme.TEXT_MUTED);

        double totalWidth = 0.0D;

        for (GuiTab tab : this.tabs)
        {
            totalWidth += fonts.medium().getStringWidth(tab.getTitle().toUpperCase()) + 22.0D;
        }

        double cursor = this.windowX + (this.windowWidth - totalWidth) / 2.0D;

        for (int i = 0; i < this.tabs.size(); ++i)
        {
            String label = this.tabs.get(i).getTitle().toUpperCase();
            double labelWidth = fonts.medium().getStringWidth(label);
            double tabWidth = labelWidth + 22.0D;
            boolean hovered = Widgets.isHovered(mouseX, mouseY, cursor, this.windowY, tabWidth, TITLE_BAR_HEIGHT);
            boolean selected = i == activeTab;
            int color = selected ? Theme.TEXT : (hovered ? Theme.TEXT_SECONDARY : Theme.TEXT_MUTED);
            fonts.medium().drawString(label, (float)(cursor + 11.0D), (float)(this.windowY + 9.0D), color);

            if (selected)
            {
                RenderUtils.roundedRect(cursor + 8.0D, barBottom - 2.0D, labelWidth + 6.0D, 2.0D, 1.0D, Theme.ACCENT);
            }

            cursor += tabWidth;
        }

        double closeX = this.windowX + this.windowWidth - 22.0D;
        double minimizeX = closeX - 20.0D;
        this.drawWindowButton(minimizeX, this.windowY + 7.0D, mouseX, mouseY, false);
        this.drawWindowButton(closeX, this.windowY + 7.0D, mouseX, mouseY, true);
    }

    private void drawWindowButton(double x, double y, int mouseX, int mouseY, boolean close)
    {
        boolean hovered = Widgets.isHovered(mouseX, mouseY, x - 4.0D, y - 4.0D, 20.0D, 20.0D);
        int color = hovered ? Theme.TEXT : Theme.TEXT_SECONDARY;

        if (close)
        {
            Icons.swords(x, y, 11.0D, color);
        }
        else
        {
            RenderUtils.rect(x + 1.0D, y + 5.5D, 9.0D, 1.0D, color);
        }
    }

    private void drawSidebar(int mouseX, int mouseY)
    {
        List<String> items = this.currentTab().getSidebarItems();

        if (items.isEmpty())
        {
            return;
        }

        FontManager fonts = FontManager.getInstance();
        double top = this.windowY + TITLE_BAR_HEIGHT;
        double bottom = this.windowY + this.windowHeight - BOTTOM_BAR_HEIGHT;
        RenderUtils.rect(this.windowX + SIDEBAR_WIDTH - 1.0D, top, 1.0D, bottom - top, Theme.BORDER);

        double itemHeight = 22.0D;
        double cursor = top + PADDING;

        for (int i = 0; i < items.size(); ++i)
        {
            boolean selected = i == this.currentTab().getSelectedSidebarIndex();
            boolean hovered = Widgets.isHovered(mouseX, mouseY, this.windowX + 6.0D, cursor, SIDEBAR_WIDTH - 14.0D, itemHeight);

            if (selected)
            {
                RenderUtils.roundedRect(this.windowX + 6.0D, cursor, SIDEBAR_WIDTH - 14.0D, itemHeight, 4.0D, Theme.ACCENT_FAINT);
                RenderUtils.roundedRectOutline(this.windowX + 6.0D, cursor, SIDEBAR_WIDTH - 14.0D, itemHeight, 4.0D, 1.0F, RenderUtils.withAlpha(Theme.ACCENT, 90));
            }
            else if (hovered)
            {
                RenderUtils.roundedRect(this.windowX + 6.0D, cursor, SIDEBAR_WIDTH - 14.0D, itemHeight, 4.0D, Theme.CARD);
            }

            int color = selected ? Theme.ACCENT_LIGHT : (hovered ? Theme.TEXT : Theme.TEXT_SECONDARY);
            this.currentTab().renderSidebarIcon(i, this.windowX + 13.0D, cursor + 6.0D, 10.0D, color);
            fonts.medium().drawString(items.get(i).toUpperCase(), (float)(this.windowX + 28.0D), (float)(cursor + itemHeight / 2.0D - fonts.medium().getHeight() / 2.0F), color);
            cursor += itemHeight + 3.0D;
        }

        double footerY = bottom - 40.0D;
        RenderUtils.rect(this.windowX + 8.0D, footerY - 6.0D, SIDEBAR_WIDTH - 18.0D, 1.0D, Theme.BORDER);
        fonts.medium().drawString("AETHER CLIENT", (float)(this.windowX + 8.0D), (float)footerY, Theme.TEXT);
        fonts.small().drawString("v" + Aether.VERSION, (float)(this.windowX + 8.0D), (float)(footerY + 9.0D), Theme.TEXT_MUTED);
        RenderUtils.circle(this.windowX + 11.0D, footerY + 22.0D, 2.0D, Theme.GREEN);
        fonts.small().drawString(this.getServerName(), (float)(this.windowX + 16.0D), (float)(footerY + 18.0D), Theme.TEXT_SECONDARY);
        fonts.medium().drawString(Minecraft.getDebugFPS() + " FPS", (float)(this.windowX + 8.0D), (float)(footerY + 29.0D), Theme.ACCENT_LIGHT);
    }

    private String getServerName()
    {
        if (mc.getCurrentServerData() != null)
        {
            return mc.getCurrentServerData().serverIP;
        }

        return mc.isSingleplayer() ? "singleplayer" : "main menu";
    }

    private void drawBottomBar(int mouseX, int mouseY)
    {
        FontManager fonts = FontManager.getInstance();
        double barY = this.windowY + this.windowHeight - BOTTOM_BAR_HEIGHT;
        RenderUtils.rect(this.windowX + 1.0D, barY, this.windowWidth - 2.0D, 1.0D, Theme.BORDER);

        fonts.medium().drawString("CONFIG", (float)(this.windowX + PADDING), (float)(barY + 9.0D), Theme.TEXT_SECONDARY);

        List<String> configs = Aether.getInstance().getConfigManager().getConfigNames();
        selectedConfigIndex = Math.max(0, Math.min(configs.size() - 1, selectedConfigIndex));
        double dropdownX = this.windowX + PADDING + 34.0D;
        Widgets.dropdown(dropdownX, barY + 5.0D, 74.0D, 14.0D, configs.isEmpty() ? "Default" : configs.get(selectedConfigIndex), this.configDropdownOpen, Widgets.isHovered(mouseX, mouseY, dropdownX, barY + 5.0D, 74.0D, 14.0D));

        double buttonY = barY + 5.0D;
        double cursor = dropdownX + 80.0D;
        String[] actions = new String[] {"NEW", "SAVE", "DELETE"};

        for (String action : actions)
        {
            double buttonWidth = fonts.medium().getStringWidth(action) + 16.0D;
            Widgets.button(cursor, buttonY, buttonWidth, 14.0D, action, Widgets.isHovered(mouseX, mouseY, cursor, buttonY, buttonWidth, 14.0D), false);
            cursor += buttonWidth + 5.0D;
        }

        double loadWidth = 68.0D;
        double loadX = this.windowX + this.windowWidth - PADDING - loadWidth;
        Widgets.button(loadX, buttonY, loadWidth, 14.0D, "LOAD CONFIG", Widgets.isHovered(mouseX, mouseY, loadX, buttonY, loadWidth, 14.0D), true);

        if (this.configDropdownOpen)
        {
            double listY = barY + 5.0D - (double)configs.size() * 13.0D - 2.0D;
            RenderUtils.roundedRect(dropdownX, listY, 74.0D, (double)configs.size() * 13.0D, 3.0D, Theme.PANEL_ELEVATED);
            RenderUtils.roundedRectOutline(dropdownX, listY, 74.0D, (double)configs.size() * 13.0D, 3.0D, 1.0F, Theme.BORDER);

            for (int i = 0; i < configs.size(); ++i)
            {
                double entryY = listY + (double)i * 13.0D;
                boolean hovered = Widgets.isHovered(mouseX, mouseY, dropdownX, entryY, 74.0D, 13.0D);

                if (hovered)
                {
                    RenderUtils.rect(dropdownX + 1.0D, entryY, 72.0D, 13.0D, Theme.CARD_HOVER);
                }

                fonts.regular().drawString(configs.get(i), (float)(dropdownX + 6.0D), (float)(entryY + 3.0D), i == selectedConfigIndex ? Theme.ACCENT_LIGHT : Theme.TEXT_SECONDARY);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        this.layout();

        if (this.handleBottomBarClick(mouseX, mouseY))
        {
            return;
        }

        if (this.handleTitleBarClick(mouseX, mouseY))
        {
            return;
        }

        if (this.handleSidebarClick(mouseX, mouseY))
        {
            return;
        }

        this.currentTab().mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean handleTitleBarClick(int mouseX, int mouseY)
    {
        if (!Widgets.isHovered(mouseX, mouseY, this.windowX, this.windowY, this.windowWidth, TITLE_BAR_HEIGHT))
        {
            return false;
        }

        FontManager fonts = FontManager.getInstance();
        double closeX = this.windowX + this.windowWidth - 22.0D;

        if (Widgets.isHovered(mouseX, mouseY, closeX - 4.0D, this.windowY + 3.0D, 20.0D, 20.0D))
        {
            mc.displayGuiScreen((GuiScreen)null);
            return true;
        }

        if (Widgets.isHovered(mouseX, mouseY, closeX - 24.0D, this.windowY + 3.0D, 20.0D, 20.0D))
        {
            mc.displayGuiScreen((GuiScreen)null);
            return true;
        }

        double totalWidth = 0.0D;

        for (GuiTab tab : this.tabs)
        {
            totalWidth += fonts.medium().getStringWidth(tab.getTitle().toUpperCase()) + 22.0D;
        }

        double cursor = this.windowX + (this.windowWidth - totalWidth) / 2.0D;

        for (int i = 0; i < this.tabs.size(); ++i)
        {
            double tabWidth = fonts.medium().getStringWidth(this.tabs.get(i).getTitle().toUpperCase()) + 22.0D;

            if (Widgets.isHovered(mouseX, mouseY, cursor, this.windowY, tabWidth, TITLE_BAR_HEIGHT))
            {
                activeTab = i;
                return true;
            }

            cursor += tabWidth;
        }

        return true;
    }

    private boolean handleSidebarClick(int mouseX, int mouseY)
    {
        List<String> items = this.currentTab().getSidebarItems();

        if (items.isEmpty() || (double)mouseX > this.windowX + SIDEBAR_WIDTH)
        {
            return false;
        }

        double cursor = this.windowY + TITLE_BAR_HEIGHT + PADDING;

        for (int i = 0; i < items.size(); ++i)
        {
            if (Widgets.isHovered(mouseX, mouseY, this.windowX + 6.0D, cursor, SIDEBAR_WIDTH - 14.0D, 22.0D))
            {
                this.currentTab().onSidebarSelected(i);
                return true;
            }

            cursor += 25.0D;
        }

        return true;
    }

    private boolean handleBottomBarClick(int mouseX, int mouseY)
    {
        double barY = this.windowY + this.windowHeight - BOTTOM_BAR_HEIGHT;
        List<String> configs = Aether.getInstance().getConfigManager().getConfigNames();
        double dropdownX = this.windowX + PADDING + 34.0D;

        if (this.configDropdownOpen)
        {
            double listY = barY + 5.0D - (double)configs.size() * 13.0D - 2.0D;

            for (int i = 0; i < configs.size(); ++i)
            {
                if (Widgets.isHovered(mouseX, mouseY, dropdownX, listY + (double)i * 13.0D, 74.0D, 13.0D))
                {
                    selectedConfigIndex = i;
                    this.configDropdownOpen = false;
                    return true;
                }
            }

            this.configDropdownOpen = false;
        }

        if (!Widgets.isHovered(mouseX, mouseY, this.windowX, barY, this.windowWidth, BOTTOM_BAR_HEIGHT))
        {
            return false;
        }

        if (Widgets.isHovered(mouseX, mouseY, dropdownX, barY + 5.0D, 74.0D, 14.0D))
        {
            this.configDropdownOpen = true;
            return true;
        }

        FontManager fonts = FontManager.getInstance();
        double cursor = dropdownX + 80.0D;
        String[] actions = new String[] {"NEW", "SAVE", "DELETE"};

        for (String action : actions)
        {
            double buttonWidth = fonts.medium().getStringWidth(action) + 16.0D;

            if (Widgets.isHovered(mouseX, mouseY, cursor, barY + 5.0D, buttonWidth, 14.0D))
            {
                this.runConfigAction(action, configs);
                return true;
            }

            cursor += buttonWidth + 5.0D;
        }

        double loadWidth = 68.0D;
        double loadX = this.windowX + this.windowWidth - PADDING - loadWidth;

        if (Widgets.isHovered(mouseX, mouseY, loadX, barY + 5.0D, loadWidth, 14.0D) && !configs.isEmpty())
        {
            Aether.getInstance().getConfigManager().load(configs.get(selectedConfigIndex));
            return true;
        }

        return true;
    }

    private void runConfigAction(String action, List<String> configs)
    {
        net.aether.config.ConfigManager configManager = Aether.getInstance().getConfigManager();

        if ("NEW".equals(action))
        {
            configManager.create("Config " + (configs.size() + 1));
        }
        else if ("SAVE".equals(action))
        {
            configManager.save(configs.isEmpty() ? "Default" : configs.get(selectedConfigIndex));
        }
        else if ("DELETE".equals(action) && !configs.isEmpty())
        {
            configManager.delete(configs.get(selectedConfigIndex));
            selectedConfigIndex = 0;
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        this.currentTab().mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.currentTab().keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();

        if (scroll != 0)
        {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.currentTab().scroll(scroll > 0 ? 1 : -1, mouseX, mouseY);
        }
    }

    static TrueTypeFont font()
    {
        return FontManager.getInstance().regular();
    }
}
