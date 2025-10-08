package com.entitysc.euclase.fe.service;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.DataListResponsePayload;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.payload.ExceptionPayload;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
@Service
public class DocumentService extends EuclaseService {

    @Autowired
    MessageSource messageSource;

    @Value("${euclasews.api.document.create}")
    private String createDocumentUrl;
    @Value("${euclasews.api.document.process}")
    private String processDocumentUrl;
    @Value("${euclase.document.prefix.usegeneric}")
    private boolean useGenericDocumentPrefix;
    @Value("${euclase.document.prefix.generic}")
    private String genericDocumentPrefix;
    @Value("${euclasews.api.document.self}")
    private String fetchMyDocumentUrl;
    @Value("${euclasews.api.document.pending}")
    private String fetchPendingDocumentUrl;
    @Value("${euclasews.api.document.draft.fetch}")
    private String fetchDraftDocumentUrl;
    @Value("${euclasews.api.document.draft.delete}")
    private String processDeleteDraftDocumentUrl;
    @Value("${euclasews.api.document.uploaddelete}")
    private String processDeleteUploadDocumentUrl;
    @Value("${euclasews.api.document.approve}")
    private String processApproveDocumentUrl;
    @Value("${euclasews.api.document.search}")
    private String processSearchDocumentUrl;
    @Value("${euclasews.api.document.advancedsearch}")
    private String processAdvancedSearchDocumentUrl;
    @Value("${euclasews.api.document.signature}")
    private String processDocumentSignatureUrl;
    @Value("${euclasews.api.document.archive}")
    private String processDocumentArchivingUrl;
    @Value("${euclasews.api.document.access}")
    private String processDocumentAccessUrl;

    /**
     * ***************Document Template************************
     */
    @Value("${euclasews.api.document.template.update}")
    private String updateDocumentTemplateUrl;

    /**
     * ***************Document Workflow************************
     */
    @Value("${euclasews.api.document.workflow.update}")
    private String updateDocumentWorkflowUrl;
    @Value("${euclasews.api.document.details}")
    private String fetchDocumentDetailsUrl;
    @Value("${euclasews.api.document.workflow.history}")
    private String fetchDocumentWorkflowHistoryUrl;
    @Value("${euclasews.api.backup.list}")
    private String backupListUrl;
    @Value("${euclasews.api.backup.create}")
    private String createBackupUrl;
    @Value("${euclasews.api.backup.update}")
    private String updateBackupUrl;
    @Value("${euclasews.api.backup.delete}")
    private String deleteBackupUrl;
    @Value("${euclasews.api.backup.fetch}")
    private String fetchBackupUrl;
    @Value("${euclasews.api.restore.list}")
    private String restoreListUrl;
    @Value("${euclasews.api.restore.create}")
    private String createRestoreUrl;
    @Value("${euclase.document.upload.maxrecord}")
    private int maxRecordSizeToProcess;
    @Value("${euclase.temp.dir}")
    private String tempFileDirectory;
    @Value("${euclase.commareplacement}")
    private String commaReplacement;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Value("${euclasews.api.pushnotification.create}")
    private String createPushNotificationUrl;

    /**
     * ******** Report
     */
    @Value("${euclasews.api.document.report}")
    private String reportUrl;
    @Value("${euclasews.api.notification.report}")
    private String notificationReportUrl;

