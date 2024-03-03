package com.entitysc.euclase.service;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonPayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.google.gson.Gson;
import java.util.Locale;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
    @Value("${pylon.api.signup}")
    private String signUpUrl;
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
     * ******** Leave **************
     */
    @Value("${pylon.api.leave.type.list}")
    private String leaveTypeListUrl;
    @Value("${pylon.api.leave.type.create}")
    private String createLeaveTypeUrl;
    @Value("${pylon.api.leave.type.update}")
    private String updateLeaveTypeUrl;
    @Value("${pylon.api.leave.type.delete}")
    private String deleteLeaveTypeUrl;
    @Value("${pylon.api.leave.type.fetch}")
    private String fetchLeaveTypeUrl;
    @Value("${pylon.api.leave.doc.create}")
    private String createLeaveDocUrl;
    /**
     * *************** Loan ****************
     */
    @Value("${pylon.api.loan.type.list}")
    private String loanTypeListUrl;
    @Value("${pylon.api.loan.type.create}")
    private String createLoanTypeUrl;
    @Value("${pylon.api.loan.type.update}")
    private String updateLoanTypeUrl;
    @Value("${pylon.api.loan.type.delete}")
    private String deleteLoanTypeUrl;
    @Value("${pylon.api.loan.type.fetch}")
    private String fetchLoanTypeUrl;
    @Value("${pylon.api.loan.doc.create}")
    private String createLoanDocUrl;
    /**
     * ************ Expense ****************
     */
    @Value("${pylon.api.expense.type.list}")
    private String expenseTypeListUrl;
    @Value("${pylon.api.expense.type.create}")
    private String createExpenseTypeUrl;
    @Value("${pylon.api.expense.type.update}")
    private String updateExpenseTypeUrl;
    @Value("${pylon.api.expense.type.delete}")
    private String deleteExpenseTypeUrl;
    @Value("${pylon.api.expense.type.fetch}")
    private String fetchExpenseTypeUrl;
    @Value("${pylon.api.expense.doc.create}")
    private String createExpenseDocUrl;

    /**
     * ************ Service Request ****************
     */
    @Value("${pylon.api.service.request.list}")
    private String serviceRequestListUrl;
    @Value("${pylon.api.service.request.create}")
    private String createServiceRequestUrl;
    @Value("${pylon.api.service.request.update}")
    private String updateServiceRequestUrl;
    @Value("${pylon.api.service.request.delete}")
    private String deleteServiceRequestUrl;
    @Value("${pylon.api.service.request.fetch}")
    private String fetchServiceRequestUrl;
    @Value("${pylon.api.service.doc.create}")
    private String createServiceDocUrl;

    /**
     * ***************Document************************
     */
    @Value("${euclase.document.prefix.usegeneric}")
    private boolean useGenericDocumentPrefix;
    @Value("${euclase.document.prefix.expense}")
    private String expenseDocumentPrefix;
    @Value("${euclase.document.prefix.leave}")
    private String leaveDocumentPrefix;
    @Value("${euclase.document.prefix.loan}")
    private String loanDocumentPrefix;
    @Value("${euclase.document.prefix.service}")
    private String serviceDocumentPrefix;
    @Value("${euclase.document.prefix.generic}")
    private String genericDocumentPrefix;

    /**
     * ***************Document Template************************
     */
    @Value("${pylon.api.document.template.fetch}")
    private String fetchDocumentTemplateUrl;
    @Value("${pylon.api.document.template.create}")
    private String createDocumentTemplateUrl;

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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String urlEncode = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(profileDetailsUrl + "?id=" + urlEncode, "GET", token, "Profile Details");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(departmentListUrl, "GET", token, "Department List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = "";
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = "";
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(departmentUnitListUrl, "GET", token, "Department Unit List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = "";
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(designationListUrl, "GET", token, "Designation List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = "";
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(branchListUrl, "GET", token, "Branch List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = "";
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(gradeLevelListUrl, "GET", token, "Grade Level List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /**
     * ***************** Leave Transactions *********************
     */
    @Override
    public PylonResponsePayload processCreateLeaveDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Leave Document");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(createLeaveDocUrl, gson.toJson(pylonPayload), token, "Service Request");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateLeaveType(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Leave Type");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = "";
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createLeaveTypeUrl, gson.toJson(pylonPayload), token, "Leave Type");
            } else {
                response = genericService.callPylonAPI(updateLeaveTypeUrl, gson.toJson(pylonPayload), token, "Leave Type");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteLeaveType(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteLeaveTypeUrl + "?id=" + encodedParam, "GET", token, "Leave Type Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchLeaveType(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchLeaveTypeUrl + "?id=" + encodedParam, "GET", token, "Leave Type Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchLeaveTypeList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(leaveTypeListUrl, "GET", token, "Leave Type List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /**
     * ********************** Loan Transaction *********************
     */
    @Override
    public PylonResponsePayload processCreateLoanDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Loan Document");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(createLoanDocUrl, gson.toJson(pylonPayload), token, "Loan Document");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateLoanType(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Loan Type");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = "";
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createLoanTypeUrl, gson.toJson(pylonPayload), token, "Loan Type");
            } else {
                response = genericService.callPylonAPI(updateLoanTypeUrl, gson.toJson(pylonPayload), token, "Loan Type");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteLoanType(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteLoanTypeUrl + "?id=" + encodedParam, "GET", token, "Loan Type Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchLoanType(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchLoanTypeUrl + "?id=" + encodedParam, "GET", token, "Loan Type Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchLoanTypeList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(loanTypeListUrl, "GET", token, "Loan Type List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /**
     * ****************** Expense Transaction ******************
     */
    @Override
    public PylonResponsePayload processCreateExpenseDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Expense Document");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(createExpenseDocUrl, gson.toJson(pylonPayload), token, "Expense Document");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateExpenseType(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Expense Type");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = "";
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createExpenseTypeUrl, gson.toJson(pylonPayload), token, "Expense Type");
            } else {
                response = genericService.callPylonAPI(updateExpenseTypeUrl, gson.toJson(pylonPayload), token, "Expense Type");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteExpenseType(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteExpenseTypeUrl + "?id=" + encodedParam, "GET", token, "Expense Type Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchExpenseType(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchExpenseTypeUrl + "?id=" + encodedParam, "GET", token, "Expense Type Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchExpenseTypeList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(expenseTypeListUrl, "GET", token, "Expense Type List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

        @Override
    public PylonResponsePayload processCreateServiceRequestDocument(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Service Document");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(createServiceDocUrl, gson.toJson(pylonPayload), token, "Service Document");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }
    
    @Override
    public PylonResponsePayload processCreateServiceRequest(EuclasePayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Service Request");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = "";
            if (requestPayload.getId() == 0) {
                response = genericService.callPylonAPI(createServiceRequestUrl, gson.toJson(pylonPayload), token, "Service Request");
            } else {
                response = genericService.callPylonAPI(updateServiceRequestUrl, gson.toJson(pylonPayload), token, "Service Request");
            }
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteServiceRequest(String id, String principal) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(deleteServiceRequestUrl + "?id=" + encodedParam, "GET", token, "Service Request Delete");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchServiceRequest(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(fetchServiceRequestUrl + "?id=" + encodedParam, "GET", token, "Service Request Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchServiceRequestList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(serviceRequestListUrl, "GET", token, "Service Request List");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public String generateDocumentId(String documentType) {
        //Check if generic docuemtn prefix is set
        String documentId = "";
        if (useGenericDocumentPrefix) {
            documentId = genericDocumentPrefix + genericService.generateRequestId();
        } else {
            switch (documentType) {
                case "Expense":
                    documentId = expenseDocumentPrefix + genericService.generateRequestId();
                    break;
                case "Leave":
                    documentId = leaveDocumentPrefix + genericService.generateRequestId();
                    break;
                case "Loan":
                    documentId = loanDocumentPrefix + genericService.generateRequestId();
                    break;
                case "Service":
                    documentId = serviceDocumentPrefix + genericService.generateRequestId();
                    break;
                default:
                    documentId = genericDocumentPrefix + genericService.generateRequestId();
                    break;
            }
        }
        return documentId;
    }

    
    @Override
    public PylonResponsePayload processFetchDocumentTemplate(String templateName) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(templateName));
            String response = genericService.callPylonAPI(fetchDocumentTemplateUrl + "?id=" + encodedParam, "GET", token, "Document Template Fetch");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processCreateDocumentTemplate(EuclasePayload requestPayload) {
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
            //Connect to onex API
            String response = genericService.callPylonAPI(createDocumentTemplateUrl, gson.toJson(pylonPayload), token, "Document Template");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

}
