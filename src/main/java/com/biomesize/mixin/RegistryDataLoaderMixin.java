package com.biomesize.mixin;

import com.biomesize.BiomeSizeMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin
{
    @Inject(method = "loadElementFromResource", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Decoder;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;", remap = false), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static <E> void onLoad(
      final WritableRegistry<E> p_326195_,
      final Decoder<E> otherdecoder,
      final RegistryOps<JsonElement> p_325932_,
      final ResourceKey<E> resourceKey,
      final Resource p_326141_,
      final RegistrationInfo p_326033_,
      final CallbackInfo ci,
      final Decoder decoder,
      final Reader reader,
      final JsonElement jsonElement)
    {
        if (BiomeSizeMod.adapted.containsKey(resourceKey.location()))
        {
            if (jsonElement instanceof JsonObject)
            {
                ((JsonObject) jsonElement).addProperty("firstOctave", ((JsonObject) jsonElement).get("firstOctave").getAsInt() - BiomeSizeMod.config.getCommonConfig().biomeSizeModifier);
            }
        }
    }
}
