package it.lbsoftware.autoi18n.converters;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public enum JacksonConfig {
  INSTANCE;

  private ObjectMapper objectMapper;

  public ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      initObjectMapper();
    }
    return objectMapper;
  }

  private void initObjectMapper() {
    objectMapper =
        JsonMapper.builder()
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .build();
  }
}
