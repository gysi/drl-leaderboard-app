package de.gregord.drlleaderboardbackend.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.gregord.drlleaderboardbackend.domain.Season;

import java.io.IOException;

public class SeasonSerializer extends StdSerializer<Season> {

    private final ObjectMapper objectMapper;

    protected SeasonSerializer(ObjectMapper objectMapper) {
        super(Season.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(Season value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.ordinal());
        gen.writeStringField("idName", value.getSeasonIdName());
        gen.writeStringField("name", value.getSeasonName());
        gen.writeFieldName("startDate");
        objectMapper.writeValue(gen, value.getSeasonStartDate());
        gen.writeFieldName("endDate");
        objectMapper.writeValue(gen, value.getSeasonEndDate());
        gen.writeEndObject();
    }
}
