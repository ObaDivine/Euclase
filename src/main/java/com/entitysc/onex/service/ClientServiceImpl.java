package com.entitysc.onex.service;

import com.entitysc.onex.constant.ResponseCodes;
import com.entitysc.onex.payload.DataListResponsePayload;
import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.google.gson.Gson;
import java.io.File;
import java.time.LocalDate;
import java.util.Locale;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author briano
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    MessageSource messageSource;
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
    @Value("${pylon.api.forgotpassword}")
    private String forgotPasswordUrl;
    @Value("${pylon.api.changepin}")
    private String changePinUrl;
    @Value("${pylon.api.changesecurityquestion}")
    private String changeSecurityQuestionUrl;
    @Value("${pylon.api.activation}")
    private String activationUrl;
    @Value("${pylon.api.qrcode.generate}")
    private String generateQRCodeUrl;
    @Value("${pylon.api.totp}")
    private String totpUpUrl;
    @Value("${pylon.api.dashboard}")
    private String dashboardUrl;
    @Value("${pylon.api.account.statement}")
    private String transactionHistoryUrl;
    @Value("${onex.image.dir}")
    private String profileImageUrl;
    /*
        Airtime Transactions
     */
    @Value("${pylon.api.airtime.single}")
    private String airtimeSingleUrl;
    @Value("${pylon.api.airtime.bulk}")
    private String airtimeBulkUrl;
    @Value("${pylon.api.airtime.scheduled.create}")
    private String airtimeScheduledUrl;
    @Value("${pylon.api.airtime.scheduled.list}")
    private String airtimeScheduledListUrl;
    @Value("${pylon.api.airtime.scheduled.record}")
    private String airtimeScheduledRecordUrl;
    @Value("${pylon.api.airtime.history}")
    private String airtimeHistoryUrl;
    @Value("${pylon.api.airtime.scheduled.delete}")
    private String airtimeScheduledDeleteUrl;
    @Value("${pylon.api.airtime.scheduled.update}")
    private String airtimeScheduledUpdateUrl;
    @Value("${pylon.api.airtime.scheduled.status}")
    private String airtimeScheduledStatusUrl;
    /*
        Data Transactions
     */
    @Value("${pylon.api.data.single}")
    private String dataSingleUrl;
    @Value("${pylon.api.data.bulk}")
    private String dataBulkUrl;
    @Value("${pylon.api.data.scheduled.create}")
    private String dataScheduledUrl;
    @Value("${pylon.api.data.scheduled.list}")
    private String dataScheduledListUrl;
    @Value("${pylon.api.data.scheduled.record}")
    private String dataScheduledRecordUrl;
    @Value("${pylon.api.data.plan}")
    private String dataPlanUrl;
    @Value("${pylon.api.data.history}")
    private String dataHistoryUrl;
    @Value("${pylon.api.data.scheduled.delete}")
    private String dataScheduledDeleteUrl;
    @Value("${pylon.api.data.scheduled.update}")
    private String dataScheduledUpdateUrl;
    @Value("${pylon.api.data.scheduled.status}")
    private String dataScheduledStatusUrl;
    /*
        Cable TV Transactions
     */
    @Value("${pylon.api.cabletv.single}")
    private String cabletvSingleUrl;
    @Value("${pylon.api.cabletv.bulk}")
    private String cabletvBulkUrl;
    @Value("${pylon.api.cabletv.scheduled.create}")
    private String cabletvScheduledUrl;
    @Value("${pylon.api.cabletv.scheduled.list}")
    private String cabletvScheduledListUrl;
    @Value("${pylon.api.cabletv.scheduled.record}")
    private String cabletvScheduledRecordUrl;
    @Value("${pylon.api.cabletv.history}")
    private String cabletvHistoryUrl;
    @Value("${pylon.api.cabletv.lookup.smartcard}")
    private String smartcardLookupUrl;
    @Value("${pylon.api.cabletv.lookup.subscription}")
    private String cableTvSubscriptionUrl;
    @Value("${pylon.api.cabletv.scheduled.delete}")
    private String cabletvScheduledDeleteUrl;
    @Value("${pylon.api.cabletv.scheduled.update}")
    private String cabletvScheduledUpdateUrl;
    @Value("${pylon.api.cabletv.scheduled.status}")
    private String cabletvScheduledStatusUrl;
    /*
        Electricity Transactions
     */
    @Value("${pylon.api.electricity.single}")
    private String electricitySingleUrl;
    @Value("${pylon.api.electricity.bulk}")
    private String electricityBulkUrl;
    @Value("${pylon.api.electricity.scheduled.create}")
    private String electricityScheduledUrl;
    @Value("${pylon.api.electricity.scheduled.list}")
    private String electricityScheduledListUrl;
    @Value("${pylon.api.electricity.scheduled.record}")
    private String electricityScheduledRecordUrl;
    @Value("${pylon.api.electricity.history}")
    private String electricityHistoryUrl;
    @Value("${pylon.api.electricity.scheduled.delete}")
    private String electricityScheduledDeleteUrl;
    @Value("${pylon.api.electricity.lookup.meter}")
    private String meterLookupUrl;
    @Value("${pylon.api.electricity.scheduled.update}")
    private String electricityScheduledUpdateUrl;
    @Value("${pylon.api.electricity.scheduled.status}")
    private String electricityScheduledStatusUrl;
    /*
        Funds Transfer Transactions
     */

    @Value("${pylon.api.beneficiary.list}")
    private String beneficiaryListUrl;
    @Value("${pylon.api.beneficiary.add}")
    private String beneficiaryAddUrl;
    @Value("${pylon.api.beneficiary.delete}")
    private String beneficiaryDeleteUrl;
    @Value("${pylon.api.fundstransfer.single}")
    private String ftSingleUrl;
    @Value("${pylon.api.fundstransfer.bulk}")
    private String ftBulkUrl;
    @Value("${pylon.api.fundstransfer.scheduled.create}")
    private String ftScheduledUrl;
    @Value("${pylon.api.fundstransfer.scheduled.list}")
    private String ftScheduledListUrl;
    @Value("${pylon.api.fundstransfer.scheduled.record}")
    private String ftScheduledRecordUrl;
    @Value("${pylon.api.fundstransfer.history}")
    private String fundsTransferHistoryUrl;
    @Value("${pylon.api.fundstransfer.scheduled.delete}")
    private String ftScheduledDeleteUrl;
    @Value("${pylon.api.fundstransfer.scheduled.update}")
    private String ftScheduledUpdateUrl;
    @Value("${pylon.api.fundstransfer.scheduled.status}")
    private String ftScheduledStatusUrl;
    @Value("${pylon.api.banklist}")
    private String bankListUrl;

    @Value("${pylon.api.wallet.fund.card}")
    private String fundWalletFromCardUrl;
    @Value("${pylon.api.wallet.fund.callback}")
    private String fundWalletCallbackUrl;
    @Value("${pylon.api.wallet.fund.notify}")
    private String fundWalletNotifyUrl;
    @Value("${pylon.api.profile.details}")
    private String profileDetailsUrl;
    @Value("${pylon.api.validate.pin}")
    private String validatePinUrl;
    @Value("${pylon.api.otp}")
    private String otpUrl;
    @Value("${pylon.api.twofactor.enable}")
    private String enableTwoFactorUrl;
    @Value("${pylon.api.twofactor.validate}")
    private String validateTwoFactorUrl;
    @Value("${pylon.qrcode.issuer}")
    private String qrCodeIssuer;

    @Override
    public PylonResponsePayload processSignin(OneXPayload requestPayload) {
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
    public PylonResponsePayload changePassword(OneXPayload requestPayload) {
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
    public PylonResponsePayload forgotPassword(OneXPayload requestPayload) {
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
    public PylonResponsePayload changePin(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("ChangePin");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changePinUrl, gson.toJson(pylonPayload), token, "Change PIN");
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
    public PylonResponsePayload changeSecurityQuestion(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("ChangeSecurityQuestion");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to Pylon API
            String response = genericService.callPylonAPI(changeSecurityQuestionUrl, gson.toJson(pylonPayload), token, "Change Security Question");
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
    public PylonResponsePayload processSignUp(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Create the user first
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("SignUp");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String createUserResponse = genericService.callPylonAPI(signUpUrl, gson.toJson(pylonPayload), token, "Sign Up");
            PylonResponsePayload createUserResponsePayload = gson.fromJson(createUserResponse, PylonResponsePayload.class);

            if (createUserResponsePayload.getStatus() != null && createUserResponsePayload.getError() != null && createUserResponsePayload.getPath() != null) {
                createUserResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                createUserResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (createUserResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setToken(token);
                pylonPayload.setRequestType("ChangeSecurityQuestion");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to Pylon API
                String response = genericService.callPylonAPI(changeSecurityQuestionUrl, gson.toJson(pylonPayload), token, "Change Security Question");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);

                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return createUserResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processSignUpActivation(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setMobileNumber(requestPayload.getMobileNumber());
            pylonPayload.setOtp(requestPayload.getOtp());
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Activation");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(activationUrl, gson.toJson(pylonPayload), token, "Activation");
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
    public PylonResponsePayload processQRCodeGenerate(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setChannel("WEB");
            pylonPayload.setIssuer(qrCodeIssuer);
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("QR Code");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(generateQRCodeUrl, gson.toJson(pylonPayload), token, "QR Code");
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
    public PylonResponsePayload validateTwoFactorAuthentication(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Totp");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(totpUpUrl, gson.toJson(pylonPayload), token, "Sign Up");
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
    public PylonResponsePayload processDashboard(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encryptedString = genericService.encryptString(username);
            PylonPayload pylonPayload = new PylonPayload();
            String response = genericService.callPylonAPI(dashboardUrl + "?id=" + encryptedString, gson.toJson(pylonPayload), token, "Dashboard");
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

    /*
        Account 
     */
    @Override
    public DataListResponsePayload processTransactionHistory(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(transactionHistoryUrl, gson.toJson(pylonPayload), token, "Transaction History");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /*
        Airtime Related Transactions
     */
    @Override
    public PylonResponsePayload processSingleAirtime(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Airtime");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(airtimeSingleUrl, gson.toJson(pylonPayload), token, "Airtime");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processBulkAirtime(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Airtime Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(airtimeBulkUrl, gson.toJson(pylonPayload), token, "Airtime Bulk");
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
    public PylonResponsePayload processScheduledAirtime(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Airtime Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(airtimeScheduledUrl, gson.toJson(pylonPayload), token, "Airtime Scheduled");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteScheduledAirtime(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getDeleteId().trim()));
                String response = genericService.callPylonAPI(airtimeScheduledDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Schedule Airtime Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledAirtime(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Airtime Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(airtimeScheduledUpdateUrl, gson.toJson(pylonPayload), token, "Airtime Schedule Update");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledAirtimeStatus(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getUpdateId().trim()));
                String response = genericService.callPylonAPI(airtimeScheduledStatusUrl + "?id=" + encodedParam, "GET", token, "Schedule Airtime Status");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchScheduledAirtimeUsingId(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(airtimeScheduledRecordUrl + "?id=" + encodedParam, "GET", token, "Schedule Airtime Record");
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
    public DataListResponsePayload processFetchScheduledAirtime(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(airtimeScheduledListUrl + "?id=" + encodedParam, "GET", token, "Schedule Airtime");
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
    public DataListResponsePayload processAirtimeTransaction(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(airtimeHistoryUrl, gson.toJson(pylonPayload), token, "Airtime Transaction History");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /*
        Data Related Transactions
     */
    @Override
    public PylonResponsePayload processSingleData(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getSubscriptionCode().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Data");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(dataSingleUrl, gson.toJson(pylonPayload), token, "Data");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processBulkData(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Data Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(dataBulkUrl, gson.toJson(pylonPayload), token, "Data Bulk");
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
    public PylonResponsePayload processScheduledData(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getSubscriptionCode().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Data Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(dataScheduledUrl, gson.toJson(pylonPayload), token, "Data Scheduled");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchScheduledDataUsingId(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(dataScheduledRecordUrl + "?id=" + encodedParam, "GET", token, "Schedule Data Record");
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
    public PylonResponsePayload processDeleteScheduledData(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getDeleteId().trim()));
                String response = genericService.callPylonAPI(dataScheduledDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Schedule Data Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledData(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Data");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getSubscriptionCode().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Data Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(dataScheduledUpdateUrl, gson.toJson(pylonPayload), token, "Data Scheduled Update");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledDataStatus(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Data");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getUpdateId().trim()));
                String response = genericService.callPylonAPI(dataScheduledStatusUrl + "?id=" + encodedParam, "GET", token, "Schedule Data Status");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchScheduledData(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(dataScheduledListUrl + "?id=" + encodedParam, "GET", token, "Schedule Data");
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
    public DataListResponsePayload processFetchDataPlan(String telco) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(dataPlanUrl + "/" + telco, "GET", token, "Data Plan");
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
    public DataListResponsePayload processDataTransaction(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(dataHistoryUrl, gson.toJson(pylonPayload), token, "Data Transaction History");
            DataListResponsePayload responsePayload = gson.fromJson(response, DataListResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            DataListResponsePayload responsePayload = new DataListResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    /*
        Cable TV Related Transactions
     */
    @Override
    public PylonResponsePayload processSingleCableTv(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "CableTV");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getBouquet().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setBiller(requestPayload.getBiller());
                pylonPayload.setViewingMonths(requestPayload.getViewingMonths());
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("CableTv");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(cabletvSingleUrl, gson.toJson(pylonPayload), token, "CableTv");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processBulkCableTv(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("CableTv Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(cabletvBulkUrl, gson.toJson(pylonPayload), token, "CableTv Bulk");
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
    public PylonResponsePayload processScheduledCableTv(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getBouquet().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setBiller(requestPayload.getBiller());
                pylonPayload.setViewingMonths(requestPayload.getViewingMonths());
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("CableTv Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(cabletvScheduledUrl, gson.toJson(pylonPayload), token, "CableTV Scheduled");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchScheduledCableTvUsingId(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(cabletvScheduledRecordUrl + "?id=" + encodedParam, "GET", token, "Schedule CableTV Record");
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
    public PylonResponsePayload processDeleteScheduledCableTv(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "CableTV");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getDeleteId().trim()));
                String response = genericService.callPylonAPI(cabletvScheduledDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Schedule CableTV Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledCableTv(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "CableTV");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] subscriptionDetails = requestPayload.getBouquet().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setSubscriptionCode(subscriptionDetails[0]);
                pylonPayload.setSubscriptionName(subscriptionDetails[1]);
                pylonPayload.setSubscriptionAmount(subscriptionDetails[2]);
                pylonPayload.setBiller(requestPayload.getBiller());
                pylonPayload.setViewingMonths(requestPayload.getViewingMonths());
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("CableTv Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(cabletvScheduledUpdateUrl, gson.toJson(pylonPayload), token, "CableTV Scheduled Update");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledCableTvStatus(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "CableTV");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getUpdateId().trim()));
                String response = genericService.callPylonAPI(cabletvScheduledStatusUrl + "?id=" + encodedParam, "GET", token, "Schedule Cable TV Status");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchScheduledCableTv(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(cabletvScheduledListUrl + "?id=" + encodedParam, "GET", token, "Schedule CableTV");
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
    public DataListResponsePayload processFetchCableTVSubscription(String biller) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(cableTvSubscriptionUrl + "/" + biller, "GET", token, "Cable TV Subscription");
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
    public DataListResponsePayload processCableTvTransaction(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(cabletvHistoryUrl, gson.toJson(pylonPayload), token, "Cable TV Transaction History");
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
    public PylonResponsePayload processSmartcardLookup(String biller, String smartcard) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setBiller(biller);
            pylonPayload.setSmartcard(smartcard);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Smartcard Lookup");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(smartcardLookupUrl, gson.toJson(pylonPayload), token, "Smartcard Lookup");
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

    /*
        Electricity Related Transaction
     */
    @Override
    public PylonResponsePayload processSingleElectricity(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Electricity");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(electricitySingleUrl, gson.toJson(pylonPayload), token, "Electricity");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processBulkElectricity(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Electricity Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(electricityBulkUrl, gson.toJson(pylonPayload), token, "Electricity Bulk");
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
    public PylonResponsePayload processScheduledElectricity(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Electricity Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(electricityScheduledUrl, gson.toJson(pylonPayload), token, "Electricity Scheduled");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchScheduledElectricityUsingId(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(electricityScheduledRecordUrl + "?id=" + encodedParam, "GET", token, "Schedule Electricity Record");
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
    public PylonResponsePayload processDeleteScheduledElectricity(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Electricity");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getDeleteId().trim()));
                String response = genericService.callPylonAPI(electricityScheduledDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Schedule Electricity Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledElectricity(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Electricity Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(electricityScheduledUpdateUrl, gson.toJson(pylonPayload), token, "Electricity Scheduled Update");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledElectricityStatus(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getUpdateId().trim()));
                String response = genericService.callPylonAPI(electricityScheduledStatusUrl + "?id=" + encodedParam, "GET", token, "Schedule Electricity Status");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchScheduledElectricity(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(electricityScheduledListUrl + "?id=" + encodedParam, "GET", token, "Schedule Electricity");
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
    public DataListResponsePayload processElectricityTransaction(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(electricityHistoryUrl, gson.toJson(pylonPayload), token, "Electricity Transaction History");
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
    public PylonResponsePayload processMeterLookup(String disco, String billType, String meterNumber) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setDisco(disco);
            pylonPayload.setBillType(billType);
            pylonPayload.setMeterNumber(meterNumber);
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Meter Lookup");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(meterLookupUrl, gson.toJson(pylonPayload), token, "Meter Lookup");
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

    /*
        Funds Transfer Related Transaction
     */
    @Override
    public PylonResponsePayload processSingleFundsTransfer(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] transferDetails = requestPayload.getBeneficiary().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setUsername(requestPayload.getUsername());
                pylonPayload.setSourceAccount(requestPayload.getWalletId());
                pylonPayload.setRecipientCode(transferDetails[1]);
                pylonPayload.setRequestType("FundsTransfer");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(ftSingleUrl, gson.toJson(pylonPayload), token, "Funds Transfer");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }

        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processBulkFundsTransfer(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("FundsTransfer Bulk");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(ftBulkUrl, gson.toJson(pylonPayload), token, "Funds Transfer Bulk");
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
    public PylonResponsePayload processScheduledFundsTransfer(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String[] transferDetails = requestPayload.getBeneficiary().split("\\|");
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setUsername(requestPayload.getUsername());
                pylonPayload.setSourceAccount(requestPayload.getWalletId());
                pylonPayload.setRecipientCode(transferDetails[1]);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("FundsTransfer Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(ftScheduledUrl, gson.toJson(pylonPayload), token, "Funds Transfer Scheduled");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processFetchScheduledFundsTransferUsingId(String id) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(id.trim()));
            String response = genericService.callPylonAPI(ftScheduledRecordUrl + "?id=" + encodedParam, "GET", token, "Schedule Funds Transfer Record");
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
    public PylonResponsePayload processDeleteScheduledFundsTransfer(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Funds Transfer");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getDeleteId().trim()));
                String response = genericService.callPylonAPI(ftScheduledDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Schedule Funds Transfer Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledFundsTransfer(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Funds Transfer");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setChannel("WEB");
                pylonPayload.setToken(token);
                pylonPayload.setRequestBy(requestPayload.getUsername());
                pylonPayload.setRequestType("Funds Transfer Schedule");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(ftScheduledUpdateUrl, gson.toJson(pylonPayload), token, "Funds Transfer Scheduled Update");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processUpdateScheduledFundsTransferStatus(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Funds Transfer");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getUpdateId().trim()));
                String response = genericService.callPylonAPI(ftScheduledStatusUrl + "?id=" + encodedParam, "GET", token, "Schedule Funds Transfer Status");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchScheduledFundsTransfer(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(ftScheduledListUrl + "?id=" + encodedParam, "GET", token, "Schedule Funds Transfer");
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
    public DataListResponsePayload processFundsTransferTransaction(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            String startDate = "";
            String endDate = "";
            if (requestPayload.getDateRange() == null) {
                startDate = LocalDate.now().toString();
                endDate = LocalDate.now().toString();
            } else {
                startDate = genericService.formatDate(requestPayload.getDateRange().split("to")[0].trim(), "d/m/y");
                endDate = genericService.formatDate(requestPayload.getDateRange().split("to")[1].trim(), "d/m/y");
            }
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setStartDate(startDate);
            pylonPayload.setEndDate(endDate);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Date Range");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(fundsTransferHistoryUrl, gson.toJson(pylonPayload), token, "Funds Transfer Transaction History");
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
    public DataListResponsePayload processUserTransferBeneficiary(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setUsername(username);
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(beneficiaryListUrl + "?id=" + encodedParam, "GET", token, "Transfer Beneficiary List");
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
    public PylonResponsePayload processAddTransferBeneficiary(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                BeanUtils.copyProperties(requestPayload, pylonPayload);
                pylonPayload.setUsername(requestPayload.getUsername());
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setRequestType("Transfer Beneficiary");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(beneficiaryAddUrl, gson.toJson(pylonPayload), token, "Transfer Beneficiary Add");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);

                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processDeleteTransferBeneficiary(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                //Connect to onex API
                String encodedParam = genericService.urlEncodeString(genericService.encryptString(requestPayload.getId().trim()));
                String response = genericService.callPylonAPI(beneficiaryDeleteUrl + "?id=" + encodedParam, "DELETE", token, "Transfer Beneficiary Delete");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload processSendOTP(String recipient, String message) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRecipient(recipient);
            pylonPayload.setMessage(message);
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("OTP");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(otpUrl, gson.toJson(pylonPayload), token, "OTP");
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
    public PylonResponsePayload processFundWalletUsingCard(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            //Check the transaction PIN
            PylonPayload pylonPayload = new PylonPayload();
            BeanUtils.copyProperties(requestPayload, pylonPayload);
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getPin());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestBy(requestPayload.getUsername());
            pylonPayload.setRequestType("PIN");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String pinResponse = genericService.callPylonAPI(validatePinUrl, gson.toJson(pylonPayload), token, "Airtime");
            PylonResponsePayload pinResponsePayload = gson.fromJson(pinResponse, PylonResponsePayload.class);

            if (pinResponsePayload.getStatus() != null && pinResponsePayload.getError() != null && pinResponsePayload.getPath() != null) {
                pinResponsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                pinResponsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
            }

            //Check if the validation succeed
            if (pinResponsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                pylonPayload = new PylonPayload();
                pylonPayload.setChannel("WEB");
                pylonPayload.setWalletId(requestPayload.getWalletId());
                pylonPayload.setAmount(requestPayload.getAmount());
                pylonPayload.setNarration("Fund Wallet");
                pylonPayload.setCallbackUrl(fundWalletCallbackUrl);
                pylonPayload.setRequestId(genericService.generateRequestId());
                pylonPayload.setToken(token);
                pylonPayload.setRequestType("Fund Wallet");
                pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                //Connect to onex API
                String response = genericService.callPylonAPI(fundWalletFromCardUrl, gson.toJson(pylonPayload), token, "Fund Wallet");
                PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
                if (responsePayload.getStatus() != null && responsePayload.getError() != null && responsePayload.getPath() != null) {
                    responsePayload.setResponseCode(ResponseCodes.INTERNAL_SERVER_ERROR.getResponseCode());
                    responsePayload.setResponseMessage(messageSource.getMessage("appMessages.failed.connect.middleware", new Object[0], Locale.ENGLISH));
                }
                return responsePayload;
            } else {
                return pinResponsePayload;
            }
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public DataListResponsePayload processFetchBankList() {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setRequestId(genericService.generateRequestId());
            pylonPayload.setToken(token);
            //Connect to onex API
            String response = genericService.callPylonAPI(bankListUrl, "GET", token, "Bank List");
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
    public void processCallback(PylonPayload requestPayload) {
        String response = "";
        String kalis = "";

        String okon = "";
    }

    @Override
    public void processFundWalletPaymentConfirm(String transRef) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Fund Wallet");
            //Connect to onex API
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(transRef.trim()));
            String response = genericService.callPylonAPI(fundWalletNotifyUrl + "?id=" + encodedParam, "GET", token, "Fund Wallet");
            PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
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
    public PylonResponsePayload processValidateTwoFactor(OneXPayload requestPayload) {
        String token = genericService.generatePylonAPIToken();
        try {
            PylonPayload pylonPayload = new PylonPayload();
            pylonPayload.setUsername(requestPayload.getUsername());
            pylonPayload.setPin(requestPayload.getOtp());
            pylonPayload.setChannel("WEB");
            pylonPayload.setToken(token);
            pylonPayload.setRequestType("Two Factor");
            pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI(validateTwoFactorUrl, gson.toJson(pylonPayload), token, "Two Factor");
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
    public PylonResponsePayload processEnableTwoFactor(String username) {
        String token = genericService.generatePylonAPIToken();
        try {
            String encodedParam = genericService.urlEncodeString(genericService.encryptString(username.trim()));
            String response = genericService.callPylonAPI(enableTwoFactorUrl + "?id=" + encodedParam, "GET", token, "Two Factor");
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

    /*
        Capital Market Transactions
     */
    @Override
    public PylonResponsePayload processCapitalMarketAccount(OneXPayload requestPayload) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PylonResponsePayload processCapitalMarketCapitalization(OneXPayload requestPayload) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PylonResponsePayload processCapitalMarketGainerLoser(OneXPayload requestPayload) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PylonResponsePayload processCapitalMarketCompanies(OneXPayload requestPayload) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public PylonResponsePayload processImageUpload(OneXPayload requestPayload) {
        try {
            String path = profileImageUrl + "/" + requestPayload.getUsername() + ".png";
            File newFile = new File(path);
            FileCopyUtils.copy(requestPayload.getFileUpload().getBytes(), newFile);
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("00");
            responsePayload.setResponseMessage("Success");
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public String processContactUs(OneXPayload requestPayload) {
        return "Success";
    }

}
