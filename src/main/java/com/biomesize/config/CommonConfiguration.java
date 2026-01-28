package com.biomesize.config;

import com.biomesize.BiomeSizeMod;
import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.*;

import java.util.Map;

public class CommonConfiguration implements ICommonConfig
{

    public  int                              biomeSizeModifier   = -2;
    public  boolean                                legacyMode          = false;
    private Object2DoubleLinkedOpenHashMap<String> scaleSettings       = new Object2DoubleLinkedOpenHashMap<>();
    private Object2IntLinkedOpenHashMap<String>          simpleScaleSettings = new Object2IntLinkedOpenHashMap<>();

    public CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Biome size settings per noise setting, set by default to no changes. Gets automatically filled on world creation. Restart after changing values."
            + "Note that the biome size changes work indirectly by modifying temperature and vegetation zone sizes and you cannot change the size of a specific biome");

        for (final Object2DoubleMap.Entry<String> scaleEntry : scaleSettings.object2DoubleEntrySet())
        {
            final JsonObject scaleEntryJson = new JsonObject();
            scaleEntryJson.addProperty("desc", "Set the multiplicative modifier for biome size. 1.0 = Default size, 2.0 = bigger, 0.5 = smaller.");
            scaleEntryJson.addProperty("size_multiplier", scaleEntry.getDoubleValue());
            entry.add(scaleEntry.getKey(), scaleEntryJson);
        }

        for (final Object2IntMap.Entry<String> scaleEntry : simpleScaleSettings.object2IntEntrySet())
        {
            final JsonObject scaleEntryJson = new JsonObject();
            scaleEntryJson.addProperty("desc",
                "Set the horizontal size offset, this dimension only has limited size changing support. You can only step sizes in whole numbers, not percentages. 0 = Default size, 1 = bigger, -1 = smaller. Range[-1,2]");
            scaleEntryJson.addProperty("horizontal_size", scaleEntry.getIntValue());
            entry.add(scaleEntry.getKey(), scaleEntryJson);
        }

        root.add("worldSettings", entry);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        if (data.has("biomeSizeModifier"))
        {
            legacyMode = true;
            biomeSizeModifier = data.get("biomeSizeModifier").getAsJsonObject().get("biomeSizeModifier").getAsInt();
        }
        else
        {
            final JsonObject dimensionJson = data.get("worldSettings").getAsJsonObject();
            for (final Map.Entry<String, JsonElement> entry : dimensionJson.entrySet())
            {
                if (entry.getValue().isJsonObject())
                {
                    final JsonObject entryJson = entry.getValue().getAsJsonObject();
                    if (entryJson.has("size_multiplier"))
                    {
                        scaleSettings.put(entry.getKey(), entryJson.get("size_multiplier").getAsDouble());
                    }
                    else
                    {
                        simpleScaleSettings.put(entry.getKey(), entryJson.get("horizontal_size").getAsInt());
                    }
                }
            }
        }
    }

    public boolean createSettingsFor(final String configID, final boolean simpleIntValue)
    {
        if (scaleSettings.containsKey(configID) || simpleScaleSettings.containsKey(configID))
        {
            return false;
        }

        if (simpleIntValue)
        {
            simpleScaleSettings.put(configID, 0);
        }
        else
        {
            scaleSettings.put(configID, 1.0);
        }

        BiomeSizeMod.config.save();
        return true;
    }

    public double getScale(final String configID)
    {
        if (scaleSettings.containsKey(configID))
        {
            return scaleSettings.getDouble(configID);
        }

        if (simpleScaleSettings.containsKey(configID))
        {
            return simpleScaleSettings.getInt(configID);
        }

        return 0;
    }
}
