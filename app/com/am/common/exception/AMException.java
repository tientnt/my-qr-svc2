package com.am.common.exception;

import com.am.common.utils.CommonConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import play.Logger;
import play.data.validation.ValidationError;
import play.libs.Json;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class AMException extends Exception {

    private final String amMessage = "AppliedMesh Exception";

    private final String errorId = RandomStringUtils.random(16, true, false);

    private final String timeStamp = ZonedDateTime.ofInstant(Instant.now(), CommonConstants.SINGAPORE_ZONE_ID)
                                                  .format(CommonConstants.EXCEPTION_DATE_TIME_FORMATTER);

    private Logger.ALogger logger = Logger.of(AMException.class);

    private String errorMessage =
            "We have encountered an unexpected condition which prevented us from fulfilling the " +
            "request. Sorry for the inconvenience caused.";

    private ObjectMapper mapper = new ObjectMapper();

    private int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;

    private Map<String, List<ValidationError>> validationError;

    private ObjectNode ameNode = null;

    private boolean displayExceptionMessageToUser = false;

    public AMException() {
        super();
    }

    public AMException(String message) {
        super(message);
    }

    public AMException(String message, boolean displayExceptionMessageToUser) {
        super(message);
        this.displayExceptionMessageToUser = displayExceptionMessageToUser;
        composeErrorMessageToDisplayToUser();
    }

    public AMException(String message, Throwable cause) {
        super(message, cause);
        logger.error(errorId + "\n" + message, cause);
    }

    public AMException(Throwable cause) {
        super(cause);
        logger.error(errorId, cause);
    }

    public AMException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        composeErrorMessageToDisplayToUser();
        logger.error(amMessage + "\n" + toJsonString(), this);
    }

    public AMException(int statusCode, String message, boolean displayExceptionMessageToUser) {
        super(message);
        this.statusCode = statusCode;
        this.displayExceptionMessageToUser = displayExceptionMessageToUser;
        composeErrorMessageToDisplayToUser();
        logger.error(amMessage + "\n" + toJsonString(), this);
    }

    public AMException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        if (displayExceptionMessageToUser) {
            errorMessage = message;
        } else {
            composeErrorMessageToDisplayToUser();
        }
        logger.error(amMessage + "\n" + toJsonString(), cause);
    }

    public AMException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        composeErrorMessageToDisplayToUser();
        logger.error(amMessage + "\n" + toJsonString(), cause);
    }

    public AMException(int statusCode, Map<String, List<ValidationError>> validationError) {
        super();
        this.statusCode = statusCode;
        this.validationError = validationError;
        composeErrorMessageToDisplayToUser();
        logger.error(amMessage + "\n" + toJsonString(), this);

    }

    public AMException(int statusCode, String message, Map<String, List<ValidationError>> validationError) {
        super(message);
        this.statusCode = statusCode;
        this.validationError = validationError;
        composeErrorMessageToDisplayToUser();
        logger.error(amMessage + "\n" + toJsonString(), this);
    }

    private void composeErrorMessageToDisplayToUser() {
        if (displayExceptionMessageToUser) {
            errorMessage = super.getMessage();
        } else {
            /*errorMessage = MessageServices.getGenericErrorMessage500();

            if(statusCode == 400){
                errorMessage = MessageServices.getGenericErrorMessage400();
            } else if (statusCode == 500){
                errorMessage = MessageServices.getGenericErrorMessage500();
            }*/
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        if (null == validationError) {
            return amMessage + errorMessage + super.getMessage();
        } else {
            StringBuilder sb = new StringBuilder(amMessage);
            sb.append(super.getMessage()).append("\n");
            sb.append(errorMessage).append("\n");
            sb.append("Validation Error: \n");
            sb.append(validationError.toString());
            return sb.toString();
        }
    }

    private ObjectNode generateObjectNode() {
        ObjectNode ameNode = mapper.createObjectNode();
        ObjectNode detailsNode = mapper.createObjectNode();

        detailsNode.put("time_stamp", timeStamp);
        detailsNode.put("error_id", errorId);
        detailsNode.put("status_code", statusCode);
        detailsNode.put("error_message", errorMessage);
        detailsNode.put("exception_message", super.getMessage());
        if (null != validationError) {
            ObjectNode valitationErrorNode = mapper.createObjectNode();
            for (String k : validationError.keySet()) {
                ObjectNode vErrorMapNode = mapper.createObjectNode();
                ArrayNode validationErrorArrNode = vErrorMapNode.putArray("validation_errors");
                for (ValidationError ve : validationError.get(k)) {
                    ObjectNode veNode = mapper.createObjectNode();
                    veNode.put("key", ve.key());
                    ArrayNode messagesNode = veNode.putArray("messages");
                    for (String s : ve.messages()) {
                        messagesNode.add(s);
                    }
                    ArrayNode argsNode = veNode.putArray("arguments");
                    for (Object o : ve.arguments()) {
                        JsonNode jo = Json.toJson(o);
                        argsNode.add(jo);
                    }
                    validationErrorArrNode.addPOJO(veNode);
                }
                valitationErrorNode.putPOJO(k, validationErrorArrNode);
            }
            detailsNode.putPOJO("validation_error", valitationErrorNode);
        }
        ameNode.putPOJO("appliedmesh_exception", detailsNode);
        return ameNode;
    }

    public ObjectNode getAMExceptionAsObjectNode() {
        if (null == ameNode) {
            ameNode = generateObjectNode();
        }
        return ameNode;
    }

    public String toJsonString() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(getAMExceptionAsObjectNode());
        } catch (JsonProcessingException jse) {
            return getAMExceptionAsObjectNode().toString();
        }
    }

}
