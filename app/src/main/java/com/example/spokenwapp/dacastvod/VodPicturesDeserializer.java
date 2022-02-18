package com.example.spokenwapp.dacastvod;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

 public class VodPicturesDeserializer<T> implements JsonDeserializer<VodPictures> {

        @Override
        public VodPictures deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException
        {
            // Get the "content" element from the parsed JSON
            JsonElement vodPictures = je.getAsJsonObject().get("vodPictures");

            // Deserialize it. You use a new instance of Gson to avoid infinite recursion
            // to this deserializer
            return new Gson().fromJson(vodPictures, VodPictures.class);

        }

}
