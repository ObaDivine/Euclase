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

    @Override
    public PylonResponsePayload processSignin(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("SignIn");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(signInUrl, gson.toJson(pylonPayload), token, "Login");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload changePassword(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("ChangePassword");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changePasswordUrl, gson.toJson(pylonPayload), token, "Change Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload forgotPassword(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("ForgotPassword");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(forgotPasswordUrl, gson.toJson(pylonPayload), token, "Forgot Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchProfileDetails(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Profile Details");
            //Connect to Pylon API
            String urlEncode = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(profileDetailsUrl + "?id=" + urlEncode, "GET", token, "Profile Details");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateUser(EuclasePayload requestPayload, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getDocumentWorkflowBody());
            pylonPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            pylonPayload.setDocumentType(requestPayload.getDocumentType());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Template");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateDocumentWorkflowUrl, gson.toJson(pylonPayload), token, "Document Workflow");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDepartmentList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(departmentListUrl, "GET", token, "Department List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDepartment(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Department");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDepartmentUrl, gson.toJson(pylonPayload), token, "Department");
            } else {
                response = genericService.callPylonAPI(updateDepartmentUrl, gson.toJson(pylonPayload), token, "Department");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDepartment(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteDepartmentUrl + "?id=" + encodedParam, "GET", token, "Department Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchDepartment(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDepartmentUrl + "?id=" + encodedParam, "GET", token, "Department Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDepartmentUnit(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Department Unit");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDepartmentUnitUrl, gson.toJson(pylonPayload), token, "Department Unit");
            } else {
                response = genericService.callPylonAPI(updateDepartmentUnitUrl, gson.toJson(pylonPayload), token, "Department Unit");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDepartmentUnit(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteDepartmentUnitUrl + "?id=" + encodedParam, "GET", token, "Department Unit Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchDepartmentUnit(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDepartmentUnitUrl + "?id=" + encodedParam, "GET", token, "Department Unit Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDepartmentUnitList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(departmentUnitListUrl, "GET", token, "Department Unit List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDepartmentUnitList(String departmentCode) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(departmentCode.trim()));
            String response = genericService.callPylonAPI(departmentUnitUrl + "?id=" + encodedParam, "GET", token, "Department Unit Fetch");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDesignation(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Designation");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDesignationUrl, gson.toJson(pylonPayload), token, "Designation");
            } else {
                response = genericService.callPylonAPI(updateDesignationUrl, gson.toJson(pylonPayload), token, "Designation");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDesignation(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteDesignationUrl + "?id=" + encodedParam, "GET", token, "Designation Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchDesignation(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDesignationUrl + "?id=" + encodedParam, "GET", token, "Designation Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDesignationList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(designationListUrl, "GET", token, "Designation List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateBranch(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Branch");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createBranchUrl, gson.toJson(pylonPayload), token, "Branch");
            } else {
                response = genericService.callPylonAPI(updateBranchUrl, gson.toJson(pylonPayload), token, "Branch");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteBranch(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteBranchUrl + "?id=" + encodedParam, "GET", token, "Branch Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchBranch(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchBranchUrl + "?id=" + encodedParam, "GET", token, "Branch Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchBranchList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(branchListUrl, "GET", token, "Branch List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateGradeLevel(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Grade Level");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createGradeLevelUrl, gson.toJson(pylonPayload), token, "Grade Level");
            } else {
                response = genericService.callPylonAPI(updateGradeLevelUrl, gson.toJson(pylonPayload), token, "Grade Level");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteGradeLevel(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteGradeLevelUrl + "?id=" + encodedParam, "GET", token, "Grade Level Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchGradeLevel(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchGradeLevelUrl + "?id=" + encodedParam, "GET", token, "Grade Level Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchGradeLevelList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(gradeLevelListUrl, "GET", token, "Grade Level List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /**
     * ***************** Document Transactions *********************
     */
    @Override
    public PylonResponsePayload processDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setUploadedFiles(null);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Upload");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(processDocumentUrl, gson.toJson(pylonPayload), requestPayload.getUploadedFiles(), token, "Document Process");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditorData());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(createDocumentUrl, gson.toJson(pylonPayload), token, "Document");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocumentGroup(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Group");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDocumentGroupUrl, gson.toJson(pylonPayload), token, "Document Group");
            } else {
                response = genericService.callPylonAPI(updateDocumentGroupUrl, gson.toJson(pylonPayload), token, "DOcument Group");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDocumentGroup(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteDocumentGroupUrl + "?id=" + encodedParam, "GET", token, "Document Group Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchDocumentGroup(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDocumentGroupUrl + "?id=" + encodedParam, "GET", token, "Document Group Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDocumentGroupList() {
        String token = genericService.generatePylonAPIToken();
        try {
            String response = genericService.callPylonAPI(documentGroupListUrl, "GET", token, "Document Group List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocumentType(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Type");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createDocumentTypeUrl, gson.toJson(pylonPayload), token, "Document Type");
            } else {
                response = genericService.callPylonAPI(updateDocumentTypeUrl, gson.toJson(pylonPayload), token, "DOcument Type");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDocumentType(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteDocumentTypeUrl + "?id=" + encodedParam, "GET", token, "Document Type Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchDocumentType(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchDocumentTypeUrl + "?id=" + encodedParam, "GET", token, "Document Type Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDocumentTypeList(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(documentTypeListUrl + "?id=" + encodedParam, "GET", token, "Document Type List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /**
     * **************** Document ******************* @param documentGroupCode
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
    public PylonResponsePayload processUpdateDocumentTemplate(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Template");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateDocumentTemplateUrl, gson.toJson(pylonPayload), token, "Document Template");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchMyDocuments(String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchMyDocumentUrl + "?id=" + encodedParam, "GET", token, "My Documents Fetch");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchPendingDocuments(String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchPendingDocumentUrl + "?id=" + encodedParam, "GET", token, "Pending Documents Fetch");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDraftDocuments(String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(principal.trim()));
            String response = genericService.callPylonAPI(fetchDraftDocumentUrl + "?id=" + encodedParam, "GET", token, "Draft Documents Fetch");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateDocumentWorkflow(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getDocumentWorkflowBody());
            pylonPayload.setDocumentTemplateName(requestPayload.getDocumentTemplateName());
            pylonPayload.setDocumentType(requestPayload.getDocumentType());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Template");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateDocumentWorkflowUrl, gson.toJson(pylonPayload), token, "Document Workflow");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDocumentDetails(String documentType, String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedDocumentType = genericService.urlEncodeString(genericService.encryptString(documentType.trim()));
            String response = genericService.callPylonAPI(fetchDocumentDetailsUrl + "?dt=" + encodedDocumentType + "&id=" + encodedParam, "GET", token, "Documents Details");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processApproveDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setDocumentTemplateBody(requestPayload.getEditor());
            pylonPayload.setUploadedFiles(requestPayload.getUploadedFiles());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Approve");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(processApproveDocumentUrl, gson.toJson(pylonPayload), requestPayload.getUploadedFiles(), token, "Document Approve");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchDocumentWorkflow(String documentType, String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedDocumentType = genericService.urlEncodeString(genericService.encryptString(documentType.trim()));
            String response = genericService.callPylonAPI(fetchDocumentWorkflowHistoryUrl + "?dt=" + encodedDocumentType + "&id=" + encodedParam, "GET", token, "Documents Workflow");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteDraftDocument(String documentType, String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String encodedDocumentType = genericService.urlEncodeString(genericService.encryptString(documentType.trim()));
            String response = genericService.callPylonAPI(processDeleteDraftDocumentUrl + "?dt=" + encodedDocumentType + "&id=" + encodedParam, "GET", token, "Delete Draft Documents");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processSearchDocument(String search) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(search.trim()));
            String response = genericService.callPylonAPI(processSearchDocumentUrl + "?search=" + encodedParam, "GET", token, "Search Documents");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDocumentSignature(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Document Signature");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            List<MultipartFile> files = new ArrayList<>();
            files.add(requestPayload.getUploadedFile());
            String response = genericService.callPylonAPI(processDocumentSignatureUrl, gson.toJson(pylonPayload), files, token, "Document Signature");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchAppUser(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchAppUserUrl + "?id=" + encodedParam, "GET", token, "App User Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteAppUser(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteAppUserUrl + "?id=" + encodedParam, "GET", token, "App User Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchAppUserList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(appUserListUrl, "GET", token, "App User List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateAppUser(String principal, EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setHod(requestPayload.getHod() == null ? "False" : "True");
            pylonPayload.setTeamLead(requestPayload.getTeamLead() == null ? "False" : "True");
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Sign Up");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createAppUserUrl, gson.toJson(pylonPayload), token, "App User");
            } else {
                response = genericService.callPylonAPI(updateAppUserUrl, gson.toJson(pylonPayload), token, "App User");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchRoleList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to Pylon API
            String response = genericService.callPylonAPI(listRoleGroupUrl, "GET", token, "Role List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchRoleGroup(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchRoleGroupUrl + "?id=" + encodedParam, "GET", token, "Role Group Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateRoleGroup(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Role Group");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response;
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createRoleGroupUrl, gson.toJson(pylonPayload), token, "Role Group");
            } else {
                response = genericService.callPylonAPI(updateRoleGroupUrl, gson.toJson(pylonPayload), token, "Role Group");
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
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteRoleGroup(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteRoleGroupUrl + "?id=" + encodedParam, "GET", token, "Role Group Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchGroupRoles(String groupName) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(groupName.trim()));
            String response = genericService.callPylonAPI(fetchGroupRolesUrl + "?groupName=" + encodedParam, "GET", token, "Group Roles Fetch");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateGroupRoles(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Group Roles");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(updateGroupRolesUrl, gson.toJson(pylonPayload), token, "Group Roles");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processChangeDefaultPassword(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setTransType("Default Password");
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Change Password");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changeDefaultPasswordUrl, gson.toJson(pylonPayload), token, "Change Password");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (JsonSyntaxException | BeansException | NoSuchMessageException ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

}
