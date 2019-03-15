package com.am.common.utils;

import com.am.common.exception.AMException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AMObjectMapper extends ObjectMapper {

    private static play.Logger.ALogger logger = play.Logger.of(AMObjectMapper.class);

    public AMObjectMapper(boolean useSnakeCase) {
        if (useSnakeCase) {
            // 909: Rename PropertyNamingStrategy
            // CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES as SNAKE_CASE, PASCAL_CASE_TO_CAMEL_CASE as UPPER_CAMEL_CASE
            // https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.7
            this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        }
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public AMObjectMapper(PropertyNamingStrategy propertyNamingStrategy) {
        this.setPropertyNamingStrategy(propertyNamingStrategy);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static JsonNode toJsonNode(Object object, boolean useSnakeCase) {
        JsonNode result = null;
        try {
            if (object instanceof JsonNode) {
                result = (JsonNode) object;
            } else {
                ObjectMapper mapper = new AMObjectMapper(useSnakeCase).enable(SerializationFeature.INDENT_OUTPUT);
                if (object instanceof String) {
                    result = mapper.readTree((String) object);
                } else {
                    result = mapper.valueToTree(object);
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("Cannot convert %s to json %s", object.toString(), ex.getMessage()), ex);
            if (object instanceof String) {
                result = new TextNode((String) object);
            }
        }
        return result;
    }

    public static JsonNode toJsonNode(Object object, PropertyNamingStrategy propertyNamingStrategy) {
        JsonNode result = null;
        try {
            if (object instanceof JsonNode) {
                result = (JsonNode) object;
            } else {
                ObjectMapper mapper = new AMObjectMapper(propertyNamingStrategy).enable(SerializationFeature.INDENT_OUTPUT);
                if (object instanceof String) {
                    result = mapper.readTree((String) object);
                } else {
                    result = mapper.valueToTree(object);
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("Cannot convert %s to json %s", object.toString(), ex.getMessage()));
            if (object instanceof String) {
                result = new TextNode((String) object);
            }
        }
        return result;
    }

    public static <T> T toObject(JsonNode node, Class<T> clazz, boolean useSnakeCase) throws JsonProcessingException {
        return new AMObjectMapper(useSnakeCase).treeToValue(node, clazz);
    }

    public static <T> T toObject(String jsonString, Class<T> clazz, boolean usingSnakeCase) throws IOException {
        return new AMObjectMapper(usingSnakeCase).readValue(jsonString, clazz);
    }

    public static String toPrettyJsonString(JsonNode jsonNode) throws AMException {
        ObjectMapper mapper = new AMObjectMapper(true);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (JsonProcessingException jse) {
            throw new AMException(jse);
        }
    }

    public static String toJsonString(Object object, boolean useSnakeCase) {
        JsonNode jsonNode = toJsonNode(object, useSnakeCase);
        if (null != jsonNode) {
            return jsonNode.toString();
        }
        return null;
    }

    public static String toJsonString(Object object, PropertyNamingStrategy propertyNamingStrategy) {
        JsonNode jsonNode = toJsonNode(object, propertyNamingStrategy);
        if (null != jsonNode) {
            return jsonNode.toString();
        }
        return null;
    }

    public static String toPrettyJsonString(String jsonString) throws AMException {
        ObjectMapper mapper = new AMObjectMapper(true).enable(SerializationFeature.INDENT_OUTPUT);
        try {
            Object json = mapper.readValue(jsonString, Object.class);
            return mapper.writerWithDefaultPrettyPrinter()
                         .writeValueAsString(json);
        } catch (IOException ioe) {
            throw new AMException(ioe);
        }
    }

    public static String toPrettyJsonString(Object object) throws AMException {
        ObjectMapper mapper = new AMObjectMapper(true).enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                         .writeValueAsString(object);
        } catch (IOException ioe) {
            throw new AMException(ioe);
        }
    }
}
