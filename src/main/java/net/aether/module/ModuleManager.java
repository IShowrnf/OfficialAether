package net.aether.module;

import java.util.ArrayList;
import java.util.List;

import net.aether.module.combat.AntiBot;
import net.aether.module.combat.AutoArmor;
import net.aether.module.combat.AutoTotem;
import net.aether.module.combat.CrystalAura;
import net.aether.module.combat.HitBoxes;
import net.aether.module.combat.KillAura;
import net.aether.module.combat.TriggerBot;
import net.aether.module.combat.Velocity;
import net.aether.module.exploit.FastPlace;
import net.aether.module.exploit.Timer;
import net.aether.module.misc.ClickGuiModule;
import net.aether.module.misc.Hud;
import net.aether.module.movement.AutoWalk;
import net.aether.module.movement.Flight;
import net.aether.module.movement.Speed;
import net.aether.module.movement.Sprint;
import net.aether.module.movement.Step;
import net.aether.module.player.AutoRespawn;
import net.aether.module.player.AutoTool;
import net.aether.module.player.NoFall;
import net.aether.module.render.Fullbright;
import net.aether.module.render.Nametags;
import net.aether.module.render.NoHurtCam;
import net.aether.module.world.AutoRejoin;
import net.aether.module.world.FastBreak;
import org.lwjgl.input.Keyboard;

public class ModuleManager
{
    private final List<Module> modules = new ArrayList<Module>();
    private int clickGuiKey = Keyboard.KEY_RSHIFT;

    public ModuleManager()
    {
        this.register(new KillAura());
        this.register(new CrystalAura());
        this.register(new AutoTotem());
        this.register(new HitBoxes());
        this.register(new Velocity());
        this.register(new AutoArmor());
        this.register(new AntiBot());
        this.register(new TriggerBot());

        this.register(new Sprint());
        this.register(new Step());
        this.register(new Speed());
        this.register(new Flight());
        this.register(new AutoWalk());

        this.register(new Fullbright());
        this.register(new NoHurtCam());
        this.register(new Nametags());

        this.register(new Timer());
        this.register(new FastPlace());

        this.register(new NoFall());
        this.register(new AutoRespawn());
        this.register(new AutoTool());

        this.register(new FastBreak());
        this.register(new AutoRejoin());

        this.register(new ClickGuiModule());
        this.register(new Hud());

        this.getModule("KillAura").setKeybind(Keyboard.KEY_C);
        this.getModule("CrystalAura").setKeybind(Keyboard.KEY_V);
        this.getModule("AutoTotem").setKeybind(Keyboard.KEY_B);
        this.getModule("Velocity").setKeybind(Keyboard.KEY_N);
        this.getModule("HitBoxes").setKeybind(Keyboard.KEY_H);
    }

    private void register(Module module)
    {
        this.modules.add(module);
    }

    public List<Module> getModules()
    {
        return this.modules;
    }

    public List<Module> getModules(Category category)
    {
        List<Module> result = new ArrayList<Module>();

        for (Module module : this.modules)
        {
            if (module.getCategory() == category)
            {
                result.add(module);
            }
        }

        return result;
    }

    public List<Module> getEnabledModules()
    {
        List<Module> result = new ArrayList<Module>();

        for (Module module : this.modules)
        {
            if (module.isEnabled())
            {
                result.add(module);
            }
        }

        return result;
    }

    public Module getModule(String name)
    {
        for (Module module : this.modules)
        {
            if (module.getName().equalsIgnoreCase(name))
            {
                return module;
            }
        }

        return null;
    }

    public int getClickGuiKey()
    {
        return this.clickGuiKey;
    }

    public void setClickGuiKey(int clickGuiKey)
    {
        this.clickGuiKey = clickGuiKey;
    }
}
