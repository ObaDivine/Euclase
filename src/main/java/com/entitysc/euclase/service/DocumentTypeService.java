package com.entitysc.euclase.service;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.payload.ExceptionPayload;
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
public class DocumentTypeService extends EuclaseService {

    @Autowired
    MessageSource messageSource;
    @Value("${euclasews.api.document.type.list}")
    private String documentTypeListUrl;
    @Value("${euclasews.api.document.type.create}")
    private String createDocumentTypeUrl;
    @Value("${euclasews.api.document.type.update}")
    private String updateDocumentTypeUrl;
    @Value("${euclasews.api.document.type.delete}")
    private String deleteDocumentTypeUrl;
    @Value("${euclasews.api.document.type.fetch}")
    private String fetchDocumentTypeUrl;
    @Value("${euclasews.api.document.group.type}")
    private String fetchDocumentGroupTypeUrl;

    public EuclaseResponsePayload processCreateDocumentType(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentType");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response;
            if (requestPayload.getId() == 0) {
                response = callEuclaseWSAPI(createDocumentTypeUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Document Type");
            } else {
                response = callEuclaseWSAPI(updateDocumentTypeUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "DOcument Type");
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

    @CacheEvict(value = "documentType", key = "#id")
    public EuclaseResponsePayload processDeleteDocumentType(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(deleteDocumentTypeUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Document Type Delete");
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

    @Cacheable(value = "documentType", key = "#id")
    public EuclaseResponsePayload fetchDocumentType(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchDocumentTypeUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Document Type Fetch");
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

    @Cacheable(value = "documentType")
    public DataListResponsePayload fetchDocumentTypeList(String recordType, String recordId) {
        try {
            EuclasePayload requestPayload = new EuclasePayload();
            requestPayload.setChannel("WEB");
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRecordType(recordType);
            requestPayload.setRecordId(recordId);
            requestPayload.setRequestType("RecordType");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(documentTypeListUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Document Type List");
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

    @Cacheable(value = "group", key = "#group")
    public DataListResponsePayload fetchDocumentGroupTypeList(String group) {
        try {
            String encodedParam = urlEncodeString(encryptString(group.trim()));
            String response = callEuclaseWSAPI(fetchDocumentGroupTypeUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Document Group Type Fetch");
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
