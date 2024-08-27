package pers.beapoe.dissociativeamnesia;

import android.text.style.ForegroundColorSpan;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ForegroundColorSpanAdapter implements JsonSerializer<ForegroundColorSpan>, JsonDeserializer<ForegroundColorSpan> {
    public ForegroundColorSpanAdapter(){}
    @Override
    public ForegroundColorSpan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ForegroundColorSpan(json.getAsInt());
    }

    @Override
    public JsonElement serialize(ForegroundColorSpan src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getForegroundColor());
    }
}
