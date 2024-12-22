package com.biomesize;

import com.biomesize.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.biomesize.BiomeSizeMod.MODID;

@Mod(MODID)
public class BiomeSizeMod
{
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("biomesize");
    public static final String MODID  = "biomesize";

    public static Map<ResourceLocation, Integer>      adapted = new HashMap<>();
    public static CupboardConfig<CommonConfiguration> config  = new CupboardConfig<>(MODID, new CommonConfiguration());

    public BiomeSizeMod(IEventBus modEventBus, ModContainer modContainer)
    {
        adapted.put(ResourceLocation.withDefaultNamespace("temperature"), 0);
        adapted.put(ResourceLocation.withDefaultNamespace("vegetation"), 0);
    }
}
