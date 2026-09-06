package net.aether.gui.clickgui;

import java.util.Collections;
import java.util.List;

import net.aether.gui.Icons;
import net.minecraft.client.Minecraft;

public abstract class GuiTab
{
    protected static final Minecraft mc = Minecraft.getMinecraft();

    protected double x;
    protected double y;
    protected double width;
    protected double height;

    public void setBounds(double x, double y, double width, double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract String getTitle();

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    /**
     * Items shown in the left rail. An empty list hides the rail for this tab.
     */
    public List<String> getSidebarItems()
    {
        return Collections.<String>emptyList();
    }

    public int getSelectedSidebarIndex()
    {
        return 0;
    }

    public void onSidebarSelected(int index)
    {
    }

    public void renderSidebarIcon(int index, double x, double y, double size, int color)
    {
        Icons.dots(x, y, size, color);
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
    }

    public void mouseReleased(int mouseX, int mouseY, int state)
    {
    }

    public void keyTyped(char typedChar, int keyCode)
    {
    }

    public void scroll(int direction, int mouseX, int mouseY)
    {
    }

    public void onClose()
    {
    }
}
