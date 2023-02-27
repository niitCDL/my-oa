package top.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ResponseUtils {
    private String code;
    private String message;

    private Map<String,Object> data = new LinkedHashMap<>();

    public ResponseUtils() {
        code = "0";
        message = "success";
    }

    public ResponseUtils(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writer().writeValueAsString(this);
    }
}
