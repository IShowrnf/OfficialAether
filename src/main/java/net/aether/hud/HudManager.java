package net.aether.hud;

import java.util.ArrayList;
import java.util.List;

import net.aether.hud.element.ArrayListElement;
import net.aether.hud.element.CoordinatesElement;
import net.aether.hud.element.PingElement;
import net.aether.hud.element.WatermarkElement;

public class HudManager
{
    private final List<HudElement> elements = new ArrayList<HudElement>();

    public HudManager()
    {
        this.elements.add(new WatermarkElement());
        this.elements.add(new ArrayListElement());
        this.elements.add(new CoordinatesElement());
        this.elements.add(new PingElement());
    }

    public void render()
    {
        if (mcHidden())
        {
            return;
        }

        for (HudElement element : this.elements)
        {
            if (element.isEnabled())
            {
                element.render();
            }
        }
    }

    private static boolean mcHidden()
    {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        return mc.gameSettings.hideGUI || mc.player == null;
    }

    public List<HudElement> getElements()
    {
        return this.elements;
    }

    public HudElement getElement(String name)
    {
        for (HudElement element : this.elements)
        {
            if (element.getName().equalsIgnoreCase(name))
            {
                return element;
            }
        }

        return null;
    }
}
