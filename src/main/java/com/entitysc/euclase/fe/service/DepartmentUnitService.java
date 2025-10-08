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
public class DepartmentUnitService extends EuclaseService {

    @Autowired
    MessageSource messageSource;
    @Value("${euclasews.api.departmentunit.department}")
    private String departmentUnitUrl;
    @Value("${euclasews.api.departmentunit.list}")
    private String departmentUnitListUrl;
    @Value("${euclasews.api.departmentunit.create}")
    private String createDepartmentUnitUrl;
    @Value("${euclasews.api.departmentunit.update}")
    private String updateDepartmentUnitUrl;
    @Value("${euclasews.api.departmentunit.delete}")
    private String deleteDepartmentUnitUrl;
    @Value("${euclasews.api.departmentunit.fetch}")
    private String fetchDepartmentUnitUrl;
    @Value("${euclasews.api.department.unit}")
    private String fetchUnitsInDepartmentUrl;

    public EuclaseResponsePayload processCreateDepartmentUnit(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DepartmentUnit");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response;
            if (requestPayload.getId() == 0) {
                response = callEuclaseWSAPI(createDepartmentUnitUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department Unit");
            } else {
                response = callEuclaseWSAPI(updateDepartmentUnitUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department Unit");
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

    @CacheEvict(value = "unit", key = "#id")
    public EuclaseResponsePayload processDeleteDepartmentUnit(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(deleteDepartmentUnitUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Department Unit Delete");
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

    @Cacheable(value = "unit", key = "#id")
    public EuclaseResponsePayload fetchDepartmentUnit(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchDepartmentUnitUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Department Unit Fetch");
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

    @Cacheable(value = "unit")
    public DataListResponsePayload fetchDepartmentUnitList() {
        try {
            EuclasePayload requestPayload = new EuclasePayload();
            requestPayload.setChannel("WEB");
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRecordType("All");
            requestPayload.setRecordId("0");
            requestPayload.setRequestType("RecordType");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(departmentUnitListUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department Unit List");
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

    @Cacheable(value = "unit", key = "#departmentCode")
    public DataListResponsePayload fetchDepartmentUnitList(String departmentCode) {
        try {
            String encodedParam = urlEncodeString(encryptString(departmentCode.trim()));
            String response = callEuclaseWSAPI(departmentUnitUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Department Unit Fetch");
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

    @Cacheable(value = "unit", key = "#department")
    public DataListResponsePayload fetchUnitsInDepartmentList(String department) {
        try {
            String encodedParam = urlEncodeString(encryptString(department.trim()));
            String response = callEuclaseWSAPI(fetchUnitsInDepartmentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Department Unit Fetch");
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
