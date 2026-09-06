package net.aether.gui.clickgui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.aether.Aether;
import net.aether.gui.Icons;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;
import net.aether.module.Category;
import net.aether.module.Module;
import net.aether.setting.BooleanSetting;
import net.aether.setting.ModeSetting;
import net.aether.setting.NumberSetting;
import net.aether.setting.Setting;
import org.lwjgl.input.Keyboard;

public class ModsTab extends GuiTab
{
    private static final double CARD_HEIGHT = 28.0D;
    private static final double ROW_HEIGHT = 19.0D;
    private static final double INFO_WIDTH = 112.0D;

    private final Map<Module, Float> toggleAnimations = new HashMap<Module, Float>();
    private final Map<Setting, Float> settingAnimations = new HashMap<Setting, Float>();

    private Category category = Category.COMBAT;
    private Module selected;
    private String search = "";
    private boolean searchFocused;
    private boolean sortAlphabetically;
    private double listScroll;
    private double settingsScroll;
    private ModeSetting openDropdown;
    private NumberSetting draggedSlider;
    private double draggedSliderX;
    private double draggedSliderWidth;
    private Module bindingModule;

    public String getTitle()
    {
        return "Mods";
    }

    public List<String> getSidebarItems()
    {
        List<String> items = new ArrayList<String>();

        for (Category value : Category.values())
        {
            items.add(value.getDisplayName());
        }

        return items;
    }

    public int getSelectedSidebarIndex()
    {
        return this.category.ordinal();
    }

    public void onSidebarSelected(int index)
    {
        this.category = Category.values()[index];
        this.listScroll = 0.0D;
        this.selected = null;
    }

    public void renderSidebarIcon(int index, double x, double y, double size, int color)
    {
        Icons.category(Category.values()[index], x, y, size, color);
    }

    private List<Module> visibleModules()
    {
        List<Module> modules = new ArrayList<Module>();

        for (Module module : Aether.getInstance().getModuleManager().getModules(this.category))
        {
            if (this.search.isEmpty() || module.getName().toLowerCase().contains(this.search.toLowerCase()))
            {
                modules.add(module);
            }
        }

        if (this.sortAlphabetically)
        {
            modules.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        }

        return modules;
    }

    private double listWidth()
    {
        return Math.max(120.0D, Math.min(170.0D, (this.width - INFO_WIDTH) * 0.42D));
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.updateDrag(mouseX);
        List<Module> modules = this.visibleModules();

        if (this.selected == null || this.selected.getCategory() != this.category)
        {
            this.selected = modules.isEmpty() ? null : modules.get(0);
        }

        double listWidth = this.listWidth();
        this.renderSearchRow(mouseX, mouseY, listWidth);
        this.renderModuleList(mouseX, mouseY, modules, listWidth);

        double panelX = this.x + listWidth + 10.0D;
        double panelWidth = this.width - listWidth - INFO_WIDTH - 22.0D;
        RenderUtils.rect(panelX - 5.0D, this.y + 8.0D, 1.0D, this.height - 16.0D, Theme.BORDER);
        this.renderSettingsPanel(mouseX, mouseY, panelX, panelWidth);
        this.renderInfoColumn(mouseX, mouseY);
    }

