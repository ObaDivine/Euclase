package com.entitysc.euclase.service;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonPayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
@Service
public class EuclaseServiceImpl implements EuclaseService {

    @Autowired
    GenericService genericService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    Gson gson;
    @Value("${pylon.api.signin}")
    private String signInUrl;
    @Value("${pylon.api.changedefaultpassword}")
    private String changeDefaultPasswordUrl;
    @Value("${pylon.api.changepassword}")
    private String changePasswordUrl;
    @Value("${pylon.api.forgotpassword}")
    private String forgotPasswordUrl;
    @Value("${pylon.api.profile.details}")
    private String profileDetailsUrl;
    /**
     * ******** Department **************
     */
    @Value("${pylon.api.department.list}")
    private String departmentListUrl;
    @Value("${pylon.api.department.create}")
    private String createDepartmentUrl;
    @Value("${pylon.api.department.update}")
    private String updateDepartmentUrl;
    @Value("${pylon.api.department.delete}")
    private String deleteDepartmentUrl;
    @Value("${pylon.api.department.fetch}")
    private String fetchDepartmentUrl;
    /**
     * ******** Designation **************
     */
    @Value("${pylon.api.designation.list}")
    private String designationListUrl;
    @Value("${pylon.api.designation.create}")
    private String createDesignationUrl;
    @Value("${pylon.api.designation.update}")
    private String updateDesignationUrl;
    @Value("${pylon.api.designation.delete}")
    private String deleteDesignationUrl;
    @Value("${pylon.api.designation.fetch}")
    private String fetchDesignationUrl;
    /**
     * ******** Department Unit **************
     */
    @Value("${pylon.api.departmentunit.department}")
    private String departmentUnitUrl;
    @Value("${pylon.api.departmentunit.list}")
    private String departmentUnitListUrl;
    @Value("${pylon.api.departmentunit.create}")
    private String createDepartmentUnitUrl;
    @Value("${pylon.api.departmentunit.update}")
    private String updateDepartmentUnitUrl;
    @Value("${pylon.api.departmentunit.delete}")
    private String deleteDepartmentUnitUrl;
    @Value("${pylon.api.departmentunit.fetch}")
    private String fetchDepartmentUnitUrl;
    /**
     * ******** Branch **************
     */
    @Value("${pylon.api.branch.list}")
    private String branchListUrl;
    @Value("${pylon.api.branch.create}")
    private String createBranchUrl;
    @Value("${pylon.api.branch.update}")
    private String updateBranchUrl;
    @Value("${pylon.api.branch.delete}")
    private String deleteBranchUrl;
    @Value("${pylon.api.branch.fetch}")
    private String fetchBranchUrl;
    /**
     * ******** Grade Level **************
     */
    @Value("${pylon.api.gradelevel.list}")
    private String gradeLevelListUrl;
    @Value("${pylon.api.gradelevel.create}")
    private String createGradeLevelUrl;
    @Value("${pylon.api.gradelevel.update}")
    private String updateGradeLevelUrl;
    @Value("${pylon.api.gradelevel.delete}")
    private String deleteGradeLevelUrl;
    @Value("${pylon.api.gradelevel.fetch}")
    private String fetchGradeLevelUrl;
    /**
     * ******** Document Group **************
     */
    @Value("${pylon.api.document.group.list}")
    private String documentGroupListUrl;
    @Value("${pylon.api.document.group.create}")
    private String createDocumentGroupUrl;
    @Value("${pylon.api.document.group.update}")
    private String updateDocumentGroupUrl;
    @Value("${pylon.api.document.group.delete}")
    private String deleteDocumentGroupUrl;
    @Value("${pylon.api.document.group.fetch}")
    private String fetchDocumentGroupUrl;

