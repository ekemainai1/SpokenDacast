package com.example.spokenwapp.dacast;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ShareCodeDeserializer implements JsonDeserializer<ShareCode> {

    @Override
    public ShareCode deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {
        // Get the "content" element from the parsed JSON
        JsonElement shareCode = je.getAsJsonObject().get("shareCode");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(shareCode, ShareCode.class);

    }
}
