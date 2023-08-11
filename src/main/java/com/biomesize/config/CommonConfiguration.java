package com.biomesize.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig {

    public int biomeSizeModifier = -2;

    public CommonConfiguration() {
    }

    public JsonObject serialize() {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Biome size modifier, added to the vanilla biome size. Vanilla value:0, min = -8 max = 8 : default:-2");
        entry.addProperty("biomeSizeModifier", biomeSizeModifier);
        root.add("biomeSizeModifier", entry);

        return root;
    }

    public void deserialize(JsonObject data) {
        biomeSizeModifier = data.get("biomeSizeModifier").getAsJsonObject().get("biomeSizeModifier").getAsInt();
    }
}