    /**
     * ******** Document Type **************
     */
    @Value("${pylon.api.document.type.list}")
    private String documentTypeListUrl;
    @Value("${pylon.api.document.type.create}")
    private String createDocumentTypeUrl;
    @Value("${pylon.api.document.type.update}")
    private String updateDocumentTypeUrl;
    @Value("${pylon.api.document.type.delete}")
    private String deleteDocumentTypeUrl;
    @Value("${pylon.api.document.type.fetch}")
    private String fetchDocumentTypeUrl;
    @Value("${pylon.api.document.create}")
    private String createDocumentUrl;
    @Value("${pylon.api.document.process}")
    private String processDocumentUrl;

    /**
     * ***************Document************************
     */
    @Value("${euclase.document.prefix.usegeneric}")
    private boolean useGenericDocumentPrefix;
    @Value("${euclase.document.prefix.generic}")
    private String genericDocumentPrefix;
    @Value("${pylon.api.document.self}")
    private String fetchMyDocumentUrl;
    @Value("${pylon.api.document.pending}")
    private String fetchPendingDocumentUrl;
    @Value("${pylon.api.document.draft.fetch}")
    private String fetchDraftDocumentUrl;
    @Value("${pylon.api.document.draft.delete}")
    private String processDeleteDraftDocumentUrl;
    @Value("${pylon.api.document.approve}")
    private String processApproveDocumentUrl;
    @Value("${pylon.api.document.search}")
    private String processSearchDocumentUrl;
    @Value("${pylon.api.document.signature}")
    private String processDocumentSignatureUrl;
    @Value("${pylon.api.document.archive}")
    private String processDocumentArchivingUrl;

    /**
     * ***************Document Template************************
     */
    @Value("${pylon.api.document.template.update}")
    private String updateDocumentTemplateUrl;

    /**
     * ***************Document Workflow************************
     */
    @Value("${pylon.api.document.workflow.update}")
    private String updateDocumentWorkflowUrl;
    @Value("${pylon.api.document.details}")
    private String fetchDocumentDetailsUrl;
    @Value("${pylon.api.document.workflow.history}")
    private String fetchDocumentWorkflowHistoryUrl;

    /**
     * ************ App User ****************
     */
    @Value("${pylon.api.appuser.list}")
    private String appUserListUrl;
    @Value("${pylon.api.appuser.create}")
    private String createAppUserUrl;
    @Value("${pylon.api.appuser.update}")
    private String updateAppUserUrl;
    @Value("${pylon.api.appuser.delete}")
    private String deleteAppUserUrl;
    @Value("${pylon.api.appuser.fetch}")
    private String fetchAppUserUrl;
    @Value("${pylon.api.appuser.role.list}")
    private String listRoleGroupUrl;
    @Value("${pylon.api.appuser.role.fetch}")
    private String fetchRoleGroupUrl;
    @Value("${pylon.api.appuser.role.update}")
    private String updateRoleGroupUrl;
    @Value("${pylon.api.appuser.role.create}")
    private String createRoleGroupUrl;
    @Value("${pylon.api.appuser.role.delete}")
    private String deleteRoleGroupUrl;
    @Value("${pylon.api.appuser.group.fetch}")
    private String fetchGroupRolesUrl;
    @Value("${pylon.api.appuser.group.update}")
    private String updateGroupRolesUrl;
    @Value("${pylon.api.appuser.generic.update}")
    private String updateAppUserGenericUrl;

    /**
     * ******** Public Holiday **************
     */
    @Value("${pylon.api.holidays.list}")
    private String publicHolidayListUrl;
    @Value("${pylon.api.holidays.create}")
    private String createPublicHolidayUrl;
    @Value("${pylon.api.holidays.update}")
    private String updatePublicHolidayUrl;
    @Value("${pylon.api.holidays.delete}")
    private String deletePublicHolidayUrl;
    @Value("${pylon.api.holidays.fetch}")
    private String fetchPublicHolidayUrl;

