package com.janickmediadb.janickmediaapi.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractEntity {

    @Override
    public String toString() {
        String result;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            result = mapper.writeValueAsString(this);
        } catch (JsonProcessingException _) {
            result = super.toString();
        }
        return result;
    }
}
