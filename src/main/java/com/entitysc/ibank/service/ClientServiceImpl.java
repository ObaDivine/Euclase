package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PylonPayload;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author briano
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    GenericService genericService;
    @Autowired
    Gson gson;
    @Value("${pylon.api.signin}")
    private String signInUrl;
    @Value("${pylon.api.signup}")
    private String signUpUrl;
    @Value("${pylon.api.changepassword}")
    private String changePasswordUrl;
    @Value("${pylon.api.activation}")
    private String activationUrl;
    @Value("${pylon.api.totp}")
    private String totpUpUrl;
    @Value("${pylon.api.dashboard}")
    private String dashboardUrl;
    @Value("${pylon.api.beneficiary.list}")
    private String beneficiaryListUrl;
    @Value("${pylon.api.airtime.single}")
    private String airtimeSingleUrl;
    @Value("${pylon.api.airtime.bulk}")
    private String airtimeBulkUrl;
    @Value("${pylon.api.airtime.scheduled}")
    private String airtimeScheduledUrl;
    @Value("${pylon.api.data.single}")
    private String dataSingleUrl;
    @Value("${pylon.api.data.bulk}")
    private String dataBulkUrl;
    @Value("${pylon.api.data.scheduled}")
    private String dataScheduledUrl;
    @Value("${pylon.api.cabletv.single}")
    private String cabletvSingleUrl;
    @Value("${pylon.api.cabletv.bulk}")
    private String cabletvBulkUrl;
    @Value("${pylon.api.cabletv.scheduled}")
    private String cabletvScheduledUrl;
    @Value("${pylon.api.electricity.single}")
    private String electricitySingleUrl;
    @Value("${pylon.api.electricity.bulk}")
    private String electricityBulkUrl;
    @Value("${pylon.api.electricity.scheduled}")
    private String electricityScheduledUrl;
    @Value("${pylon.api.fundstransfer.single}")
    private String ftSingleUrl;
    @Value("${pylon.api.fundstransfer.bulk}")
    private String ftBulkUrl;
    @Value("${pylon.api.fundstransfer.scheduled}")
    private String ftScheduledUrl;
    @Value("${pylon.api.data.plan}")
    private String dataPlanUrl;

    @Override
    public PylonPayload processSignin(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("SignIn");
            pylonPayload.setTotp(requestPayload.getOtp1() + requestPayload.getOtp2() + requestPayload.getOtp3()
                    + requestPayload.getOtp4() + requestPayload.getOtp5() + requestPayload.getOtp6());
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(signInUrl, gson.toJson(pylonPayload), token, "Login");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload changePassword(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("ChangePassword");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changePasswordUrl, gson.toJson(pylonPayload), token, "Change Password");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSignUp(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("SignUp");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(signUpUrl, gson.toJson(pylonPayload), token, "Sign Up");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSignUpActivation(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setActivationId(id);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Activation");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(activationUrl, gson.toJson(pylonPayload), token, "Sign Up");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload validateTwoFactorAuthentication(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Totp");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(totpUpUrl, gson.toJson(pylonPayload), token, "Sign Up");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processDashboard(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encryptedString = genericService.encryptString(username, token);
            PylonPayload pylonPayload = new PylonPayload();
            String response = genericService.callPylonAPI(dashboardUrl + "?id=" + encryptedString, gson.toJson(pylonPayload), token, "Dashboard");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processTransferBeneficiary(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to ibank API
            String encryptedUsername = genericService.encryptString(username, token);
            String response = genericService.callPylonAPI(beneficiaryListUrl + "?id=" + encryptedUsername, null, token, "Transfer Beneficiary List");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleAirtime(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setCustomerName(requestPayload.getFirstName() + " " + requestPayload.getLastName());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("Airtime");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(airtimeSingleUrl, gson.toJson(pylonPayload), token, "Airtime");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processBulkAirtime(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Airtime Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(airtimeBulkUrl, gson.toJson(pylonPayload), token, "Airtime Bulk");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processScheduledAirtime(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Airtime Scheduled");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(airtimeScheduledUrl, gson.toJson(pylonPayload), token, "Airtime Scheduled");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleData(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String[] subscriptionDetails = requestPayload.getSubscriptionCode().split("\\|");
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
            pylonPayload.setSubscriptionName(subscriptionDetails[1]);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("Data");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(dataSingleUrl, gson.toJson(pylonPayload), token, "Data");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processBulkData(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Data Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(dataBulkUrl, gson.toJson(pylonPayload), token, "Data Bulk");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processScheduledData(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Data Scheduled");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(dataScheduledUrl, gson.toJson(pylonPayload), token, "Data Scheduled");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleCableTv(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("CableTv");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(cabletvSingleUrl, gson.toJson(pylonPayload), token, "CableTv");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processBulkCableTv(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("CableTv Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(cabletvBulkUrl, gson.toJson(pylonPayload), token, "CableTv Bulk");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processScheduledCableTv(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("CableTv Scheduled");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(cabletvScheduledUrl, gson.toJson(pylonPayload), token, "CableTv Scheduled");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleElectricity(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("Electricity");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(electricitySingleUrl, gson.toJson(pylonPayload), token, "Electricity");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processBulkElectricity(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Electricity Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(electricityBulkUrl, gson.toJson(pylonPayload), token, "Electricity Bulk");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processScheduledElectricity(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Electricity Scheduled");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(electricityScheduledUrl, gson.toJson(pylonPayload), token, "Electricity Scheduled");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleFundsTransfer(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("FundsTransfer");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(ftSingleUrl, gson.toJson(pylonPayload), token, "Funds Transfer");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processSingleBulkTransfer(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("FundsTransfer Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(ftBulkUrl, gson.toJson(pylonPayload), token, "Funds Transfer Bulk");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processScheduledFundsTransfer(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("FundsTransfer Scheduled");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI(ftScheduledUrl, gson.toJson(pylonPayload), token, "Funds Transfer Scheduled");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processFetchDataPlan(String telco) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to ibank API
            String response = genericService.callPylonAPI(dataPlanUrl + "/" + telco, null, token, "Data Plan");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

    @Override
    public PylonPayload processAirtimeDetails(IBankPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to ibank API
            String response = genericService.callPylonAPI(dataPlanUrl + "/" + telco, null, token, "Data Plan");
            pylonPayload = new PylonPayload();
            pylonPayload = gson.fromJson(response, PylonPayload.class);
            return pylonPayload;
        } catch (Exception ex) {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setResponseCode("500");
            pylonPayload.setResponseMessage(ex.getMessage());
            return pylonPayload;
        }
    }

}