    private void renderSearchRow(int mouseX, int mouseY, double listWidth)
    {
        FontManager fonts = FontManager.getInstance();
        double rowY = this.y + 8.0D;
        double sortWidth = 38.0D;
        double searchWidth = listWidth - sortWidth - 5.0D;

        RenderUtils.roundedRect(this.x, rowY, searchWidth, 16.0D, 4.0D, Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(this.x, rowY, searchWidth, 16.0D, 4.0D, 1.0F, this.searchFocused ? Theme.ACCENT : Theme.BORDER);
        Icons.search(this.x + 5.0D, rowY + 4.0D, 8.0D, Theme.TEXT_MUTED);
        String text = this.search.isEmpty() && !this.searchFocused ? "Search modules..." : this.search + (this.searchFocused ? "_" : "");
        fonts.regular().drawString(text, (float)(this.x + 17.0D), (float)(rowY + 4.0D), this.search.isEmpty() ? Theme.TEXT_MUTED : Theme.TEXT);

        double sortX = this.x + searchWidth + 5.0D;
        boolean sortHovered = Widgets.isHovered(mouseX, mouseY, sortX, rowY, sortWidth, 16.0D);
        RenderUtils.roundedRect(sortX, rowY, sortWidth, 16.0D, 4.0D, sortHovered ? Theme.CARD_HOVER : Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(sortX, rowY, sortWidth, 16.0D, 4.0D, 1.0F, Theme.BORDER);
        fonts.regular().drawString("Sort", (float)(sortX + 7.0D), (float)(rowY + 4.0D), Theme.TEXT_SECONDARY);
        Widgets.chevron(sortX + sortWidth - 8.0D, rowY + 8.0D, this.sortAlphabetically);
    }

    private void renderModuleList(int mouseX, int mouseY, List<Module> modules, double listWidth)
    {
        FontManager fonts = FontManager.getInstance();
        double top = this.y + 30.0D;
        double bottom = this.y + this.height - 4.0D;
        RenderUtils.beginScissor(this.x, top, listWidth, bottom - top);
        double cursor = top - this.listScroll;

        for (Module module : modules)
        {
            if (cursor + CARD_HEIGHT >= top - CARD_HEIGHT && cursor <= bottom)
            {
                boolean hovered = Widgets.isHovered(mouseX, mouseY, this.x, cursor, listWidth, CARD_HEIGHT) && mouseY >= (int)top && mouseY <= (int)bottom;
                boolean isSelected = module == this.selected;
                int background = isSelected ? Theme.CARD_SELECTED : (hovered ? Theme.CARD_HOVER : Theme.CARD);
                RenderUtils.roundedRect(this.x, cursor, listWidth, CARD_HEIGHT, 4.0D, background);
                RenderUtils.roundedRectOutline(this.x, cursor, listWidth, CARD_HEIGHT, 4.0D, 1.0F, isSelected ? Theme.BORDER_ACCENT : Theme.BORDER);

                int iconColor = module.isEnabled() ? Theme.ACCENT_LIGHT : Theme.TEXT_MUTED;
                Icons.category(module.getCategory(), this.x + 6.0D, cursor + 9.0D, 11.0D, iconColor);
                fonts.medium().drawString(module.getName(), (float)(this.x + 22.0D), (float)(cursor + 6.0D), Theme.TEXT);
                fonts.small().drawString(fonts.small().trimToWidth(module.getDescription(), (float)(listWidth - 60.0D)), (float)(this.x + 22.0D), (float)(cursor + 16.0D), Theme.TEXT_MUTED);

                float animation = this.animate(this.toggleAnimations, module, module.isEnabled());
                Widgets.toggle(this.x + listWidth - 26.0D, cursor + 9.0D, 20.0D, 11.0D, animation);
            }

            cursor += CARD_HEIGHT + 4.0D;
        }

        RenderUtils.endScissor();
    }

    private void renderSettingsPanel(int mouseX, int mouseY, double panelX, double panelWidth)
    {
        FontManager fonts = FontManager.getInstance();

        if (this.selected == null)
        {
            fonts.regular().drawString("No modules in this category.", (float)panelX, (float)(this.y + 12.0D), Theme.TEXT_MUTED);
            return;
        }

        double top = this.y + 8.0D;
        fonts.large().drawString(this.selected.getName(), (float)panelX, (float)top, Theme.ACCENT_LIGHT);
        float headerAnimation = this.animate(this.toggleAnimations, this.selected, this.selected.isEnabled());
        Widgets.toggle(panelX + panelWidth - 24.0D, top, 22.0D, 12.0D, headerAnimation);
        fonts.regular().drawString(this.selected.getDescription(), (float)panelX, (float)(top + 14.0D), Theme.TEXT_SECONDARY);

        double contentTop = top + 28.0D;
        double bottom = this.y + this.height - 4.0D;
        RenderUtils.beginScissor(panelX, contentTop, panelWidth, bottom - contentTop);
        double cursor = contentTop - this.settingsScroll;
        String group = null;

        for (Setting setting : this.selected.getSettings())
        {
            if (!setting.getGroup().equals(group))
            {
                group = setting.getGroup();
                fonts.medium().drawString(group, (float)panelX, (float)(cursor + 2.0D), Theme.ACCENT);
                cursor += 15.0D;
            }

            this.renderSettingRow(mouseX, mouseY, setting, panelX, cursor, panelWidth);
            cursor += ROW_HEIGHT + 3.0D;
        }

        RenderUtils.endScissor();
        this.renderOpenDropdown(mouseX, mouseY, panelX, panelWidth, contentTop);
    }

    private void renderSettingRow(int mouseX, int mouseY, Setting setting, double panelX, double rowY, double panelWidth)
    {
        FontManager fonts = FontManager.getInstance();
        boolean hovered = Widgets.isHovered(mouseX, mouseY, panelX, rowY, panelWidth, ROW_HEIGHT);
        RenderUtils.roundedRect(panelX, rowY, panelWidth, ROW_HEIGHT, 4.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
        float textY = (float)(rowY + ROW_HEIGHT / 2.0D - (double)fonts.regular().getHeight() / 2.0D);
        fonts.regular().drawString(setting.getName(), (float)(panelX + 8.0D), textY, Theme.TEXT);

        if (setting instanceof NumberSetting)
        {
            NumberSetting number = (NumberSetting)setting;
            double sliderWidth = Math.max(40.0D, panelWidth * 0.42D);
            double sliderX = panelX + panelWidth - sliderWidth - 8.0D;
            fonts.regular().drawCenteredString(number.getDisplayValue(), (float)(sliderX - 16.0D), textY, Theme.TEXT);
            Widgets.slider(sliderX, rowY, sliderWidth, ROW_HEIGHT, number.getFraction());
        }
        else if (setting instanceof ModeSetting)
        {
            ModeSetting mode = (ModeSetting)setting;
            double dropdownWidth = Math.max(52.0D, panelWidth * 0.45D);
            double dropdownX = panelX + panelWidth - dropdownWidth - 6.0D;
            Widgets.dropdown(dropdownX, rowY + 2.0D, dropdownWidth, ROW_HEIGHT - 4.0D, mode.getMode(), this.openDropdown == mode, Widgets.isHovered(mouseX, mouseY, dropdownX, rowY, dropdownWidth, ROW_HEIGHT));
        }
        else if (setting instanceof BooleanSetting)
        {
            BooleanSetting bool = (BooleanSetting)setting;
            Widgets.toggle(panelX + panelWidth - 28.0D, rowY + 4.0D, 20.0D, 11.0D, this.animate(this.settingAnimations, setting, bool.getValue()));
        }
    }

    private void renderOpenDropdown(int mouseX, int mouseY, double panelX, double panelWidth, double contentTop)
    {
        if (this.openDropdown == null)
        {
            return;
        }

        double rowY = this.rowYOf(this.openDropdown, contentTop);

        if (rowY == -1.0D)
        {
            return;
        }

        FontManager fonts = FontManager.getInstance();
        double dropdownWidth = Math.max(52.0D, panelWidth * 0.45D);
        double dropdownX = panelX + panelWidth - dropdownWidth - 6.0D;
        List<String> modes = this.openDropdown.getModes();
        double listY = rowY + ROW_HEIGHT;
        RenderUtils.roundedRect(dropdownX, listY, dropdownWidth, (double)modes.size() * 13.0D + 2.0D, 3.0D, Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(dropdownX, listY, dropdownWidth, (double)modes.size() * 13.0D + 2.0D, 3.0D, 1.0F, Theme.ACCENT);

        for (int i = 0; i < modes.size(); ++i)
        {
            double entryY = listY + 1.0D + (double)i * 13.0D;

            if (Widgets.isHovered(mouseX, mouseY, dropdownX, entryY, dropdownWidth, 13.0D))
            {
                RenderUtils.rect(dropdownX + 1.0D, entryY, dropdownWidth - 2.0D, 13.0D, Theme.CARD_HOVER);
            }

            fonts.regular().drawString(modes.get(i), (float)(dropdownX + 6.0D), (float)(entryY + 3.0D), this.openDropdown.is(modes.get(i)) ? Theme.ACCENT_LIGHT : Theme.TEXT_SECONDARY);
        }
    }

    private double rowYOf(Setting target, double contentTop)
    {
        double cursor = contentTop - this.settingsScroll;
        String group = null;

        for (Setting setting : this.selected.getSettings())
        {
            if (!setting.getGroup().equals(group))
            {
                group = setting.getGroup();
                cursor += 15.0D;
            }

            if (setting == target)
            {
                return cursor;
            }

            cursor += ROW_HEIGHT + 3.0D;
        }

        return -1.0D;
    }

    private void renderInfoColumn(int mouseX, int mouseY)
    {
        FontManager fonts = FontManager.getInstance();
        double columnX = this.x + this.width - INFO_WIDTH;
        double top = this.y + 8.0D;
        double infoHeight = 76.0D;
        Widgets.panel(columnX, top, INFO_WIDTH - 8.0D, infoHeight);
        fonts.medium().drawString("INFO", (float)(columnX + 8.0D), (float)(top + 6.0D), Theme.ACCENT);

        String username = mc.getSession() != null ? mc.getSession().getUsername() : "AetherUser";
        this.infoRow(columnX, top + 20.0D, "Username", username, Theme.ACCENT_LIGHT);
        this.infoRow(columnX, top + 31.0D, "Rank", "Premium", Theme.ACCENT_LIGHT);
        this.infoRow(columnX, top + 42.0D, "Play Time", Aether.getInstance().getPlayTime(), Theme.TEXT);
        this.infoRow(columnX, top + 53.0D, "Ping", this.getPing() + "ms", Theme.GREEN);
        this.infoRow(columnX, top + 64.0D, "TPS", String.format("%.2f", Float.valueOf(20.0F)), Theme.TEXT);

        double bindsTop = top + infoHeight + 8.0D;
        List<Module> bound = new ArrayList<Module>();

        for (Module module : Aether.getInstance().getModuleManager().getModules())
        {
            if (module.getKeybind() != 0)
            {
                bound.add(module);
            }
        }

        double bindsHeight = 22.0D + (double)(bound.size() + 1) * 11.0D;
        Widgets.panel(columnX, bindsTop, INFO_WIDTH - 8.0D, bindsHeight);
        fonts.medium().drawString("KEY BINDS", (float)(columnX + 8.0D), (float)(bindsTop + 6.0D), Theme.ACCENT);
        double cursor = bindsTop + 20.0D;
        this.bindRow(columnX, cursor, "GUI", Keyboard.getKeyName(Aether.getInstance().getModuleManager().getClickGuiKey()), false);
        cursor += 11.0D;

        for (Module module : bound)
        {
            boolean binding = this.bindingModule == module;
            this.bindRow(columnX, cursor, module.getName(), binding ? "..." : Keyboard.getKeyName(module.getKeybind()), binding);
            cursor += 11.0D;
        }

        double buttonsY = this.y + this.height - 22.0D;
        double buttonWidth = (INFO_WIDTH - 8.0D - 8.0D) / 3.0D;

        for (int i = 0; i < 3; ++i)
        {
            double buttonX = columnX + (double)i * (buttonWidth + 4.0D);
            boolean hovered = Widgets.isHovered(mouseX, mouseY, buttonX, buttonsY, buttonWidth, 16.0D);
            RenderUtils.roundedRect(buttonX, buttonsY, buttonWidth, 16.0D, 4.0D, hovered ? Theme.CARD_HOVER : Theme.PANEL_ELEVATED);
            RenderUtils.roundedRectOutline(buttonX, buttonsY, buttonWidth, 16.0D, 4.0D, 1.0F, Theme.BORDER);
            int color = hovered ? Theme.TEXT : Theme.TEXT_SECONDARY;
            double iconX = buttonX + buttonWidth / 2.0D - 5.0D;

            if (i == 0)
            {
                Icons.folder(iconX, buttonsY + 3.0D, 10.0D, color);
            }
            else if (i == 1)
            {
                Icons.gear(iconX, buttonsY + 3.0D, 10.0D, color);
            }
            else
            {
                Icons.cloud(iconX, buttonsY + 3.0D, 10.0D, color);
            }
        }
    }

    private void infoRow(double columnX, double rowY, String label, String value, int valueColor)
    {
        FontManager fonts = FontManager.getInstance();
        fonts.small().drawString(label, (float)(columnX + 8.0D), (float)rowY, Theme.TEXT_SECONDARY);
        float valueWidth = fonts.small().getStringWidth(value);
        fonts.small().drawString(value, (float)(columnX + INFO_WIDTH - 16.0D - (double)valueWidth), (float)rowY, valueColor);
    }

    private void bindRow(double columnX, double rowY, String label, String key, boolean active)
    {
        FontManager fonts = FontManager.getInstance();
        fonts.small().drawString(label, (float)(columnX + 8.0D), (float)(rowY + 1.0D), Theme.TEXT_SECONDARY);
        float keyWidth = fonts.small().getStringWidth(key) + 8.0F;
        double keyX = columnX + INFO_WIDTH - 16.0D - (double)keyWidth;
        RenderUtils.roundedRect(keyX, rowY - 1.0D, (double)keyWidth, 10.0D, 2.0D, active ? Theme.ACCENT_FAINT : Theme.CARD);
        fonts.small().drawCenteredString(key, (float)(keyX + (double)keyWidth / 2.0D), (float)(rowY + 1.0D), active ? Theme.ACCENT_LIGHT : Theme.TEXT);
    }

    private int getPing()
    {
        if (mc.getConnection() != null && mc.player != null && mc.getConnection().getPlayerInfo(mc.player.getUniqueID()) != null)
        {
            return mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        }

        return 0;
    }

    private <T> float animate(Map<T, Float> animations, T key, boolean active)
    {
        float current = animations.containsKey(key) ? animations.get(key).floatValue() : (active ? 1.0F : 0.0F);
        float next = Widgets.approach(current, active ? 1.0F : 0.0F, 0.25F);
        animations.put(key, Float.valueOf(next));
        return next;
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        double listWidth = this.listWidth();
        double rowY = this.y + 8.0D;
        double sortWidth = 38.0D;
        double searchWidth = listWidth - sortWidth - 5.0D;
        this.searchFocused = Widgets.isHovered(mouseX, mouseY, this.x, rowY, searchWidth, 16.0D);

        if (this.searchFocused)
        {
            return;
        }

        if (Widgets.isHovered(mouseX, mouseY, this.x + searchWidth + 5.0D, rowY, sortWidth, 16.0D))
        {
            this.sortAlphabetically = !this.sortAlphabetically;
            return;
        }

        if (this.handleDropdownClick(mouseX, mouseY))
        {
            return;
        }

        if (this.handleListClick(mouseX, mouseY, button, listWidth))
        {
            return;
        }

        this.handleSettingsClick(mouseX, mouseY, button, listWidth);
    }

    private boolean handleDropdownClick(int mouseX, int mouseY)
    {
        if (this.openDropdown == null || this.selected == null)
        {
            return false;
        }

        double listWidth = this.listWidth();
        double panelX = this.x + listWidth + 10.0D;
        double panelWidth = this.width - listWidth - INFO_WIDTH - 22.0D;
        double contentTop = this.y + 36.0D;
        double rowY = this.rowYOf(this.openDropdown, contentTop);
        double dropdownWidth = Math.max(52.0D, panelWidth * 0.45D);
        double dropdownX = panelX + panelWidth - dropdownWidth - 6.0D;
        List<String> modes = this.openDropdown.getModes();

        for (int i = 0; i < modes.size(); ++i)
        {
            if (Widgets.isHovered(mouseX, mouseY, dropdownX, rowY + ROW_HEIGHT + 1.0D + (double)i * 13.0D, dropdownWidth, 13.0D))
            {
                this.openDropdown.setMode(modes.get(i));
                this.openDropdown = null;
                return true;
            }
        }

        this.openDropdown = null;
        return false;
    }

    private boolean handleListClick(int mouseX, int mouseY, int button, double listWidth)
    {
        double top = this.y + 30.0D;

        if (!Widgets.isHovered(mouseX, mouseY, this.x, top, listWidth, this.height - 34.0D))
        {
            return false;
        }

        double cursor = top - this.listScroll;

        for (Module module : this.visibleModules())
        {
            if (Widgets.isHovered(mouseX, mouseY, this.x, cursor, listWidth, CARD_HEIGHT))
            {
                if (button == 1)
                {
                    this.bindingModule = module;
                }
                else if (Widgets.isHovered(mouseX, mouseY, this.x + listWidth - 28.0D, cursor + 7.0D, 24.0D, 15.0D))
                {
                    module.toggle();
                }
                else
                {
                    this.selected = module;
                    this.settingsScroll = 0.0D;
                }

                return true;
            }

            cursor += CARD_HEIGHT + 4.0D;
        }

        return true;
    }

    private void handleSettingsClick(int mouseX, int mouseY, int button, double listWidth)
    {
        if (this.selected == null)
        {
            return;
        }

        double panelX = this.x + listWidth + 10.0D;
        double panelWidth = this.width - listWidth - INFO_WIDTH - 22.0D;

        if (Widgets.isHovered(mouseX, mouseY, panelX + panelWidth - 26.0D, this.y + 6.0D, 26.0D, 16.0D))
        {
            this.selected.toggle();
            return;
        }

        double contentTop = this.y + 36.0D;
        double cursor = contentTop - this.settingsScroll;
        String group = null;

        for (Setting setting : this.selected.getSettings())
        {
            if (!setting.getGroup().equals(group))
            {
                group = setting.getGroup();
                cursor += 15.0D;
            }

            if (Widgets.isHovered(mouseX, mouseY, panelX, cursor, panelWidth, ROW_HEIGHT))
            {
                this.clickSetting(setting, mouseX, panelX, panelWidth);
                return;
            }

            cursor += ROW_HEIGHT + 3.0D;
        }
    }

    private void clickSetting(Setting setting, int mouseX, double panelX, double panelWidth)
    {
        if (setting instanceof BooleanSetting)
        {
            ((BooleanSetting)setting).toggle();
        }
        else if (setting instanceof ModeSetting)
        {
            this.openDropdown = this.openDropdown == setting ? null : (ModeSetting)setting;
        }
        else if (setting instanceof NumberSetting)
        {
            NumberSetting number = (NumberSetting)setting;
            double sliderWidth = Math.max(40.0D, panelWidth * 0.42D);
            double sliderX = panelX + panelWidth - sliderWidth - 8.0D;
            this.draggedSlider = number;
            this.draggedSliderX = sliderX;
            this.draggedSliderWidth = sliderWidth;
            number.setFromFraction(((double)mouseX - sliderX) / sliderWidth);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        this.draggedSlider = null;
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        if (this.bindingModule != null)
        {
            this.bindingModule.setKeybind(keyCode == Keyboard.KEY_DELETE ? 0 : keyCode);
            this.bindingModule = null;
            return;
        }

        if (!this.searchFocused)
        {
            return;
        }

        if (keyCode == Keyboard.KEY_BACK)
        {
            if (!this.search.isEmpty())
            {
                this.search = this.search.substring(0, this.search.length() - 1);
            }
        }
        else if (keyCode == Keyboard.KEY_RETURN)
        {
            this.searchFocused = false;
        }
        else if (typedChar >= ' ' && typedChar < 127)
        {
            this.search = this.search + typedChar;
        }
    }

    public void scroll(int direction, int mouseX, int mouseY)
    {
        double listWidth = this.listWidth();

        if ((double)mouseX < this.x + listWidth)
        {
            double contentHeight = (double)this.visibleModules().size() * (CARD_HEIGHT + 4.0D);
            double maxScroll = Math.max(0.0D, contentHeight - (this.height - 34.0D));
            this.listScroll = Math.max(0.0D, Math.min(maxScroll, this.listScroll - (double)direction * 14.0D));
        }
        else if (this.selected != null)
        {
            double contentHeight = (double)this.selected.getSettings().size() * (ROW_HEIGHT + 3.0D) + 30.0D;
            double maxScroll = Math.max(0.0D, contentHeight - (this.height - 40.0D));
            this.settingsScroll = Math.max(0.0D, Math.min(maxScroll, this.settingsScroll - (double)direction * 12.0D));
        }
    }

    public void onClose()
    {
        this.draggedSlider = null;
        this.openDropdown = null;
        this.bindingModule = null;
    }

    /**
     * Called every frame from the render pass so a held slider keeps tracking the cursor.
     */
    public void updateDrag(int mouseX)
    {
        if (this.draggedSlider != null)
        {
            this.draggedSlider.setFromFraction(((double)mouseX - this.draggedSliderX) / this.draggedSliderWidth);
        }
    }
}
