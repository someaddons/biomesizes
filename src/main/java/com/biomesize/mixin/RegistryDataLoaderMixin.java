package com.biomesize.mixin;

import com.biomesize.BiomeSizeMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Decoder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;

import static com.biomesize.BiomeSizeMod.adjustJsonData;

@Mixin(RegistryLoadTask.PendingRegistration.class)
public class RegistryDataLoaderMixin
{
    @Inject(method = "loadFromResource", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/conditions/ConditionalOps;createConditionalCodec(Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/Codec;"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static <E> void onLoad(
        final Decoder elementDecoder,
        final RegistryOps<JsonElement> ops,
        final ResourceKey resourceKey,
        final Resource thunk,
        final CallbackInfoReturnable<Either<Object, Exception>> cir,
        final Reader reader,
        final JsonElement jsonElement)
    {
        if (BiomeSizeMod.config.getCommonConfig().legacyMode)
        {
            if (BiomeSizeMod.adapted.containsKey(resourceKey.identifier()))
            {
                if (jsonElement instanceof JsonObject)
                {
                    ((JsonObject) jsonElement).addProperty("firstOctave",
                        ((JsonObject) jsonElement).get("firstOctave").getAsInt() - BiomeSizeMod.config.getCommonConfig().biomeSizeModifier);
                }
            }

            return;
        }

        if (resourceKey.toString().contains("worldgen/noise_settings"))
        {
            try
            {
                adjustJsonData(jsonElement, resourceKey.identifier());
            }
            catch (Exception e)
            {
                BiomeSizeMod.LOGGER.error("Failed to adjust:" + resourceKey.identifier() + " data:" + jsonElement, e);
            }
        }
    }
}