    /**
     * ******** Service Level Agreement **************
     */
    @Value("${pylon.api.sla.list}")
    private String slaListUrl;
    @Value("${pylon.api.sla.create}")
    private String createSlaUrl;
    @Value("${pylon.api.sla.update}")
    private String updateSlaUrl;
    @Value("${pylon.api.sla.delete}")
    private String deleteSlaUrl;
    @Value("${pylon.api.sla.fetch}")
    private String fetchSlaUrl;

    /**
     * ******** Backup and Restore **************
     */
    @Value("${pylon.api.backup.list}")
    private String backupListUrl;
    @Value("${pylon.api.backup.create}")
    private String createBackupUrl;
    @Value("${pylon.api.backup.update}")
    private String updateBackupUrl;
    @Value("${pylon.api.backup.delete}")
    private String deleteBackupUrl;
    @Value("${pylon.api.backup.fetch}")
    private String fetchBackupUrl;
    @Value("${pylon.api.restore.list}")
    private String restoreListUrl;
    @Value("${pylon.api.restore.create}")
    private String createRestoreUrl;

    /**
     * ******** Report
     */
    @Value("${pylon.api.document.report}")
    private String reportUrl;

    /**
     * ******** Notification **************
     */
    @Value("${pylon.api.notification.list}")
    private String notificationListUrl;
    @Value("${pylon.api.notification.create}")
    private String createNotificationUrl;
    @Value("${pylon.api.notification.update}")
    private String updateNotificationUrl;
    @Value("${pylon.api.notification.delete}")
    private String deleteNotificationUrl;
    @Value("${pylon.api.notification.fetch}")
    private String fetchNotificationUrl;

    @Value("${pylon.api.pushnotification.list}")
    private String pushNotificationListUrl;
    @Value("${pylon.api.pushnotification.create}")
    private String createPushNotificationUrl;
    @Value("${pylon.api.pushnotification.update}")
    private String updatePushNotificationUrl;
    @Value("${pylon.api.pushnotification.delete}")
    private String deletePushNotificationUrl;
    @Value("${pylon.api.pushnotification.fetch}")
    private String fetchPushNotificationUrl;
    @Value("${pylon.api.pushnotification.selflist}")
    private String selfPushNotificationListUrl;
    @Value("${pylon.api.pushnotification.selfupdate}")
    private String updateSelfPushNotificationUrl;

