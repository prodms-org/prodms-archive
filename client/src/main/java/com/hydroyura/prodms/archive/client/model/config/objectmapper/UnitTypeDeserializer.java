package com.hydroyura.prodms.archive.client.model.config.objectmapper;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hydroyura.prodms.archive.client.model.enums.UnitType;
import java.io.IOException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnitTypeDeserializer extends JsonDeserializer<UnitType> {

    @Override
    public UnitType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JacksonException {

        //TODO: add log message
        return UnitType.valueOf(jsonParser.getValueAsString().toUpperCase(Locale.getDefault()));
    }
}
