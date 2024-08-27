package pers.beapoe.dissociativeamnesia;

import android.text.Html;
import android.text.SpannableStringBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SpannableStringBuilderAdapter implements JsonSerializer<SpannableStringBuilder>, JsonDeserializer<SpannableStringBuilder> {
    public SpannableStringBuilderAdapter(){}
    @Override
    public SpannableStringBuilder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new SpannableStringBuilder(Html.fromHtml(json.getAsString().trim(),Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public JsonElement serialize(SpannableStringBuilder src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Html.toHtml(src,Html.FROM_HTML_MODE_LEGACY));
    }
}
