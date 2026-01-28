package com.biomesize;

import com.cupboard.config.CupboardConfig;
import com.biomesize.config.CommonConfiguration;
import net.fabricmc.api.ModInitializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BiomeSizeMod implements ModInitializer
{
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("biomesize");
    public static final String MODID = "biomesize";
    public static final String NOISE_ROUTER    = "noise_router";
    public static final String TEMPERATURE     = "temperature";
    public static final String VEGETATION      = "vegetation";
    public static final String XZ_SCALE        = "xz_scale";
    public static final String NOISE           = "noise";
    public static final String SIZE_HORIZONTAL = "size_horizontal";

    public static Map<ResourceLocation,Integer> adapted = new HashMap<>();
    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID,new CommonConfiguration());

    @Override
    public void onInitialize()
    {
        adapted.put(new ResourceLocation("minecraft:worldgen/noise/temperature.json"), 0);
        adapted.put(new ResourceLocation("minecraft:worldgen/noise/vegetation.json"), 0);
    }

    public static void adjustJsonData(final JsonElement jsonElement, final ResourceLocation id)
    {
        if (jsonElement instanceof JsonObject fileJson)
        {
            final String configID = id.getNamespace() + ":" + id.getPath().replace("worldgen/noise_settings/", "").replace(".json", "");

            if (fileJson.has(NOISE_ROUTER))
            {
                final JsonObject noiseJson = fileJson.get(NOISE_ROUTER).getAsJsonObject();
                if (noiseJson.has(TEMPERATURE) && noiseJson.has(VEGETATION))
                {
                    if (noiseJson.get(TEMPERATURE).isJsonObject() && noiseJson.get(VEGETATION).isJsonObject())
                    {
                        // Overworld style worldgen
                        final JsonObject temperatureJson = noiseJson.getAsJsonObject(TEMPERATURE).getAsJsonObject();
                        final JsonObject vegetationJson = noiseJson.getAsJsonObject(VEGETATION).getAsJsonObject();

                        if (config.getCommonConfig().createSettingsFor(configID, false))
                        {
                            return;
                        }

                        double scale = config.getCommonConfig().getScale(configID);

                        if (temperatureJson.has(XZ_SCALE))
                        {
                            temperatureJson.addProperty(XZ_SCALE, temperatureJson.get(XZ_SCALE).getAsDouble() * 1 / scale);
                        }
                        if (vegetationJson.has(XZ_SCALE))
                        {
                            vegetationJson.addProperty(XZ_SCALE, vegetationJson.get(XZ_SCALE).getAsDouble() * 1 / scale);
                        }

                        return;
                    }
                }
            }

            if (fileJson.has(NOISE) && fileJson.get(NOISE).isJsonObject())
            {
                final JsonObject noiseJson = fileJson.get(NOISE).getAsJsonObject();
                if (noiseJson.has(SIZE_HORIZONTAL))
                {
                    if (config.getCommonConfig().createSettingsFor(configID, true))
                    {
                        return;
                    }

                    int scale = (int) config.getCommonConfig().getScale(configID);

                    noiseJson.addProperty(SIZE_HORIZONTAL, noiseJson.get(SIZE_HORIZONTAL).getAsInt() + scale);
                }
            }
        }
    }
}
