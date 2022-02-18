package com.example.spokenwapp.dacastanalytics;

import com.example.spokenwapp.dacast.DacastObject;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DacastAnalyticsDeserializer <T> implements JsonDeserializer<DacastChannelAnalytics> {

        @Override
        public  DacastChannelAnalytics deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException
        {
            // Get the "content" element from the parsed JSON
            JsonElement dacastChannelAnalytics = je.getAsJsonObject().get("dacastChannelAnalytics");

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(dacastChannelAnalytics, DacastChannelAnalytics.class);

        }

}