    public EuclaseResponsePayload processDocument(EuclasePayload requestPayload) {
        try {
            //Get the uploaded files out before resetting
            List<MultipartFile> files = new ArrayList<>();
            files.addAll(requestPayload.getUploadedFiles());
            requestPayload.setDocumentTemplateBody(requestPayload.getEditor());
            requestPayload.setUploadedFiles(null); //This removes the Multipart files as interfaces cannot be instantiated
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setStartDate(requestPayload.getStartDate() == null ? "" : requestPayload.getStartDate());
            requestPayload.setEndDate(requestPayload.getEndDate() == null ? "" : requestPayload.getEndDate());
            requestPayload.setAmount(requestPayload.getAmount() == null ? "" : requestPayload.getAmount());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentUpload");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(processDocumentUrl, gson.toJson(requestPayload), files, generateEuclaseWSAPIToken(), "Document Process");
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

    public EuclaseResponsePayload processCreateDocument(EuclasePayload requestPayload) {
        try {
            requestPayload.setDocumentTemplateBody(requestPayload.getEditorData());
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setStartDate(requestPayload.getStartDate() == null ? "" : requestPayload.getStartDate());
            requestPayload.setEndDate(requestPayload.getEndDate() == null ? "" : requestPayload.getEndDate());
            requestPayload.setAmount(requestPayload.getAmount() == null ? "" : requestPayload.getAmount());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Document");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(createDocumentUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Document");
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

    public String generateDocumentId(String companyName, String documentGroupCode, String documentTypeCode) {
        //Check if generic docuemtn prefix is set
        String documentId;
        if (useGenericDocumentPrefix) {
            documentId = genericDocumentPrefix + generateRequestId();
        } else {
            documentId = companyName + "-" + documentGroupCode + "-" + documentTypeCode + "-" + generateRequestId();
        }
        return documentId;
    }

    @CachePut(value = "documentTemplate", key = "{#a0.id}")
    public EuclaseResponsePayload processUpdateDocumentTemplate(EuclasePayload requestPayload) {
        try {
            requestPayload.setDocumentTemplateBody(requestPayload.getEditor());
            requestPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentTemplate");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(updateDocumentTemplateUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Document Template");
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

    @Cacheable(value = "myDocument", key = "#principal")
    public DataListResponsePayload fetchMyDocuments(String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(fetchMyDocumentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "My Documents Fetch");
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

//    @Cacheable(value = "pendingDocument", key = "#principal") Removed to prevent false positive
    public DataListResponsePayload fetchPendingDocuments(String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(fetchPendingDocumentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Pending Documents Fetch");
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

    @Cacheable(value = "draftDocument", key = "#principal")
    public DataListResponsePayload fetchDraftDocuments(String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(fetchDraftDocumentUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Draft Documents Fetch");
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

    @CachePut(value = "documentWorkflow", key = "{#a0.id}")
    public EuclaseResponsePayload processUpdateDocumentWorkflow(EuclasePayload requestPayload) {
        try {
            requestPayload.setDocumentTemplateBody(requestPayload.getDocumentWorkflowBody());
            requestPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            requestPayload.setDocumentType(requestPayload.getDocumentType());
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentTemplate");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(updateDocumentWorkflowUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Document Workflow");
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

    @Cacheable(value = "documentDetail", key = "#id")
    public DataListResponsePayload fetchDocumentDetails(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(fetchDocumentDetailsUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Documents Details");
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

    public EuclaseResponsePayload processApproveDocument(EuclasePayload requestPayload) {
        try {
            //Get the uploaded files out before resetting
            List<MultipartFile> files = new ArrayList<>();
            files.addAll(requestPayload.getUploadedFiles());
            requestPayload.setDocumentTemplateBody(requestPayload.getEditor());
            requestPayload.setUploadedFiles(null); //This removes the Multipart files as interfaces cannot be instantiated
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentApprove");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(processApproveDocumentUrl, gson.toJson(requestPayload), files, generateEuclaseWSAPIToken(), "Document Approve");
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

    @Cacheable(value = "documentWorkflow", key = "#id")
    public DataListResponsePayload fetchDocumentWorkflow(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchDocumentWorkflowHistoryUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Documents Workflow");
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

    @CacheEvict(value = "draftDocument", key = "#id")
    public EuclaseResponsePayload processDeleteDraftDocument(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(processDeleteDraftDocumentUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Delete Draft Documents");
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

    public EuclaseResponsePayload processDeleteUploadDocument(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(processDeleteUploadDocumentUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Delete Draft Documents");
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

//    @Cacheable(value = "searchDocument", key = "#search")
    public DataListResponsePayload processSearchDocument(String search, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(search.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(processSearchDocumentUrl + "?search=" + encodedParam + "&principal=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Search Documents");
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

    public DataListResponsePayload processAdvancedSearchDocument(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentSearch");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            String response = callEuclaseWSAPI(processAdvancedSearchDocumentUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Search Documents");
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

    public EuclaseResponsePayload processDocumentSignature(EuclasePayload requestPayload) {
        try {
            String encodedPrincipal = urlEncodeString(encryptString(requestPayload.getUsername().trim()));
            //Connect to EuclaseWS API
            List<MultipartFile> files = new ArrayList<>();
            files.add(requestPayload.getUploadedFile());
            String response = callEuclaseWSAPI(processDocumentSignatureUrl + "?prcp=" + encodedPrincipal, "GET", files, generateEuclaseWSAPIToken(), "Document Signature");
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

    public EuclaseResponsePayload processSingleDocumentUpload(EuclasePayload requestPayload) {
        try {
            //Get the uploaded files out before resetting
            List<MultipartFile> files = new ArrayList<>();
            files.addAll(requestPayload.getUploadedFiles());
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setUploadedFiles(null); //This removes the Multipart files as interfaces cannot be instantiated
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("DocumentArchiving");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(processDocumentArchivingUrl, gson.toJson(requestPayload), files, generateEuclaseWSAPIToken(), "Document Archiving");
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

    public DataListResponsePayload parseBatchDocumentUpload(EuclasePayload requestPayload) {
        try {
            //Check if the upload is empty
            if (requestPayload.getUploadedFiles().isEmpty()) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.FAILED_MODEL.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.emptyrecord", new Object[0], Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            //Check the file extension
            String extension = FilenameUtils.getExtension(requestPayload.getUploadedFile().getOriginalFilename());
            if (!"csv".equalsIgnoreCase(extension)) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.INVALID_TYPE.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.fileext", new Object[]{"CSV"}, Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            //Read the file for processing
            File newFile = new File(tempFileDirectory + "/" + requestPayload.getUploadedFile().getOriginalFilename() + ".csv");
            FileCopyUtils.copy(requestPayload.getUploadedFile().getBytes(), newFile);

            FileInputStream fis = new FileInputStream(newFile);
            String stringToParse = IOUtils.toString(fis);
            if (stringToParse.equalsIgnoreCase("\r\n")) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.FAILED_MODEL.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.emptyrecord", new Object[0], Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            //Check if the file is empty or has only the header (1 row)
            List<String> documentList = Arrays.asList(stringToParse.split("\\n"));
            if (documentList.isEmpty() || documentList.size() == 1) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.FAILED_MODEL.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.emptyrecord", new Object[0], Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            //Check the number of record to process at a time
            if (documentList.size() > maxRecordSizeToProcess) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.LIMIT_EXCEEDED.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.maxrecord", new Object[]{maxRecordSizeToProcess}, Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            //Check if the total record in the meta data matches number of uploaded files. Subtract the header
            if ((documentList.size() - 1) > requestPayload.getUploadedFiles().size()) {
                DataListResponsePayload responsePayload = new DataListResponsePayload();
                responsePayload.setResponseCode(ResponseCodes.NAME_MISMATCH.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.recordmismatch", new Object[]{"Meta data file", "Files uploaded"}, Locale.ENGLISH));
                responsePayload.setData(null);
                return responsePayload;
            }

            int index = 0;
            List<EuclasePayload> recordToProcess = new ArrayList<>();
            List<String> documentIds = new ArrayList<>();
            for (String str : documentList) {
                //Skip the header
                if (index != 0) {
                    StringBuilder strBuilder = new StringBuilder();
                    //Check if the number of Past Due is 1
                    String[] splitString = str.split(",");
                    //Check if the columns are complete
                    if (splitString.length == 5) {
                        boolean failedValidation = false;
                        //Check document id
                        if (splitString[0].equalsIgnoreCase("")) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Document Id missing ").append(splitString[0]).append("\r\n");
                        }

                        //Check if the document Id is unique
                        if (documentIds.contains(splitString[0].trim())) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Document Id exist ").append(splitString[0]).append("\r\n");
                        } else {
                            documentIds.add(splitString[0].trim());
                        }

                        //Check the document tag
                        if (splitString[1].equalsIgnoreCase("")) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Document tag missing ").append(splitString[1]).append("\r\n");
                        }

                        //Check the narration or comment
                        if (splitString[2].equalsIgnoreCase("")) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Narration is missing ").append(splitString[2]).append("\r\n");
                        }

                        //Check the access level is between 1 & 6
                        if (!splitString[3].matches("[1-6]{1}")) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Invalid access level ").append(splitString[3]).append("\r\n");
                        }

                        //Check the file path supplied
                        if (splitString[4].equalsIgnoreCase("")) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Missing file path ").append(splitString[4]).append("\r\n");
                        }

                        //Get the actual file to upload
                        Optional<MultipartFile> fileToUpload = null;
                        fileToUpload = requestPayload.getUploadedFiles().stream()
                                .filter(t -> t.getOriginalFilename().equalsIgnoreCase(splitString[4].replace("\r", ""))).findFirst();

                        if (fileToUpload.isEmpty()) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("Missing file ").append(splitString[4]).append("\r\n");
                        }

                        //Check the file size for each record
                        double acceptableFileSize = 0;
                        if (maxFileSize.contains("GB") || maxFileSize.contains("gb") || maxFileSize.contains("Gb")) {
                            Double tempSize = Double.valueOf(maxFileSize.replace("GB", "").replace("Gb", "").replace("gb", ""));
                            acceptableFileSize = tempSize * 1024 * 1024 * 1024;
                        } else if (maxFileSize.contains("MB") || maxFileSize.contains("mb") || maxFileSize.contains("Mb")) {
                            Double tempSize = Double.valueOf(maxFileSize.replace("MB", "").replace("Mb", "").replace("mb", ""));
                            acceptableFileSize = tempSize * 1024 * 1024;
                        } else {
                            Double tempSize = Double.valueOf(maxFileSize.replace("KB", "").replace("Kb", "").replace("kb", ""));
                            acceptableFileSize = tempSize * 1024;
                        }

                        if (!fileToUpload.isEmpty() && fileToUpload.get().getSize() > acceptableFileSize) {
                            failedValidation = true;
                            strBuilder.append("Record [").append(index).append("] - ").append("File size exceed ").append(maxFileSize).append("\r\n");
                        }

                        //Add the record to the list to process if passed validation
                        if (failedValidation) {
                            DataListResponsePayload responsePayload = new DataListResponsePayload();
                            responsePayload.setResponseCode(ResponseCodes.FAILED_TRANSACTION.getResponseCode());
                            responsePayload.setResponseMessage(strBuilder.toString());
                            responsePayload.setData(null);
                            return responsePayload;
                        } else {
                            EuclasePayload docPayload = new EuclasePayload();
                            docPayload.setDocumentId(splitString[0]);
                            docPayload.setTag(splitString[1].replace(commaReplacement, ","));
                            docPayload.setNarration(splitString[2]);
                            docPayload.setAccessLevel(splitString[3]);
                            docPayload.setUploadedFile(fileToUpload.get());
                            recordToProcess.add(docPayload);
                        }
                    } else {
                        //Enter log
                        strBuilder.append("Record [").append(index).append("] - ").append("Invalid data column. Found ").append(splitString.length).append(" data points").append("\r\n");
                        DataListResponsePayload responsePayload = new DataListResponsePayload();
                        responsePayload.setResponseCode(ResponseCodes.FAILED_MODEL.getResponseCode());
                        responsePayload.setResponseMessage(messageSource.getMessage("appMessages.invalid.columncount", new Object[]{splitString.length, 5}, Locale.ENGLISH));
                        responsePayload.setData(null);
                        return responsePayload;
                    }
                }
                index++;
            }

            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode(ResponseCodes.SUCCESS_CODE.getResponseCode());
            responsePayload.setResponseMessage(messageSource.getMessage("appMessages.success.fileprocessing", new Object[0], Locale.ENGLISH));
            responsePayload.setData(recordToProcess);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException | IOException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setData(null);
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    public void processBatchDocumentUpload(List<EuclasePayload> recordToProcess, String principal) {
        try {
            //Set the start time
            LocalDateTime startTime = LocalDateTime.now();
            for (EuclasePayload documentPayload : recordToProcess) {
                //Get the uploaded files out before resetting
                List<MultipartFile> files = new ArrayList<>();
                files.add(documentPayload.getUploadedFile());
                documentPayload.setChannel("WEB");
                documentPayload.setRequestBy(principal);
                documentPayload.setUsername(principal);
                documentPayload.setRequestId(generateRequestId());
                documentPayload.setUploadedFiles(null); //This removes the Multipart files as interfaces cannot be instantiated
                documentPayload.setUploadedFile(null); //This removes the Multipart file as interfaces cannot be instantiated
                documentPayload.setToken(generateEuclaseWSAPIToken());
                documentPayload.setRequestType("DocumentArchiving");
                documentPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), documentPayload));
                //Connect to EuclaseWS API
                callEuclaseWSAPI(processDocumentArchivingUrl, gson.toJson(documentPayload), files, generateEuclaseWSAPIToken(), "Document Archiving");
            }

            //Set the end time for the backup
            LocalDateTime endTime = LocalDateTime.now();
            long processingTime = Duration.between(startTime, endTime).toMinutes();
            //Send push notification to the user
            Random random = new Random(1000);
            EuclasePayload notificationPayload = new EuclasePayload();
            notificationPayload.setUsername(principal);
            notificationPayload.setBatchId(random.nextInt());
            notificationPayload.setSentTo(principal);
            notificationPayload.setMessage("Batch upload completed. A total of " + recordToProcess.size() + " records processed in " + processingTime + " minutes");
            notificationPayload.setChannel("WEB");
            notificationPayload.setRequestBy(principal);
            notificationPayload.setRequestId(generateRequestId());
            notificationPayload.setToken(generateEuclaseWSAPIToken());
            notificationPayload.setRequestType("PushNotification");
            notificationPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), notificationPayload));
            //Connect to EuclaseWS API
            callEuclaseWSAPI(createPushNotificationUrl, gson.toJson(notificationPayload), generateEuclaseWSAPIToken(), "Push Notification");
        } catch (Exception ex) {
            logger.info("Batch upload error: " + ex.getMessage());
        }
    }

    public String processDocumentAccess(String seid, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(seid.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            return callEuclaseWSAPI(processDocumentAccessUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Documents Access");
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return ex.getMessage();
        }
    }

    public EuclaseResponsePayload processCreateBackup(EuclasePayload requestPayload) {
        try {
            requestPayload.setChannel("WEB");
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Backup");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response;
            if (requestPayload.getId() == 0) {
                response = callEuclaseWSAPI(createBackupUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Backup");
            } else {
                response = callEuclaseWSAPI(updateBackupUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Backup");
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

    @Cacheable(value = "backup", key = "#id")
    public EuclaseResponsePayload fetchBackup(String id) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(fetchBackupUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Backup Fetch");
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

    @CacheEvict(value = "backup", key = "#id")
    public EuclaseResponsePayload processDeleteBackup(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String encodedPrincipal = urlEncodeString(encryptString(principal.trim()));
            String response = callEuclaseWSAPI(deleteBackupUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", generateEuclaseWSAPIToken(), "Backup Delete");
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

    @Cacheable(value = "backup")
    public DataListResponsePayload fetchBackupList() {
        try {
            String response = callEuclaseWSAPI(backupListUrl, "GET", generateEuclaseWSAPIToken(), "Backup List");
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

    public EuclaseResponsePayload processCreateRestore(String id, String principal) {
        try {
            String encodedParam = urlEncodeString(encryptString(id.trim()));
            String response = callEuclaseWSAPI(createRestoreUrl + "?id=" + encodedParam, "GET", generateEuclaseWSAPIToken(), "Restore");
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

    @Cacheable(value = "restore")
    public DataListResponsePayload fetchRestoreList() {
        try {
            String response = callEuclaseWSAPI(restoreListUrl, "GET", generateEuclaseWSAPIToken(), "Restore List");
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

    public DataListResponsePayload processReports(EuclasePayload requestPayload) {
        try {
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Report");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(reportUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Reports");
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

    public DataListResponsePayload processNotificationReports(EuclasePayload requestPayload) {
        try {
            requestPayload.setRequestBy(requestPayload.getUsername());
            requestPayload.setRequestId(generateRequestId());
            requestPayload.setToken(generateEuclaseWSAPIToken());
            requestPayload.setRequestType("Report");
            requestPayload.setHash(generateRequestString(generateEuclaseWSAPIToken(), requestPayload));
            //Connect to EuclaseWS API
            String response = callEuclaseWSAPI(notificationReportUrl, gson.toJson(requestPayload), generateEuclaseWSAPIToken(), "Reports");
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
