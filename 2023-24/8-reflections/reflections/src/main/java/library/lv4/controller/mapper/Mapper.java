package library.lv4.controller.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import library.lv0.crosscutting.di.Dependency;
import library.lv4.controller.ControllerAppException;

@Dependency
public class Mapper {
    public static final String CONTENT_TYPE = "application/json";
    private final ObjectMapper mapper;

    public Mapper() {
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getContentType() {
        return CONTENT_TYPE;
    }

    public <T> String serialize(T object) {
        try {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ControllerAppException(e);
        }
    }

    public <T> T deserialize(String s, Class<T> type) {
        try {
            return mapper.readValue(s, type);
        }
        catch (Exception e) {
            throw new ControllerAppException(e);
        }
    }
}
