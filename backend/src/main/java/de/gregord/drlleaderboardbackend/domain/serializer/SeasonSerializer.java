package de.gregord.drlleaderboardbackend.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.gregord.drlleaderboardbackend.domain.Season;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SeasonSerializer extends StdSerializer<Season> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public SeasonSerializer(Class<Season> t) {
        super(t);
    }

    @Override
    public void serialize(Season value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("idName", value.getSeasonIdName());
        gen.writeStringField("name", value.getSeasonName());
        gen.writeStringField("startDate", formatter.format(value.getSeasonStartDate()));
        gen.writeStringField("endDate", formatter.format(value.getSeasonEndDate()));
        if(value.getDetails_v1() != null){
            gen.writeObjectField("details_v1", value.getDetails_v1());
        }
        gen.writeEndObject();
    }
}
