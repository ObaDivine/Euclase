package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.PylonPayload;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.StringJoiner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 *
 * @author briano
 */
@Service
public class GenericServiceImpl implements GenericService {

    @Autowired
    Gson gson;
    @Value("${ibank.encryption.key.web}")
    private String encryptionKey;
    private static SecretKeySpec secretKey;
    private static byte[] key;
    Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    @Override
    public String callPylonAPI(String url, String requestBody, String token, String app) {
        HttpResponse<String> httpResponse = null;
        try {
            Unirest.setTimeouts(0, 0);
            if (requestBody != null) {
                httpResponse = Unirest.post(url)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .body(requestBody)
                        .asString();
            } else {
                httpResponse = Unirest.get(url)
                        .header("Authorization", "Bearer " + token)
                        .asString();
            }
            logger.info("Pylon Service - " + app + " Request " + requestBody);
            //Log the error
            logger.info("Pylon Service - " + app + " Response " + httpResponse.getBody());
            return httpResponse.getBody();
        } catch (Exception ex) {
            logger.info("Pylon Service Response " + ex.getMessage());
            return ex.getMessage();
        }
    }

    @Override
    public String generatePylonAPIToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJXRUIiLCJhdWQiOiJbQ1JFRElUX0JVUkVBVSwgV0FMTEVULCBEQVRBLCBGVU5EU19UUkFOU0ZFUiwgQUlSVElNRSwgQ0FCTEVfVFYsIEJWTl9OSU4sIEVMRUNUUklDSVRZLCBDQVJEX1RPS0VOSVpBVElPTiwgVFJBTlNBQ1RJT05fUVVFUlldIiwiZW5ja2V5IjoiM0hQMXpDUDcvem5icFBPWVdyd0F5QXBOMUlBdmphTGphdzl1TXh6LzdwND0iLCJjaGFubmVsIjoiV0VCIiwiaXAiOiIwOjA6MDowOjA6MDowOjEiLCJhcGlrZXkiOiJ6S0ZjRjFFTVpSUTkvcGM0MFNzMWR3ekxpU1ExN0tkbWhoTHNoaUtCYjRvPSIsImlzcyI6IkVudGl0eSBTb2Z0d2FyZSBDb21wYW55IiwiaWF0IjoxNjc5MzM4MTYwLCJleHAiOjYyNTE0Mjg0NDAwfQ.i28GlGM2lSErR5E5-2ZCdoxQ9UyxWheC2Fa5nECMEkY";
    }

    @Override
    public String generateRequestId() {
        SecureRandom secureRnd = new SecureRandom();
        int max = 12;
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder mnemonic = new StringBuilder(max);
        for (int i = 0; i < max; i++) {
            mnemonic.append(ALPHA_NUMERIC_STRING.charAt(secureRnd.nextInt(ALPHA_NUMERIC_STRING.length())));
        }
        return mnemonic.toString();
    }

    @Override
    public String generateRequestString(String token, PylonPayload requestPayload) {
        PylonPayload rePayload = new PylonPayload();
        StringJoiner rawString = new StringJoiner(":");
        switch (requestPayload.getRequestType()) {
            case "SignIn": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getTotp().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "SignUp": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getLastName().trim());
                rawString.add(requestPayload.getFirstName().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getConfirmPassword().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getConfirmPin().trim());
                rawString.add(requestPayload.getChannel().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "ChangePassword": {
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getConfirmPassword().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Airtime": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getCustomerName().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestBy().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Airtime Bulk": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getCustomerName().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Airtime Scheduled": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getCustomerName().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Data": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestBy().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Data Bulk": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Data Scheduled": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getTelco().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "CableTv": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getBiller().trim());
                rawString.add(requestPayload.getSmartcard().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getSubscriptionType().trim());
                rawString.add(requestPayload.getViewingMonths().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestBy().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "CableTv Bulk": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getBiller().trim());
                rawString.add(requestPayload.getSmartcard().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getSubscriptionType().trim());
                rawString.add(requestPayload.getViewingMonths().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "CableTv Scheduled": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getBiller().trim());
                rawString.add(requestPayload.getSmartcard().trim());
                rawString.add(requestPayload.getSubscriptionCode().trim());
                rawString.add(requestPayload.getSubscriptionName().trim());
                rawString.add(requestPayload.getSubscriptionType().trim());
                rawString.add(requestPayload.getViewingMonths().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Electricity": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getDisco().trim());
                rawString.add(requestPayload.getMeterNumber().trim());
                rawString.add(requestPayload.getBillType().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestBy().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Electricity Bulk": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getDisco().trim());
                rawString.add(requestPayload.getMeterNumber().trim());
                rawString.add(requestPayload.getBillType().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Electricity Scheduled": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getDisco().trim());
                rawString.add(requestPayload.getMeterNumber().trim());
                rawString.add(requestPayload.getBillType().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "FundsTransfer": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getUserType().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestBy().trim());
                break;
            }
            case "FundsTransfer Bulk": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getUserType().trim());
                break;
            }
            case "FundsTransfer Scheduled": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getUserType().trim());
                break;
            }
            case "EmailPassword": {
                rawString.add(requestPayload.getEmail().trim());
                break;
            }
            case "Activation": {
                rawString.add(requestPayload.getActivationId().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "Totp": {
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getOtp1().trim());
                rawString.add(requestPayload.getOtp2().trim());
                rawString.add(requestPayload.getOtp3().trim());
                rawString.add(requestPayload.getOtp4().trim());
                rawString.add(requestPayload.getOtp5().trim());
                rawString.add(requestPayload.getOtp6().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
        }

        return encryptString(rawString.toString(), token);
    }

    @Override
    public String encryptString(String textToEncrypt, String token) {
        try {
            String secret = encryptionKey;
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(textToEncrypt.trim().getBytes("UTF-8")));
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
