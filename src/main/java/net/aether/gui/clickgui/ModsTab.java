package net.aether.gui.clickgui;

import java.util.ArrayList;
import java.util.List;

import net.aether.gui.Icons;
import net.aether.gui.RenderUtils;
import net.aether.gui.Theme;
import net.aether.gui.Widgets;
import net.aether.gui.font.FontManager;
import net.aether.module.Category;

/**
 * Presentation-only controls for the first GUI milestone.
 *
 * <p>This screen intentionally does not read or toggle gameplay modules. The
 * rows are visual placeholders for the shell and safe client-side preferences
 * can be wired here independently in a later milestone.</p>
 */
public class ModsTab extends GuiTab
{
    private static final double CARD_HEIGHT = 30.0D;
    private static final double INFO_WIDTH = 126.0D;
    private static final String[] CATEGORIES = new String[] {"Interface", "HUD", "Accessibility"};
    private static final Category[] CATEGORY_ICONS = new Category[] {Category.MISC, Category.RENDER, Category.PLAYER};

    private static final SafeOption[][] OPTIONS = new SafeOption[][] {
        new SafeOption[] {
            new SafeOption("Panel animations", "Smooth transitions in the shell.", true),
            new SafeOption("Compact layout", "Use tighter rows and spacing.", false),
            new SafeOption("Accent glow", "Emphasize selected navigation.", true)
        },
        new SafeOption[] {
            new SafeOption("FPS indicator", "Show client performance in the rail.", true),
            new SafeOption("HUD preview", "Keep the layout preview visible.", true),
            new SafeOption("Status labels", "Display descriptive HUD labels.", false)
        },
        new SafeOption[] {
            new SafeOption("High contrast", "Increase text and border contrast.", false),
            new SafeOption("Reduce motion", "Prefer static presentation effects.", false),
            new SafeOption("Large text", "Use a more readable interface scale.", false)
        }
    };

    private int category;
    private int selected;
    private String search = "";
    private boolean searchFocused;
    private boolean sortAlphabetically;
    private double listScroll;

    public String getTitle()
    {
        return "Mods";
    }

    public List<String> getSidebarItems()
    {
        List<String> items = new ArrayList<String>();

        for (String name : CATEGORIES)
        {
            items.add(name);
        }

        return items;
    }

    public int getSelectedSidebarIndex()
    {
        return this.category;
    }

    public void onSidebarSelected(int index)
    {
        if (index >= 0 && index < CATEGORIES.length)
        {
            this.category = index;
            this.selected = 0;
            this.listScroll = 0.0D;
            this.searchFocused = false;
        }
    }

    public void renderSidebarIcon(int index, double x, double y, double size, int color)
    {
        Icons.category(CATEGORY_ICONS[index], x, y, size, color);
    }

    private SafeOption[] visibleOptions()
    {
        List<SafeOption> visible = new ArrayList<SafeOption>();
        String query = this.search.toLowerCase();

        for (SafeOption option : OPTIONS[this.category])
        {
            if (query.isEmpty() || option.name.toLowerCase().contains(query) || option.description.toLowerCase().contains(query))
            {
                visible.add(option);
            }
        }

        if (this.sortAlphabetically)
        {
            visible.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        }

        return visible.toArray(new SafeOption[visible.size()]);
    }

    private double listWidth()
    {
        return Math.max(142.0D, Math.min(210.0D, this.width - INFO_WIDTH - 34.0D));
    }

    public void render(int mouseX, int mouseY, float partialTicks)
    {
        SafeOption[] options = this.visibleOptions();
        double listWidth = this.listWidth();
        this.renderSearchRow(mouseX, listWidth);
        this.renderOptionList(mouseX, mouseY, options, listWidth);

        double panelX = this.x + listWidth + 12.0D;
        double panelWidth = this.width - listWidth - INFO_WIDTH - 20.0D;
        RenderUtils.rect(panelX - 6.0D, this.y + 8.0D, 1.0D, this.height - 16.0D, Theme.BORDER);
        this.renderDetails(panelX, panelWidth);
        this.renderSafetyNote(mouseX, panelX, panelWidth);
    }

