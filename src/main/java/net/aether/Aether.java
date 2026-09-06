package net.aether;

import net.aether.config.ConfigManager;
import net.aether.gui.font.FontManager;
import net.aether.hud.HudManager;
import net.aether.module.Module;
import net.aether.module.ModuleManager;
import net.minecraft.client.Minecraft;

public class Aether
{
    public static final String NAME = "Aether Client";
    public static final String VERSION = "2.7.4";

    private static Aether instance;

    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private HudManager hudManager;
    private long startTime;

    public static Aether getInstance()
    {
        if (instance == null)
        {
            instance = new Aether();
        }

        return instance;
    }

    public void startup()
    {
        this.startTime = System.currentTimeMillis();
        this.moduleManager = new ModuleManager();
        this.hudManager = new HudManager();
        this.configManager = new ConfigManager(this.moduleManager);
        this.configManager.load(this.configManager.getActiveConfig());
    }

    public void shutdown()
    {
        if (this.configManager != null)
        {
            this.configManager.save(this.configManager.getActiveConfig());
        }
    }

    public void onTick()
    {
        if (this.moduleManager == null || Minecraft.getMinecraft().player == null)
        {
            return;
        }

        for (Module module : this.moduleManager.getModules())
        {
            if (module.isEnabled())
            {
                module.onTick();
            }
        }
    }

    public void onKeyPress(int keyCode)
    {
        if (this.moduleManager == null || keyCode == 0)
        {
            return;
        }

        if (keyCode == this.moduleManager.getClickGuiKey())
        {
            Minecraft.getMinecraft().displayGuiScreen(new net.aether.gui.clickgui.ClickGui());
            return;
        }

        for (Module module : this.moduleManager.getModules())
        {
            if (module.getKeybind() == keyCode)
            {
                module.toggle();
            }
        }
    }

    public void onRender2D(float partialTicks)
    {
        if (this.moduleManager == null)
        {
            return;
        }

        Module hud = this.moduleManager.getModule("HUD");

        if (hud != null && hud.isEnabled())
        {
            this.hudManager.render();
        }

        for (Module module : this.moduleManager.getModules())
        {
            if (module.isEnabled())
            {
                module.onRender2D(partialTicks);
            }
        }
    }

    public void onRender3D(float partialTicks)
    {
        if (this.moduleManager == null)
        {
            return;
        }

        for (Module module : this.moduleManager.getModules())
        {
            if (module.isEnabled())
            {
                module.onRender3D(partialTicks);
            }
        }
    }

    public String getPlayTime()
    {
        long seconds = (System.currentTimeMillis() - this.startTime) / 1000L;
        long days = seconds / 86400L;
        long hours = seconds % 86400L / 3600L;
        long minutes = seconds % 3600L / 60L;
        return days + "d " + hours + "h " + minutes + "m";
    }

    public ModuleManager getModuleManager()
    {
        return this.moduleManager;
    }

    public ConfigManager getConfigManager()
    {
        return this.configManager;
    }

    public HudManager getHudManager()
    {
        return this.hudManager;
    }

    public FontManager getFontManager()
    {
        return FontManager.getInstance();
    }
}
