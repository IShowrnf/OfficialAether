package net.aether.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.aether.module.Module;
import net.aether.module.ModuleManager;
import net.aether.setting.Setting;
import net.minecraft.client.Minecraft;

public class ConfigManager
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ModuleManager moduleManager;
    private final File directory;
    private String activeConfig = "Default";

    public ConfigManager(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
        this.directory = new File(Minecraft.getMinecraft().mcDataDir, "aether/configs");

        if (!this.directory.exists())
        {
            this.directory.mkdirs();
        }
    }

    public List<String> getConfigNames()
    {
        List<String> names = new ArrayList<String>();
        File[] files = this.directory.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile() && file.getName().endsWith(".json"))
                {
                    names.add(file.getName().substring(0, file.getName().length() - 5));
                }
            }
        }

        if (!names.contains("Default"))
        {
            names.add("Default");
        }

        Collections.sort(names);
        return names;
    }

    public void save(String name)
    {
        JsonArray moduleArray = new JsonArray();

        for (Module module : this.moduleManager.getModules())
        {
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("name", module.getName());
            moduleObject.addProperty("enabled", module.isEnabled());
            moduleObject.addProperty("keybind", module.getKeybind());

            JsonObject settingsObject = new JsonObject();

            for (Setting setting : module.getSettings())
            {
                settingsObject.addProperty(setting.getName(), setting.serialize());
            }

            moduleObject.add("settings", settingsObject);
            moduleArray.add(moduleObject);
        }

        JsonObject root = new JsonObject();
        root.addProperty("clickGuiKey", this.moduleManager.getClickGuiKey());
        root.add("modules", moduleArray);

        FileWriter writer = null;

        try
        {
            writer = new FileWriter(new File(this.directory, name + ".json"), StandardCharsets.UTF_8);
            writer.write(GSON.toJson(root));
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        finally
        {
            close(writer);
        }
    }

    public void load(String name)
    {
        File file = new File(this.directory, name + ".json");

        if (!file.exists())
        {
            return;
        }

        FileReader reader = null;

        try
        {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            JsonElement parsed = new JsonParser().parse(reader);

            if (!parsed.isJsonObject())
            {
                return;
            }

            JsonObject root = parsed.getAsJsonObject();

            if (root.has("clickGuiKey"))
            {
                this.moduleManager.setClickGuiKey(root.get("clickGuiKey").getAsInt());
            }

            if (!root.has("modules"))
            {
                return;
            }

            for (JsonElement element : root.getAsJsonArray("modules"))
            {
                JsonObject moduleObject = element.getAsJsonObject();
                Module module = this.moduleManager.getModule(moduleObject.get("name").getAsString());

                if (module == null)
                {
                    continue;
                }

                module.setKeybind(moduleObject.get("keybind").getAsInt());
                module.setEnabled(moduleObject.get("enabled").getAsBoolean());

                if (!moduleObject.has("settings"))
                {
                    continue;
                }

                JsonObject settingsObject = moduleObject.getAsJsonObject("settings");

                for (Setting setting : module.getSettings())
                {
                    if (settingsObject.has(setting.getName()))
                    {
                        setting.deserialize(settingsObject.get(setting.getName()).getAsString());
                    }
                }
            }

            this.activeConfig = name;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            close(reader);
        }
    }

    public void delete(String name)
    {
        File file = new File(this.directory, name + ".json");

        if (file.exists())
        {
            file.delete();
        }

        if (this.activeConfig.equals(name))
        {
            this.activeConfig = "Default";
        }
    }

    public void create(String name)
    {
        this.save(name);
        this.activeConfig = name;
    }

    public String getActiveConfig()
    {
        return this.activeConfig;
    }

    public void setActiveConfig(String activeConfig)
    {
        this.activeConfig = activeConfig;
    }

    private static void close(java.io.Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException ioexception)
            {
            }
        }
    }
}