    private void renderSearchRow(int mouseX, double listWidth)
    {
        FontManager fonts = FontManager.getInstance();
        double rowY = this.y + 8.0D;
        double sortWidth = 42.0D;
        double searchWidth = listWidth - sortWidth - 6.0D;

        RenderUtils.roundedRect(this.x, rowY, searchWidth, 16.0D, 4.0D, Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(this.x, rowY, searchWidth, 16.0D, 4.0D, 1.0F, this.searchFocused ? Theme.ACCENT : Theme.BORDER);
        Icons.search(this.x + 5.0D, rowY + 4.0D, 8.0D, Theme.TEXT_MUTED);
        String text = this.search.isEmpty() && !this.searchFocused ? "Search controls..." : this.search + (this.searchFocused ? "_" : "");
        fonts.regular().drawString(text, (float)(this.x + 17.0D), (float)(rowY + 4.0D), this.search.isEmpty() ? Theme.TEXT_MUTED : Theme.TEXT);

        double sortX = this.x + searchWidth + 6.0D;
        boolean hovered = Widgets.isHovered(mouseX, mouseY, sortX, rowY, sortWidth, 16.0D);
        RenderUtils.roundedRect(sortX, rowY, sortWidth, 16.0D, 4.0D, hovered ? Theme.CARD_HOVER : Theme.PANEL_ELEVATED);
        RenderUtils.roundedRectOutline(sortX, rowY, sortWidth, 16.0D, 4.0D, 1.0F, Theme.BORDER);
        fonts.regular().drawString("Sort", (float)(sortX + 7.0D), (float)(rowY + 4.0D), Theme.TEXT_SECONDARY);
        Widgets.chevron(sortX + sortWidth - 8.0D, rowY + 8.0D, this.sortAlphabetically);
    }

    private void renderOptionList(int mouseX, int mouseY, SafeOption[] options, double listWidth)
    {
        FontManager fonts = FontManager.getInstance();
        double top = this.y + 30.0D;
        double bottom = this.y + this.height - 4.0D;
        RenderUtils.beginScissor(this.x, top, listWidth, bottom - top);
        double cursor = top - this.listScroll;

        for (int i = 0; i < options.length; ++i)
        {
            SafeOption option = options[i];

            if (cursor + CARD_HEIGHT >= top - CARD_HEIGHT && cursor <= bottom)
            {
                boolean hovered = Widgets.isHovered(mouseX, mouseY, this.x, cursor, listWidth, CARD_HEIGHT)
                        && mouseY >= (int)top && mouseY <= (int)bottom;
                boolean selected = optionsIndex(option) == this.selected;
                RenderUtils.roundedRect(this.x, cursor, listWidth, CARD_HEIGHT, 4.0D,
                        selected ? Theme.CARD_SELECTED : (hovered ? Theme.CARD_HOVER : Theme.CARD));
                RenderUtils.roundedRectOutline(this.x, cursor, listWidth, CARD_HEIGHT, 4.0D, 1.0F,
                        selected ? Theme.BORDER_ACCENT : Theme.BORDER);
                fonts.medium().drawString(option.name, (float)(this.x + 10.0D), (float)(cursor + 6.0D), Theme.TEXT);
                fonts.small().drawString(fonts.small().trimToWidth(option.description, (float)(listWidth - 52.0D)),
                        (float)(this.x + 10.0D), (float)(cursor + 17.0D), Theme.TEXT_MUTED);
                Widgets.toggle(this.x + listWidth - 28.0D, cursor + 10.0D, 20.0D, 11.0D, option.enabled ? 1.0F : 0.0F);
            }

            cursor += CARD_HEIGHT + 4.0D;
        }

        RenderUtils.endScissor();
    }

    private int optionsIndex(SafeOption option)
    {
        SafeOption[] options = OPTIONS[this.category];

        for (int i = 0; i < options.length; ++i)
        {
            if (options[i] == option)
            {
                return i;
            }
        }

        return 0;
    }

    private void renderDetails(double panelX, double panelWidth)
    {
        FontManager fonts = FontManager.getInstance();
        SafeOption option = OPTIONS[this.category][Math.min(this.selected, OPTIONS[this.category].length - 1)];
        fonts.large().drawString(option.name, (float)panelX, (float)(this.y + 8.0D), Theme.ACCENT_LIGHT);
        fonts.regular().drawString("Presentation control", (float)panelX, (float)(this.y + 25.0D), Theme.ACCENT);
        fonts.small().drawString(fonts.small().trimToWidth(option.description, (float)panelWidth),
                (float)panelX, (float)(this.y + 42.0D), Theme.TEXT_SECONDARY);

        Widgets.panel(panelX, this.y + 62.0D, panelWidth, 54.0D);
        fonts.small().drawString("SAFE CLIENT UI", (float)(panelX + 8.0D), (float)(this.y + 70.0D), Theme.ACCENT);
        fonts.small().drawString("No gameplay, network, or entity behavior is", (float)(panelX + 8.0D),
                (float)(this.y + 84.0D), Theme.TEXT_MUTED);
        fonts.small().drawString("connected to these controls.", (float)(panelX + 8.0D), (float)(this.y + 95.0D), Theme.TEXT_MUTED);
    }

    private void renderSafetyNote(int mouseX, double panelX, double panelWidth)
    {
        FontManager fonts = FontManager.getInstance();
        double y = this.y + this.height - 34.0D;
        boolean hovered = Widgets.isHovered(mouseX, (int)y, panelX, y, panelWidth, 20.0D);
        RenderUtils.roundedRect(panelX, y, panelWidth, 20.0D, 4.0D, hovered ? Theme.CARD_HOVER : Theme.CARD);
        fonts.small().drawString("Cosmetic shell • offline-safe", (float)(panelX + 8.0D), (float)(y + 6.0D), Theme.TEXT_SECONDARY);
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        double listWidth = this.listWidth();
        double rowY = this.y + 8.0D;
        double sortWidth = 42.0D;
        double searchWidth = listWidth - sortWidth - 6.0D;

        this.searchFocused = Widgets.isHovered(mouseX, mouseY, this.x, rowY, searchWidth, 16.0D);

        if (this.searchFocused)
        {
            return;
        }

        if (Widgets.isHovered(mouseX, mouseY, this.x + searchWidth + 6.0D, rowY, sortWidth, 16.0D))
        {
            this.sortAlphabetically = !this.sortAlphabetically;
            return;
        }

        double top = this.y + 30.0D;
        if (Widgets.isHovered(mouseX, mouseY, this.x, top, listWidth, this.height - 34.0D))
        {
            double cursor = top - this.listScroll;

            for (SafeOption option : this.visibleOptions())
            {
                if (Widgets.isHovered(mouseX, mouseY, this.x, cursor, listWidth, CARD_HEIGHT))
                {
                    this.selected = this.optionsIndex(option);
                    option.enabled = !option.enabled;
                    return;
                }

                cursor += CARD_HEIGHT + 4.0D;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        if (!this.searchFocused)
        {
            return;
        }

        if (keyCode == 14)
        {
            if (!this.search.isEmpty())
            {
                this.search = this.search.substring(0, this.search.length() - 1);
            }
        }
        else if (keyCode == 28)
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
        double contentHeight = (double)this.visibleOptions().length * (CARD_HEIGHT + 4.0D);
        double maxScroll = Math.max(0.0D, contentHeight - (this.height - 34.0D));
        this.listScroll = Math.max(0.0D, Math.min(maxScroll, this.listScroll - (double)direction * 14.0D));
    }

    public void onClose()
    {
        this.searchFocused = false;
    }

    private static final class SafeOption
    {
        private final String name;
        private final String description;
        private boolean enabled;

        private SafeOption(String name, String description, boolean enabled)
        {
            this.name = name;
            this.description = description;
            this.enabled = enabled;
        }
    }
}
