package com.biomesize;

import com.cupboard.config.CupboardConfig;
import com.biomesize.config.CommonConfiguration;
import net.fabricmc.api.ModInitializer;
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

    public static Map<ResourceLocation,Integer> adapted = new HashMap<>();
    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID,new CommonConfiguration());

    @Override
    public void onInitialize()
    {
        adapted.put(new ResourceLocation("minecraft:worldgen/noise/temperature.json"), 0);
        adapted.put(new ResourceLocation("minecraft:worldgen/noise/vegetation.json"), 0);
    }
}
