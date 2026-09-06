package net.aether.gui.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Lazily builds the UI fonts. Fonts are rasterised at twice their logical size and drawn at half
 * scale, which keeps them sharp at any GUI scale.
 */
public class FontManager
{
    private static final String[] PREFERRED_FAMILIES = new String[] {"Inter", "Segoe UI", "Roboto", "Helvetica Neue", "DejaVu Sans", "SansSerif"};

    private static FontManager instance;

    private TrueTypeFont title;
    private TrueTypeFont large;
    private TrueTypeFont medium;
    private TrueTypeFont regular;
    private TrueTypeFont small;

    public static FontManager getInstance()
    {
        if (instance == null)
        {
            instance = new FontManager();
        }

        return instance;
    }

    private static String pickFamily()
    {
        try
        {
            String[] available = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            for (String preferred : PREFERRED_FAMILIES)
            {
                for (String family : available)
                {
                    if (family.equalsIgnoreCase(preferred))
                    {
                        return family;
                    }
                }
            }
        }
        catch (Throwable throwable)
        {
        }

        return Font.SANS_SERIF;
    }

    private static TrueTypeFont build(int size, int style)
    {
        return new TrueTypeFont(new Font(pickFamily(), style, size * 2));
    }

    public TrueTypeFont title()
    {
        if (this.title == null)
        {
            this.title = build(19, Font.BOLD);
        }

        return this.title;
    }

    public TrueTypeFont large()
    {
        if (this.large == null)
        {
            this.large = build(14, Font.BOLD);
        }

        return this.large;
    }

    public TrueTypeFont medium()
    {
        if (this.medium == null)
        {
            this.medium = build(12, Font.BOLD);
        }

        return this.medium;
    }

    public TrueTypeFont regular()
    {
        if (this.regular == null)
        {
            this.regular = build(11, Font.PLAIN);
        }

        return this.regular;
    }

    public TrueTypeFont small()
    {
        if (this.small == null)
        {
            this.small = build(9, Font.PLAIN);
        }

        return this.small;
    }
}
