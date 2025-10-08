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
public class DepartmentService extends EuclaseService {

    @Autowired
    MessageSource messageSource;
    @Value("${euclasews.api.department.list}")
    private String departmentListUrl;
    @Value("${euclasews.api.department.create}")
    private String createDepartmentUrl;
    @Value("${euclasews.api.department.update}")
    private String updateDepartmentUrl;
    @Value("${euclasews.api.department.delete}")
    private String deleteDepartmentUrl;
    @Value("${euclasews.api.department.fetch}")
    private String fetchDepartmentUrl;
    @Value("${euclasews.api.branch.department}")
    private String fetchBranchDepartmentUrl;

    public EuclaseResponsePayload processCreateDepartment(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Department");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response;
            if (requestPayload.getId() == 0) {
                response = callEuclaseWSAPI(createDepartmentUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department");
            } else {
                response = callEuclaseWSAPI(updateDepartmentUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department");
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

    @Cacheable(value = "department")
    public DataListResponsePayload fetchDepartmentList() {
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
            String response = callEuclaseWSAPI(departmentListUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Department List");
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

    @CacheEvict(value = "department", key = "#id")
    public EuclaseResponsePayload processDeleteDepartment(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(deleteDepartmentUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Department Delete");
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

    @Cacheable(value = "department", key = "#id")
    public EuclaseResponsePayload fetchDepartment(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchDepartmentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Department Fetch");
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

    @Cacheable(value = "branch", key = "#branch")
    public DataListResponsePayload fetchBranchDepartmentList(String branch) {
        try {
            String encodedParam = urlEncodeString(encryptString(branch.trim()));
            String response = callEuclaseWSAPI(fetchBranchDepartmentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Branch Department Fetch");
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
