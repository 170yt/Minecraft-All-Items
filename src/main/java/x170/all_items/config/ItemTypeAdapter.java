package x170.all_items.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.io.IOException;

public class ItemTypeAdapter extends TypeAdapter<Item> {
    public Item read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String str = reader.nextString();
        return BuiltInRegistries.ITEM.getValue(Identifier.parse(str));
    }

    public void write(JsonWriter writer, Item value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value.toString());
    }
}
