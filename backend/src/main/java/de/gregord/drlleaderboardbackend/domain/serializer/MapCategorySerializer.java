package de.gregord.drlleaderboardbackend.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.gregord.drlleaderboardbackend.entities.MapCategory;

import java.io.IOException;

public class MapCategorySerializer extends StdSerializer<MapCategory> {

    public MapCategorySerializer() {
        super(MapCategory.class);
    }

    @Override
    public void serialize(MapCategory value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.ordinal());
        gen.writeStringField("name", value.name());
        gen.writeStringField("description", value.getDescription());
        gen.writeEndObject();
    }
}

