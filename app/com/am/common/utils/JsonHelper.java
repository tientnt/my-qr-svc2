package com.am.common.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Thomas-Pro-Win10 on 7/14/2017.
 */
public class JsonHelper {

    public static String getJsonNodeValue(JsonNode node, String fieldName, String defaultValue) {
        JsonNode findNode = node.findValue(fieldName);
        if (findNode == null) {
            return defaultValue;
        }
        return findNode.asText(defaultValue);
    }

    public static String toJson(Object source) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(source);
        } catch (JsonGenerationException e) {
            throw new IllegalStateException(e);
        } catch (JsonMappingException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Boolean checkExistAndEmptyArray(JsonNode node, String fieldName){
        if(node.has(fieldName) && node.findValue(fieldName).toString().replace(" ","").equals("[]"))
            return true;
        return false;
    }
}
