package net.aether.hud.element;

import java.util.List;

import net.aether.Aether;
import net.aether.gui.RenderUtils;
import net.aether.gui.font.FontManager;
import net.aether.gui.font.TrueTypeFont;
import net.aether.hud.HudElement;
import net.aether.module.Module;
import net.minecraft.client.gui.ScaledResolution;

public class ArrayListElement extends HudElement
{
    public ArrayListElement()
    {
        super("Array List", 0.0D, 4.0D, true);
    }

    private net.aether.module.misc.Hud settings()
    {
        Module module = Aether.getInstance().getModuleManager().getModule("HUD");
        return module instanceof net.aether.module.misc.Hud ? (net.aether.module.misc.Hud)module : null;
    }

    private boolean background()
    {
        net.aether.module.misc.Hud hud = this.settings();
        return hud == null || hud.background.getValue();
    }

    private int colour(int index)
    {
        net.aether.module.misc.Hud hud = this.settings();

        if (hud != null && hud.colour.is("White"))
        {
            return 0xFFFFFFFF;
        }

        if (hud != null && hud.colour.is("Accent"))
        {
            return net.aether.gui.Theme.ACCENT_LIGHT;
        }

        return RenderUtils.rainbow(index * 180, 0.55F, 1.0F);
    }

    public void render()
    {
        TrueTypeFont font = FontManager.getInstance().medium();
        ScaledResolution resolution = new ScaledResolution(mc);
        List<Module> modules = Aether.getInstance().getModuleManager().getEnabledModules();
        modules.sort((a, b) -> Float.compare(font.getStringWidth(b.getName()), font.getStringWidth(a.getName())));

        double right = (double)resolution.getScaledWidth() - 4.0D;
        double cursor = this.getY();
        int index = 0;

        for (Module module : modules)
        {
            float width = font.getStringWidth(module.getName());
            int color = this.colour(index);

            if (this.background())
            {
                RenderUtils.rect(right - (double)width - 4.0D, cursor, (double)width + 4.0D, 11.0D, 0x90000000);
            }

            RenderUtils.rect(right, cursor, 1.5D, 11.0D, color);
            font.drawString(module.getName(), (float)(right - (double)width - 2.0D), (float)(cursor + 1.5D), color);
            cursor += 11.0D;
            ++index;
        }
    }
}
