package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.PylonPayload;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
@Service
public class GenericServiceImpl implements GenericService {

    @Value("${euclase.encryption.key.web}")
    private String encryptionKey;
    @Value("${euclase.image.dir}")
    private String imageDirectory;
    Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    @Override
    public String generatePylonAPIToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJXRUIiLCJhdWQiOiJbQ1JFRElUX0JVUkVBVSwgV0FMTEVULCBEQVRBLCBGVU5EU19UUkFOU0ZFUiwgQUlSVElNRSwgQ0FCTEVfVFYsIElOU1VSQU5DRSwgQlZOX05JTiwgRUxFQ1RSSUNJVFksIENBUkRfVE9LRU5JWkFUSU9OLCBUUkFOU0FDVElPTl9RVUVSWV0iLCJlbmNrZXkiOiJjbnpsVnovMzlnRXNXTXNleHgwc2Uydk00RTA5NXdTR0ZsaUZTNUxOYlpldVAzV3hURzI4Mlo2SEpJcWo3N1dJR3hlWWQ4dDdNV2VIaFJsVSIsImNoYW5uZWwiOiJXRUIiLCJpcCI6IjA6MDowOjA6MDowOjA6MSIsImFwaWtleSI6ImFRajlzNTFtOGV6VWFvMll2a3ZNS3NHcUl0RjFXZGZBK210eW5VV2lrWG4xc1pmY0FIT1pOVG9oaW0rZFRLaFdLQnliZ3plZGVzL1hiSHNRb2NvTDZGOTNUc0IrQ2dFRiIsImlzcyI6IkVudGl0eSBTb2Z0d2FyZSBDb21wYW55IiwiaWF0IjoxNjk4Njg5OTAwLCJleHAiOjYyNTE0Mjg0NDAwfQ.5gUND9p3SHIMa_MX02mCO_-U8O06p8ZuGUR50_h9P-4";
    }

    @Override
    public String generateRequestString(String token, PylonPayload requestPayload) {
        StringJoiner rawString = new StringJoiner(":");
        switch (requestPayload.getRequestType()) {
            case "SignIn": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                break;
            }
            case "SignUp": {
                rawString.add(requestPayload.getTitle().trim());
                rawString.add(requestPayload.getLastName().trim());
                rawString.add(requestPayload.getFirstName().trim());
                rawString.add(requestPayload.getMiddleName() == null ? "" : requestPayload.getMiddleName().trim());
                rawString.add(requestPayload.getGender().trim());
                rawString.add(requestPayload.getDob().trim());
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getConfirmPassword().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getConfirmPin().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "ChangePassword": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCurrentPassword().trim());
                rawString.add(requestPayload.getNewPassword().trim());
                rawString.add(requestPayload.getConfirmNewPassword().trim());
                break;
            }
            case "ForgotPassword": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getNewPassword().trim());
                rawString.add(requestPayload.getConfirmNewPassword().trim());
                rawString.add(requestPayload.getSecurityQuestion().trim());
                rawString.add(requestPayload.getSecurityAnswer().trim());
                break;
            }
            case "ChangePin": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCurrentPin().trim());
                rawString.add(requestPayload.getNewPin().trim());
                rawString.add(requestPayload.getConfirmNewPin().trim());
                break;
            }
            case "ChangeSecurityQuestion": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getSecurityQuestion().trim());
                rawString.add(requestPayload.getSecurityAnswer().trim());
                break;
            }
            case "Department": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getDepartmentCode().trim());
                rawString.add(requestPayload.getDepartmentName().trim());
                rawString.add(requestPayload.getHod().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Department Unit": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getDepartmentUnitCode().trim());
                rawString.add(requestPayload.getDepartmentUnitName().trim());
                rawString.add(requestPayload.getDepartmentCode().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Designation": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getDesignationCode().trim());
                rawString.add(requestPayload.getDesignationName().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Branch": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getBranchCode().trim());
                rawString.add(requestPayload.getBranchName().trim());
                rawString.add(requestPayload.getLocation().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Grade Level": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getGradeLevelCode().trim());
                rawString.add(requestPayload.getGradeLevelName().trim());
                rawString.add(requestPayload.getOrdinalValue().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Leave Type": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getLeaveTypeCode().trim());
                rawString.add(requestPayload.getLeaveTypeName().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Loan Type": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getLoanTypeCode().trim());
                rawString.add(requestPayload.getLoanTypeName().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Expense Type": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getExpenseTypeCode().trim());
                rawString.add(requestPayload.getExpenseTypeName().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Service Request": {
                rawString.add(requestPayload.getUsername().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getServiceRequestCode().trim());
                rawString.add(requestPayload.getServiceRequestName().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "Document Template": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentTemplateName().trim());
                break;
            }
            case "Document Workflow": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentTemplateName().trim());
                break;
            }
            case "Leave Document": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getUniqueId().trim());
                rawString.add(requestPayload.getLeaveType().trim());
                rawString.add(requestPayload.getLeaveReason().trim());
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                if (requestPayload.getCarbonCopy() != null) {
                    rawString.add(requestPayload.getCarbonCopy().trim());
                }
                break;
            }
            case "Loan Document": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getUniqueId().trim());
                rawString.add(requestPayload.getLoanType().trim());
                rawString.add(requestPayload.getLoanAmount().trim());
                rawString.add(requestPayload.getLoanPurpose().trim());
                rawString.add(requestPayload.getLoanRequestBy().trim());
                if (requestPayload.getCarbonCopy() != null) {
                    rawString.add(requestPayload.getCarbonCopy().trim());
                }
                break;
            }
            case "Expense Document": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getUniqueId().trim());
                rawString.add(requestPayload.getExpenseType().trim());
                rawString.add(requestPayload.getExpenseDate().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getPaymentMethod().trim());
                if (requestPayload.getCarbonCopy() != null) {
                    rawString.add(requestPayload.getCarbonCopy().trim());
                }
                break;
            }
            case "Service Document": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getUniqueId().trim());
                rawString.add(requestPayload.getServiceType().trim());
                if (requestPayload.getCarbonCopy() != null) {
                    rawString.add(requestPayload.getCarbonCopy().trim());
                }
                break;
            }
            case "Document Upload": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getComment().trim());
                break;
            }
            case "Document Approve": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getComment().trim());
                rawString.add(requestPayload.getStatus().trim());
                break;
            }
            case "FundsTransfer": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getSourceAccount().trim());
                rawString.add(requestPayload.getRecipientCode().trim());
                rawString.add(requestPayload.getNarration().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "FundsTransfer Bulk": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getUserType().trim());
                break;
            }
            case "FundsTransfer Schedule": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getSourceAccount().trim());
                rawString.add(requestPayload.getRecipientCode().trim());
                rawString.add(requestPayload.getNarration().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                rawString.add(requestPayload.getFrequency().trim());
                rawString.add(requestPayload.getExecuteTime().trim());
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                break;
            }
            case "Fund Wallet": {
                rawString.add(requestPayload.getWalletId().trim());
                rawString.add(requestPayload.getAmount().trim());
                rawString.add(requestPayload.getNarration().trim());
                rawString.add(requestPayload.getRequestId().trim());
                break;
            }
            case "EmailPassword": {
                rawString.add(requestPayload.getEmail().trim());
                break;
            }
            case "Activation": {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getOtp().trim());
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
            case "Date Range": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                break;
            }
            case "QR Code": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getIssuer().trim());
                break;
            }
            case "OTP": {
                rawString.add(requestPayload.getRecipient().trim());
                rawString.add(requestPayload.getMessage().trim());
                break;
            }
            case "PIN": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPin().trim());
                break;
            }
            case "Smartcard Lookup": {
                rawString.add(requestPayload.getBiller().trim());
                rawString.add(requestPayload.getSmartcard().trim());
                break;
            }
            case "Meter Lookup": {
                rawString.add(requestPayload.getDisco().trim());
                rawString.add(requestPayload.getMeterNumber().trim());
                rawString.add(requestPayload.getBillType().trim());
                break;
            }
            case "Transfer Beneficiary": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getAccountNumber().trim());
                rawString.add(requestPayload.getMnemonic().trim());
                rawString.add(requestPayload.getBankCode().trim());
                break;
            }
            case "Two Factor": {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPin().trim());
                break;
            }
        }

        return encryptString(rawString.toString());
    }

    private static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

    private static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    @Override
    public String encryptString(String textToEncrypt) {
        try {
            byte[] salt = getRandomNonce(16);
            SecretKey aesKeyFromPassword = getAESKeyFromPassword(encryptionKey.toCharArray(), salt);
            byte[] iv = getRandomNonce(12);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));
            byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes());
            byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                    .put(iv)
                    .put(salt)
                    .put(cipherText)
                    .array();
            return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @Override
    public String urlEncodeString(String urlToEncrypt) {
        try {
            return URLEncoder.encode(urlToEncrypt, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            return ex.getMessage();
        }
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
    public String formatDate(String dateToFormat, String currentFormat) {
        String year = "", month = "", day = "";
        if (currentFormat.contains("/")) {
            String[] dates = dateToFormat.split("/");
            if (dates.length != 3) {
                return "";
            } else {
                if (currentFormat.startsWith("y")) {
                    if (dates[0].length() == 2) {
                        year = "20" + dates[0];
                    } else {
                        year = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        month = "0" + dates[1];
                    } else {
                        month = dates[1];
                    }
                    if (dates[2].length() == 1) {
                        month = "0" + dates[2];
                    } else {
                        day = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                } else if (currentFormat.startsWith("d")) {
                    if (dates[0].length() == 1) {
                        day = "0" + dates[0];
                    } else {
                        day = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        month = "0" + dates[1];
                    } else {
                        month = dates[1];
                    }
                    if (dates[2].length() == 2) {
                        year = "20" + dates[2];
                    } else {
                        year = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                } else {
                    if (dates[0].length() == 1) {
                        month = "0" + dates[0];
                    } else {
                        month = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        day = "0" + dates[1];
                    } else {
                        day = dates[1];
                    }
                    if (dates[2].length() == 2) {
                        year = "20" + dates[2];
                    } else {
                        year = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                }
            }
        }

        if (currentFormat.contains("-")) {
            String[] dates = dateToFormat.split("-");
            if (dates.length != 3) {
                return "";
            } else {
                if (currentFormat.startsWith("y")) {
                    if (dates[0].length() == 2) {
                        year = "20" + dates[0];
                    } else {
                        year = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        month = "0" + dates[1];
                    } else {
                        month = dates[1];
                    }
                    if (dates[2].length() == 1) {
                        month = "0" + dates[2];
                    } else {
                        day = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                } else if (currentFormat.startsWith("d")) {
                    if (dates[0].length() == 1) {
                        day = "0" + dates[0];
                    } else {
                        day = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        month = "0" + dates[1];
                    } else {
                        month = dates[1];
                    }
                    if (dates[2].length() == 2) {
                        year = "20" + dates[2];
                    } else {
                        year = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                } else {
                    if (dates[0].length() == 1) {
                        month = "0" + dates[0];
                    } else {
                        month = dates[0];
                    }
                    if (dates[1].length() == 1) {
                        day = "0" + dates[1];
                    } else {
                        day = dates[1];
                    }
                    if (dates[2].length() == 2) {
                        year = "20" + dates[2];
                    } else {
                        year = dates[2];
                    }
                    return year + "-" + month + "-" + day;
                }
            }
        }
        return "";
    }

    @Override
    public String callPylonAPI(String url, String requestBody, String token, String app) {
        HttpResponse<String> httpResponse = null;
        try {
            Unirest.setTimeouts(0, 0);
            if (requestBody.equalsIgnoreCase("GET")) {
                httpResponse = Unirest.get(url)
                        .header("Authorization", "Bearer " + token)
                        .asString();
            } else if (requestBody.equalsIgnoreCase("DELETE")) {
                httpResponse = Unirest.delete(url)
                        .header("Authorization", "Bearer " + token)
                        .asString();
            } else {
                httpResponse = Unirest.post(url)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .body(requestBody)
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
    public String callPylonAPI(String url, String requestJson, List<MultipartFile> uploadedFiles, String token, String app) {
        try {
            //Generate a collection of files
            Collection<File> files = new ArrayList<>();
            if (!uploadedFiles.isEmpty()) {
                for (MultipartFile f : uploadedFiles) {
                    if (!f.getOriginalFilename().equalsIgnoreCase("")) {
                        File convFile = new File(f.getOriginalFilename());
                        convFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(convFile);
                        fos.write(f.getBytes());
                        fos.close();
                        files.add(convFile);
                    }
                }
            }

            //Check if the collection is empty. Add dummy file
            if (files.isEmpty()) {
                File file = new File(imageDirectory + "/logo_nobg.png");
                files.add(file);
            }

            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.post(url)
                    .header("Authorization", "Bearer " + token)
                    .field("requestPayload", requestJson)
                    .field("uploadedFiles", files)
                    .asString();
            logger.info("Pylon Service - " + app + " Request " + requestJson);
            //Log the error
            logger.info("Pylon Service - " + app + " Response " + httpResponse.getBody());
            return httpResponse.getBody();
        } catch (Exception ex) {
            logger.info("Pylon Service Response " + ex.getMessage());
            return ex.getMessage();
        }
    }
}
