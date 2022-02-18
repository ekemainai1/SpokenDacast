package com.example.spokenwapp.dacastvod;

import com.example.spokenwapp.dacast.DacastObject;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DacastVODObjectDeserializer<T> implements JsonDeserializer<DacastVODObject> {

    @Override
    public DacastVODObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "content" element from the parsed JSON
        JsonElement dacastObject = je.getAsJsonObject().get("dacastVODObject");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(dacastObject, DacastVODObject.class);

    }
}
