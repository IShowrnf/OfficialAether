package net.aether.module;

import java.util.ArrayList;
import java.util.List;

import net.aether.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public abstract class Module
{
    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting> settings = new ArrayList<Setting>();

    private boolean enabled;
    private int keybind = 0;

    public Module(String name, String description, Category category)
    {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable()
    {
    }

    public void onDisable()
    {
    }

    public void onTick()
    {
    }

    public void onRender2D(float partialTicks)
    {
    }

    public void onRender3D(float partialTicks)
    {
    }

    public void toggle()
    {
        this.setEnabled(!this.enabled);
    }

    public void setEnabled(boolean enabled)
    {
        if (this.enabled == enabled)
        {
            return;
        }

        this.enabled = enabled;

        if (enabled)
        {
            this.onEnable();
        }
        else
        {
            this.onDisable();
        }
    }

    protected void addSettings(Setting... toAdd)
    {
        for (Setting setting : toAdd)
        {
            this.settings.add(setting);
        }
    }

    public Setting getSetting(String settingName)
    {
        for (Setting setting : this.settings)
        {
            if (setting.getName().equalsIgnoreCase(settingName))
            {
                return setting;
            }
        }

        return null;
    }

    protected EntityPlayerSP player()
    {
        return mc.player;
    }

    protected WorldClient world()
    {
        return mc.world;
    }

    protected boolean inGame()
    {
        return mc.player != null && mc.world != null;
    }

    public boolean isEnabled()
    {
        return this.enabled;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public List<Setting> getSettings()
    {
        return this.settings;
    }

    public int getKeybind()
    {
        return this.keybind;
    }

    public void setKeybind(int keybind)
    {
        this.keybind = keybind;
    }
}
