package com.gome.test.gtp.dao.mongodb;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonReportAdapter {

    public <T> T adapter(String json, Class<T> cls) throws IOException {
        if (json == null || json.isEmpty())
            return null;

        ObjectMapper mapper = new ObjectMapper();
//            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, cls);
    }
}