    @Override
    public PylonResponsePayload processSignin(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("SignIn");
            pylonPayload.setAppType("Euclase");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(signInUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Login");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload changePassword(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("ChangePassword");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changePasswordUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Change Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload forgotPassword(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("ForgotPassword");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(forgotPasswordUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Forgot Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "userProfile", key = "#username")
    public DataListResponsePayload processFetchProfileDetails(String username) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Dashboard");
            //Connect to Pylon API
            String usernameEncode = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String appTypeEncode = genericService.urlEncodeString(genericService.encryptString("Euclase"));
            String response = genericService.callPylonAPI(profileDetailsUrl + "?id=" + usernameEncode + "&appType=" + appTypeEncode, "GET", genericService.generatePylonAPIToken(), "Dashboard");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            if (responsePayload.getPayload().getStatus() != null && responsePayload.getPayload().getError() != null && responsePayload.getPayload().getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
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

    @Override
    @Cacheable(value = "department")
    public DataListResponsePayload processFetchDepartmentList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(departmentListUrl, "GET", genericService.generatePylonAPIToken(), "Department List");
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

    @Override
    public PylonResponsePayload processCreateDepartment(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Department");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDepartmentUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Department");
            } else {
                response = genericService.callPylonAPI(updateDepartmentUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Department");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "department", key = "#id")
    public PylonResponsePayload processDeleteDepartment(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteDepartmentUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Department Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "department", key = "#id")
    public PylonResponsePayload processFetchDepartment(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDepartmentUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Department Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDepartmentUnit(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DepartmentUnit");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDepartmentUnitUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Department Unit");
            } else {
                response = genericService.callPylonAPI(updateDepartmentUnitUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Department Unit");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "unit", key = "#id")
    public PylonResponsePayload processDeleteDepartmentUnit(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteDepartmentUnitUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Department Unit Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "unit", key = "#id")
    public PylonResponsePayload processFetchDepartmentUnit(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDepartmentUnitUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Department Unit Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "unit")
    public DataListResponsePayload processFetchDepartmentUnitList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(departmentUnitListUrl, "GET", genericService.generatePylonAPIToken(), "Department Unit List");
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

    @Override
    @Cacheable(value = "unit", key = "#departmentCode")
    public DataListResponsePayload processFetchDepartmentUnitList(String departmentCode) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(departmentCode.trim()));
            String response = genericService.callPylonAPI(departmentUnitUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Department Unit Fetch");
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

    @Override
    public PylonResponsePayload processCreateDesignation(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Designation");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDesignationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Designation");
            } else {
                response = genericService.callPylonAPI(updateDesignationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Designation");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "designation", key = "#id")
    public PylonResponsePayload processDeleteDesignation(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteDesignationUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Designation Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "designation", key = "#id")
    public PylonResponsePayload processFetchDesignation(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDesignationUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Designation Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "designation")
    public DataListResponsePayload processFetchDesignationList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(designationListUrl, "GET", genericService.generatePylonAPIToken(), "Designation List");
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

    @Override
    public PylonResponsePayload processCreateBranch(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Branch");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createBranchUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Branch");
            } else {
                response = genericService.callPylonAPI(updateBranchUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Branch");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "branch", key = "#id")
    public PylonResponsePayload processDeleteBranch(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteBranchUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Branch Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "branch", key = "#id")
    public PylonResponsePayload processFetchBranch(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchBranchUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Branch Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
//    @Cacheable(value = "branch")
    public DataListResponsePayload processFetchBranchList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(branchListUrl, "GET", genericService.generatePylonAPIToken(), "Branch List");
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

    @Override
    public PylonResponsePayload processCreateGradeLevel(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("GradeLevel");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createGradeLevelUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Grade Level");
            } else {
                response = genericService.callPylonAPI(updateGradeLevelUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Grade Level");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "gradeLevel", key = "#id")
    public PylonResponsePayload processDeleteGradeLevel(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteGradeLevelUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Grade Level Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "gradeLevel", key = "#id")
    public PylonResponsePayload processFetchGradeLevel(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchGradeLevelUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Grade Level Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "gradeLevel")
    public DataListResponsePayload processFetchGradeLevelList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(gradeLevelListUrl, "GET", genericService.generatePylonAPIToken(), "Grade Level List");
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

    /**
     * ***************** Document Transactions *********************
     */
    @Override
    public PylonResponsePayload processDocument(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setUploadedFiles(null);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setStartDate(requestPayload.getStartDate() == null ? "" : requestPayload.getStartDate());
            pylonPayload.setEndDate(requestPayload.getEndDate() == null ? "" : requestPayload.getEndDate());
            pylonPayload.setAmount(requestPayload.getAmount() == null ? "" : requestPayload.getAmount());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentUpload");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(processDocumentUrl, gson.toJson(pylonPayload), requestPayload.getUploadedFiles(), genericService.generatePylonAPIToken(), "Document Process");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocument(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditorData());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setStartDate(requestPayload.getStartDate() == null ? "" : requestPayload.getStartDate());
            pylonPayload.setEndDate(requestPayload.getEndDate() == null ? "" : requestPayload.getEndDate());
            pylonPayload.setAmount(requestPayload.getAmount() == null ? "" : requestPayload.getAmount());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Document");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(createDocumentUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocumentGroup(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentGroup");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDocumentGroupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document Group");
            } else {
                response = genericService.callPylonAPI(updateDocumentGroupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "DOcument Group");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "documentGroup", key = "#id")
    public PylonResponsePayload processDeleteDocumentGroup(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteDocumentGroupUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Document Group Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentGroup", key = "#id")
    public PylonResponsePayload processFetchDocumentGroup(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDocumentGroupUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Document Group Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentGroup")
    public DataListResponsePayload processFetchDocumentGroupList() {
        try {
            String response = genericService.callPylonAPI(documentGroupListUrl, "GET", genericService.generatePylonAPIToken(), "Document Group List");
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

    @Override
    public PylonResponsePayload processCreateDocumentType(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentType");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDocumentTypeUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document Type");
            } else {
                response = genericService.callPylonAPI(updateDocumentTypeUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "DOcument Type");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "documentType", key = "#id")
    public PylonResponsePayload processDeleteDocumentType(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteDocumentTypeUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Document Type Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentType", key = "#id")
    public PylonResponsePayload processFetchDocumentType(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDocumentTypeUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Document Type Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentType")
    public DataListResponsePayload processFetchDocumentTypeList(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(documentTypeListUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Document Type List");
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

    /**
     * **************** Document
     *
     *******************
     * @param documentGroupCode
     * @return
     */
    @Override
    public String generateDocumentId(String documentGroupCode) {
        //Check if generic docuemtn prefix is set
        String documentId;
        if (useGenericDocumentPrefix) {
            documentId = genericDocumentPrefix + genericService.generateRequestId();
        } else {
            documentId = documentGroupCode + genericService.generateRequestId();
        }
        return documentId;
    }

    @Override
    @CachePut(value = "documentTemplate", key = "#requestPayload.id")
    public PylonResponsePayload processUpdateDocumentTemplate(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentTemplate");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateDocumentTemplateUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document Template");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "myDocument", key = "#principal")
    public DataListResponsePayload processFetchMyDocuments(String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchMyDocumentUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "My Documents Fetch");
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

    @Override
//    @Cacheable(value = "pendingDocument", key = "#principal")
    public DataListResponsePayload processFetchPendingDocuments(String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchPendingDocumentUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Pending Documents Fetch");
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

    @Override
    @Cacheable(value = "draftDocument", key = "#principal")
    public DataListResponsePayload processFetchDraftDocuments(String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchDraftDocumentUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Draft Documents Fetch");
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

    @Override
    @CachePut(value = "documentWorkflow", key = "#requestPayload.id")
    public PylonResponsePayload processUpdateDocumentWorkflow(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getDocumentWorkflowBody());
            pylonPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            pylonPayload.setDocumentType(requestPayload.getDocumentType());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentTemplate");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateDocumentWorkflowUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document Workflow");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentDetail", key = "#id")
    public DataListResponsePayload processFetchDocumentDetails(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchDocumentDetailsUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Documents Details");
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

    @Override
    public PylonResponsePayload processApproveDocument(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setUploadedFiles(null);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentApprove");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(processApproveDocumentUrl, gson.toJson(pylonPayload), requestPayload.getUploadedFiles(), genericService.generatePylonAPIToken(), "Document Approve");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "documentWorkflow", key = "#id")
    public DataListResponsePayload processFetchDocumentWorkflow(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDocumentWorkflowHistoryUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Documents Workflow");
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

    @Override
    @CacheEvict(value = "draftDocument", key = "#id")
    public PylonResponsePayload processDeleteDraftDocument(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(processDeleteDraftDocumentUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Delete Draft Documents");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "searchDocument", key = "#search")
    public DataListResponsePayload processSearchDocument(String search, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(search.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(processSearchDocumentUrl + "?search=" + encodedParam + "&principal=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Search Documents");
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

    @Override
    public PylonResponsePayload processDocumentSignature(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentSignature");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            List<MultipartFile> files = new ArrayList<>();
            files.add(requestPayload.getUploadedFile());
            String response = genericService.callPylonAPI(processDocumentSignatureUrl, gson.toJson(pylonPayload), files, genericService.generatePylonAPIToken(), "Document Signature");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDocumentArchiving(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("DocumentArchiving");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            List<MultipartFile> files = new ArrayList<>();
            files.add(requestPayload.getUploadedFile());
            String response = genericService.callPylonAPI(processDocumentArchivingUrl, gson.toJson(pylonPayload), files, genericService.generatePylonAPIToken(), "Document Archiving");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public PylonResponsePayload processFetchAppUser(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchAppUserUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "App User Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public PylonResponsePayload processDeleteAppUser(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteAppUserUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "App User Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "user")
    public DataListResponsePayload processFetchAppUserList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(appUserListUrl, "GET", genericService.generatePylonAPIToken(), "App User List");
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

    @Override
    public PylonResponsePayload processCreateAppUser(String principal, EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setHod(requestPayload.getHod() == null ? "False" : "True");
            pylonPayload.setTeamLead(requestPayload.getTeamLead() == null ? "False" : "True");
            pylonPayload.setBranchHead(requestPayload.getBranchHead() == null ? "False" : "True");
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("SignUp");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createAppUserUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "App User");
            } else {
                response = genericService.callPylonAPI(updateAppUserUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "App User");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CachePut(value = "userUpdate", key = "#a0.username")
    public PylonResponsePayload processUpdateUserGenericDetails(EuclasePayload requestPayload, String principal) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setAppType("Euclase");
            pylonPayload.setPrincipal(principal);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("GenericUpdate");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateAppUserGenericUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Document Workflow");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "role")
    public DataListResponsePayload processFetchRoleList() {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            //Connect to Pylon API
            String response = genericService.callPylonAPI(listRoleGroupUrl, "GET", genericService.generatePylonAPIToken(), "Role List");
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

    @Override
    @Cacheable(value = "role", key = "#id")
    public PylonResponsePayload processFetchRoleGroup(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchRoleGroupUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Role Group Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateRoleGroup(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("RoleGroup");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createRoleGroupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Role Group");
            } else {
                response = genericService.callPylonAPI(updateRoleGroupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Role Group");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "role", key = "#id")
    public PylonResponsePayload processDeleteRoleGroup(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteRoleGroupUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Role Group Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "group", key = "#groupName")
    public DataListResponsePayload processFetchGroupRoles(String groupName) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(groupName.trim()));
            String response = genericService.callPylonAPI(fetchGroupRolesUrl + "?groupName=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Group Roles Fetch");
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

    @Override
    @CachePut(value = "group", key = "#requestPayload.id")
    public PylonResponsePayload processUpdateGroupRoles(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("GroupRoles");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateGroupRolesUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Group Roles");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processChangeDefaultPassword(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setTransType("DefaultPassword");
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("ChangePassword");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changeDefaultPasswordUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Change Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreatePublicHoliday(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("PublicHoliday");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createPublicHolidayUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Public Holiday");
            } else {
                response = genericService.callPylonAPI(updatePublicHolidayUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Public Holiday");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "holiday", key = "#id")
    public PylonResponsePayload processDeletePublicHoliday(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deletePublicHolidayUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Public Holiday Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "holiday", key = "#id")
    public PylonResponsePayload processFetchPublicHoliday(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchPublicHolidayUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Public Holiday Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "holiday")
    public DataListResponsePayload processFetchPublicHolidayList() {
        try {
            String response = genericService.callPylonAPI(publicHolidayListUrl, "GET", genericService.generatePylonAPIToken(), "Public Holiday List");
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

    @Override
    public PylonResponsePayload processCreateSLA(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Sla");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createSlaUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "SLA");
            } else {
                response = genericService.callPylonAPI(updateSlaUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "SLA");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "sla", key = "#id")
    public PylonResponsePayload processDeleteSLA(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteSlaUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "SLA Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "sla", key = "#id")
    public PylonResponsePayload processFetchSLA(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchSlaUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "SLA Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "sla")
    public DataListResponsePayload processFetchSLAList() {
        try {
            String response = genericService.callPylonAPI(slaListUrl, "GET", genericService.generatePylonAPIToken(), "SLA List");
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

    @Override
    public PylonResponsePayload processCreateBackup(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Backup");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createBackupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Backup");
            } else {
                response = genericService.callPylonAPI(updateBackupUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Backup");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "backup", key = "#id")
    public PylonResponsePayload processFetchBackup(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchBackupUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Backup Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }

    }

    @Override
    @CacheEvict(value = "backup", key = "#id")
    public PylonResponsePayload processDeleteBackup(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteBackupUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Backup Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    //    @Cacheable(value = "backup")
    public DataListResponsePayload processFetchBackupList() {
        try {
            String response = genericService.callPylonAPI(backupListUrl, "GET", genericService.generatePylonAPIToken(), "Backup List");
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

    @Override
    public PylonResponsePayload processCreateRestore(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(createRestoreUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Restore");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "restore")
    public DataListResponsePayload processFetchRestoreList() {
        try {
            String response = genericService.callPylonAPI(restoreListUrl, "GET", genericService.generatePylonAPIToken(), "Restore List");
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

    @Override
    public DataListResponsePayload processReports(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Report");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(reportUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Reports");
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

    @Override
    public PylonResponsePayload processCreateNotification(EuclasePayload requestPayload) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("Notification");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createNotificationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Notification");
            } else {
                response = genericService.callPylonAPI(updateNotificationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Notification");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @CacheEvict(value = "notification", key = "#id")
    public PylonResponsePayload processDeleteNotification(String id, String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(deleteNotificationUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal, "GET", genericService.generatePylonAPIToken(), "Notification Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "notification", key = "#id")
    public PylonResponsePayload processFetchNotification(String id) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchNotificationUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Notification Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    @Cacheable(value = "notification", key = "#principal")
    public DataListResponsePayload processFetchNotificationList(String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(notificationListUrl + "?id=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Notification List");
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

    @Override
    public PylonResponsePayload processCreatePushNotification(EuclasePayload requestPayload, String principal) {
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(genericService.generatePylonAPIToken());
            pylonPayload.setRequestType("PushNotification");
            pylonPayload.setHash(genericService.generateRequestString(genericService.generatePylonAPIToken(), pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createPushNotificationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Push Notification");
            } else {
                response = genericService.callPylonAPI(updatePushNotificationUrl, gson.toJson(pylonPayload), genericService.generatePylonAPIToken(), "Push Notification");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeletePushNotification(String id, String principal, boolean batch) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String encodedBatch = genericService.urlEncodeString(genericService.encryptString(String.valueOf(batch)));
            String response = genericService.callPylonAPI(deletePushNotificationUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal + "&btch=" + encodedBatch, "GET", genericService.generatePylonAPIToken(), "Notification Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchPushNotification(String id, boolean batch) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedBatch = genericService.urlEncodeString(genericService.encryptString(String.valueOf(batch).trim()));
            String response = genericService.callPylonAPI(fetchPushNotificationUrl + "?id=" + encodedParam + "&btch=" + encodedBatch, "GET", genericService.generatePylonAPIToken(), "Notification Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            if (ex.getMessage().contains("Expected BEGIN_OBJECT but was STRING")) {
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            } else {
                responsePayload.setResponseMessage(ex.getMessage());
            }
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchPushNotificationList() {
        try {
            String response = genericService.callPylonAPI(pushNotificationListUrl, "GET", genericService.generatePylonAPIToken(), "Notification List");
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

    @Override
    public DataListResponsePayload processFetchUserPushNotification(String principal) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(selfPushNotificationListUrl + "?prcp=" + encodedParam, "GET", genericService.generatePylonAPIToken(), "Notification List");
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

    @Override
    public PylonResponsePayload processUpdateSelfPushNotification(String id, String principal, String readStatus) {
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedPrincipal = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String encodedReadStatus = genericService.urlEncodeString(genericService.encryptString(readStatus));
            String response = genericService.callPylonAPI(updateSelfPushNotificationUrl + "?id=" + encodedParam + "&prcp=" + encodedPrincipal + "&rstat=" + encodedReadStatus, "GET", genericService.generatePylonAPIToken(), "Notification Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
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
