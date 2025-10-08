package com.entitysc.euclase.fe.service;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.DataListResponsePayload;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.payload.ExceptionPayload;
import com.google.gson.JsonSyntaxException;
import java.util.Locale;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

/**
 *
 * @author briano
 */
@Service
public class DesignationService extends EuclaseService {

    @Autowired
    MessageSource messageSource;
    @Value("${euclasews.api.designation.list}")
    private String designationListUrl;
    @Value("${euclasews.api.designation.create}")
    private String createDesignationUrl;
    @Value("${euclasews.api.designation.update}")
    private String updateDesignationUrl;
    @Value("${euclasews.api.designation.delete}")
    private String deleteDesignationUrl;
    @Value("${euclasews.api.designation.fetch}")
    private String fetchDesignationUrl;

    public EuclaseResponsePayload processCreateDesignation(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Designation");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response;
            if (requestPayload.getId() == 0) {
                response = callEuclaseWSAPI(createDesignationUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Designation");
            } else {
                response = callEuclaseWSAPI(updateDesignationUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Designation");
            }
            //Check for error
            if (response.contains("error")) {
                ExceptionPayload responsePayload = gson.fromJson(response, ExceptionPayload.class);
                EuclaseResponsePayload exceptionResponse = new EuclaseResponsePayload();
                exceptionResponse.setResponseCode(responsePayload.getStatus());
                exceptionResponse.setResponseMessage(responsePayload.getMessage());
                return exceptionResponse;
            } else {
                EuclaseResponsePayload responsePayload = gson.fromJson(response, EuclaseResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            }
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            EuclaseResponsePayload responsePayload = new EuclaseResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @CacheEvict(value = "designation", key = "#id")
    public EuclaseResponsePayload processDeleteDesignation(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(deleteDesignationUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Designation Delete");
            //Check for error
            if (response.contains("error")) {
                ExceptionPayload responsePayload = gson.fromJson(response, ExceptionPayload.class);
                EuclaseResponsePayload exceptionResponse = new EuclaseResponsePayload();
                exceptionResponse.setResponseCode(responsePayload.getStatus());
                exceptionResponse.setResponseMessage(responsePayload.getMessage());
                return exceptionResponse;
            } else {
                EuclaseResponsePayload responsePayload = gson.fromJson(response, EuclaseResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            }
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            EuclaseResponsePayload responsePayload = new EuclaseResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Cacheable(value = "designation", key = "#id")
    public EuclaseResponsePayload fetchDesignation(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchDesignationUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Designation Fetch");
            //Check for error
            if (response.contains("error")) {
                ExceptionPayload responsePayload = gson.fromJson(response, ExceptionPayload.class);
                EuclaseResponsePayload exceptionResponse = new EuclaseResponsePayload();
                exceptionResponse.setResponseCode(responsePayload.getStatus());
                exceptionResponse.setResponseMessage(responsePayload.getMessage());
                return exceptionResponse;
            } else {
                EuclaseResponsePayload responsePayload = gson.fromJson(response, EuclaseResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            }
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            EuclaseResponsePayload responsePayload = new EuclaseResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @CacheEvict(value = "designation")
    public DataListResponsePayload fetchDesignationList() {
        try {
            EuclasePayload requestPayload = new EuclasePayload();
            requestPayload.setChannel("WEB");
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(designationListUrl, "GET", generateEuclaseWSAPIToken(), "Designation List");
            return gson.fromJson(response, DataListResponsePayload.class);
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

}
