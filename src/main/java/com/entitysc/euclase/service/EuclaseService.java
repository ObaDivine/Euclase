package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.EuclasePayload;
import com.google.gson.Gson;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
public abstract class EuclaseService {

    @Value("${euclase.encryption.key.web}")
    private String encryptionKey;
    @Value("${euclasews.api.key}")
    private String apiKey;
    @Value("${euclase.image.dir}")
    private String imageDirectory;
    @Value("${euclase.temp.dir}")
    private String tempFileDirectory;
    @Autowired
    Gson gson;
    Logger logger = LoggerFactory.getLogger(EuclaseService.class);

    public String generateEuclaseWSAPIToken() {
        return apiKey;
    }

    public String generateRequestString(String token, EuclasePayload requestPayload) {
        StringJoiner rawString = new StringJoiner(":");
        switch (requestPayload.getRequestType()) {
            case "SignIn" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPassword().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "SignUp" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getTitle().trim());
                rawString.add(requestPayload.getLastName().trim());
                rawString.add(requestPayload.getFirstName().trim());
                rawString.add(requestPayload.getMiddleName() == null ? "" : requestPayload.getMiddleName().trim());
                rawString.add(requestPayload.getGender().trim());
                rawString.add(requestPayload.getDob().trim());
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCompany().trim());
                rawString.add(requestPayload.getBranch().trim());
                rawString.add(requestPayload.getDepartment().trim());
                rawString.add(requestPayload.getDepartmentUnit().trim());
                rawString.add(requestPayload.getDesignation().trim());
                rawString.add(requestPayload.getGradeLevel().trim());
                rawString.add(requestPayload.getRole().trim());
                rawString.add(requestPayload.getLoginDays().trim());
                rawString.add(requestPayload.getStartTime().trim());
                rawString.add(requestPayload.getEndTime().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getAccessLevel().trim());
                rawString.add(requestPayload.getCompanyHead().trim());
                rawString.add(requestPayload.getBranchHead().trim());
                rawString.add(requestPayload.getDepartmentHead().trim());
                rawString.add(requestPayload.getTeamLead().trim());
                rawString.add(requestPayload.getPrincipal().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "ChangePassword" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCurrentPassword().trim());
                rawString.add(requestPayload.getNewPassword().trim());
                rawString.add(requestPayload.getConfirmNewPassword().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "ForgotPassword" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getNewPassword().trim());
                rawString.add(requestPayload.getConfirmNewPassword().trim());
                rawString.add(requestPayload.getSecurityQuestion().trim());
                rawString.add(requestPayload.getSecurityAnswer().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "ChangePin" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCurrentPin().trim());
                rawString.add(requestPayload.getNewPin().trim());
                rawString.add(requestPayload.getConfirmNewPin().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "SecurityQuestion" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getSecurityQuestion().trim());
                rawString.add(requestPayload.getSecurityAnswer().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Company" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getCompanyName().trim());
                rawString.add(requestPayload.getCompanyCode().trim());
                rawString.add(requestPayload.getContactAddress().trim());
                rawString.add(requestPayload.getContactEmail().trim());
                rawString.add(requestPayload.getContactNumber().trim());
                rawString.add(requestPayload.getRcNumber().trim());
                rawString.add(requestPayload.getDateOfIncorporation());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Branch" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getBranchCode().trim());
                rawString.add(requestPayload.getBranchName().trim());
                rawString.add(requestPayload.getLocation().trim());
                rawString.add(requestPayload.getCompany().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Department" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDepartmentCode().trim());
                rawString.add(requestPayload.getDepartmentName().trim());
                rawString.add(requestPayload.getBranch().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DepartmentUnit" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDepartmentUnitCode().trim());
                rawString.add(requestPayload.getDepartmentUnitName().trim());
                rawString.add(requestPayload.getDepartment().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Designation" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDesignationCode().trim());
                rawString.add(requestPayload.getDesignationName().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "GradeLevel" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getGradeLevelCode().trim());
                rawString.add(requestPayload.getGradeLevelName().trim());
                rawString.add(requestPayload.getOrdinalValue().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentGroup" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentGroupCode().trim());
                rawString.add(requestPayload.getDocumentGroupName().trim());
                rawString.add(requestPayload.getCompany().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentType" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentTypeCode().trim());
                rawString.add(requestPayload.getDocumentTypeName().trim());
                rawString.add(requestPayload.getDocumentGroup().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentTemplate" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(String.valueOf(requestPayload.getId()));
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Document" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getTag().trim());
                rawString.add(requestPayload.getDocumentType().trim());
                rawString.add(requestPayload.getPurpose().trim());
                rawString.add(requestPayload.getNarration().trim());
                rawString.add(requestPayload.getAccessLevel().trim());
                if (!requestPayload.getCarbonCopy().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getCarbonCopy().trim());
                }
                if (!requestPayload.getReferenceDocument().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getReferenceDocument().trim());
                }
                if (!requestPayload.getStartDate().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getStartDate().trim());
                }
                if (!requestPayload.getEndDate().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getEndDate().trim());
                }
                if (!requestPayload.getAmount().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getAmount().trim());
                }
                rawString.add(requestPayload.getChannel().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentUpload" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getComment().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentApprove" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getComment().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentArchiving" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getNarration().trim());
                rawString.add(requestPayload.getTag().trim());
                rawString.add(requestPayload.getAccessLevel().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DocumentSearch" -> {
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                if (!requestPayload.getUsername().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getUsername().trim());
                }
                if (!requestPayload.getDocumentId().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getDocumentId().trim());
                }
                if (!requestPayload.getDocumentIdOperator().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getDocumentIdOperator().trim());
                }
                if (!requestPayload.getTag().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getTag().trim());
                }
                if (!requestPayload.getCreatedBy().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getCreatedBy().trim());
                }
                if (!requestPayload.getApprovedBy().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getApprovedBy().trim());
                }
                if (!requestPayload.getNarration().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getNarration().trim());
                }
                if (!requestPayload.getNarrationOperator().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getNarrationOperator().trim());
                }
                if (!requestPayload.getAmount().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getAmount().trim());
                }
                if (!requestPayload.getAmountOperator().equalsIgnoreCase("")) {
                    rawString.add(requestPayload.getAmountOperator().trim());
                }
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "RoleGroup" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getRoleName().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "GroupRoles" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getRoleName().trim());
                rawString.add(requestPayload.getRole().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "EmailPassword" -> {
                rawString.add(requestPayload.getEmail().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Activation" -> {
                rawString.add(requestPayload.getMobileNumber().trim());
                rawString.add(requestPayload.getOtp().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "TOTP" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "DateRange" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "QRCode" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getIssuer().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "OTP" -> {
                rawString.add(requestPayload.getRecipient().trim());
                rawString.add(requestPayload.getMessage().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "PIN" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "TwoFactor" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPin().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Sla" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getSlaName().trim());
                rawString.add(requestPayload.getSlaPriority().trim());
                rawString.add(requestPayload.getSlaValue().trim());
                rawString.add(requestPayload.getSlaPeriod().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "PublicHoliday" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getPublicHolidayName().trim());
                rawString.add(requestPayload.getPublicHolidayDate().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Backup" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getBackupName().trim());
                rawString.add(requestPayload.getBackupType().trim());
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getFrequency().trim());
                rawString.add(requestPayload.getRuntime().trim());
                rawString.add(requestPayload.getRemoteHost().trim());
                rawString.add(requestPayload.getRemoteHostUsername().trim());
                rawString.add(requestPayload.getRemoteHostPassword().trim());
                rawString.add(requestPayload.getSource().trim());
                rawString.add(requestPayload.getDestination().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Report" -> {
                rawString.add(requestPayload.getStartDate().trim());
                rawString.add(requestPayload.getEndDate().trim());
                rawString.add(requestPayload.getTransType().trim());
                rawString.add(requestPayload.getNewValue().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "GenericUpdate" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getAppType().trim());
                rawString.add(requestPayload.getTransType().trim());
                rawString.add(requestPayload.getPrincipal().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Notification" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getNotificationName().trim());
                rawString.add(requestPayload.getNotificationType().trim());
                rawString.add(requestPayload.getNotificationTrigger().trim());
                rawString.add(requestPayload.getDocumentId().trim());
                rawString.add(requestPayload.getStatus().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "PushNotification" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(String.valueOf(requestPayload.getBatchId()));
                rawString.add(requestPayload.getSentTo().trim());
                rawString.add(requestPayload.getMessage().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "Channel" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getChannel().trim());
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(String.valueOf(requestPayload.getIpAddressId()));
                rawString.add(String.valueOf(requestPayload.getApiBandId().trim()));
                rawString.add(String.valueOf(requestPayload.getRoleId().trim()));
                rawString.add(requestPayload.getWebhookUrl().trim());
                rawString.add(requestPayload.getLoginDays().trim());
                rawString.add(requestPayload.getStartTime().trim());
                rawString.add(requestPayload.getEndTime().trim());
                rawString.add(requestPayload.getPrincipal().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "IP" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getIpAddress().trim());
                rawString.add(requestPayload.getOrganizationName().trim());
                rawString.add(requestPayload.getOrganizationAddress().trim());
                rawString.add(requestPayload.getOrganizationPhone().trim());
                rawString.add(requestPayload.getOrganizationEmail().trim());
                rawString.add(requestPayload.getOrganizationContactPerson().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "ApiBand" -> {
                if (requestPayload.getId() != 0) {
                    rawString.add(String.valueOf(requestPayload.getId()));
                }
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getBandName().trim());
                rawString.add(String.valueOf(requestPayload.getUnitCost()).replace(",", ""));
                rawString.add(String.valueOf(requestPayload.getDailyLimit()));
                rawString.add(String.valueOf(requestPayload.getWeeklyLimit()));
                rawString.add(String.valueOf(requestPayload.getMonthlyLimit()));
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "TopUp" -> {
                rawString.add(requestPayload.getUsername().trim());
                rawString.add(requestPayload.getChannel().trim());
                rawString.add(requestPayload.getAmount().replace(",", ""));
                rawString.add(requestPayload.getRequestId().trim());
            }
            case "RecordType" -> {
                rawString.add(requestPayload.getRecordType().trim());
                rawString.add(requestPayload.getRecordId().trim());
                rawString.add(requestPayload.getRequestId().trim());
            }
        }

        return encryptString(rawString.toString());
    }

    private static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

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

    public String decryptString(String textToDecrypt, String encryptionKey) {
        try {
            byte[] decode = Base64.getDecoder().decode(textToDecrypt.getBytes(StandardCharsets.UTF_8));
            ByteBuffer bb = ByteBuffer.wrap(decode);
            byte[] iv = new byte[12];
            bb.get(iv);
            byte[] salt = new byte[16];
            bb.get(salt);
            byte[] cipherText = new byte[bb.remaining()];
            bb.get(cipherText);
            SecretKey aesKeyFromPassword = getAESKeyFromPassword(encryptionKey.toCharArray(), salt);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(128, iv));
            byte[] plainText = cipher.doFinal(cipherText);
            String decryptedResponse = new String(plainText, StandardCharsets.UTF_8);
            String[] splitString = decryptedResponse.split(":");
            StringJoiner rawString = new StringJoiner(":");
            for (String str : splitString) {
                rawString.add(str.trim());
            }
            return rawString.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String urlEncodeString(String urlToEncrypt) {
        try {
            return URLEncoder.encode(urlToEncrypt, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

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

    public String callEuclaseWSAPI(String url, String requestBody, String token, String app) {
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
            //Remove the hash and token from the request payload in the logger
            EuclasePayload payload = null;
            if (!requestBody.equalsIgnoreCase("GET") && !requestBody.equalsIgnoreCase("DELETE")) {
                payload = gson.fromJson(requestBody, EuclasePayload.class);
                payload.setHash(null);
                payload.setToken(null);
            }

            logger.info("EuclaseWS Service - " + app + " Request " + (payload == null ? requestBody : gson.toJson(payload)));
            //Log the error
            logger.info("EuclaseWS Service - " + app + " Response " + httpResponse.getBody());
            return httpResponse.getBody();
        } catch (Exception ex) {
            logger.info("EuclaseWS Service Response " + ex.getMessage());
            return ex.getMessage();
        }
    }

    public String callEuclaseWSAPI(String url, String requestBody, List<MultipartFile> uploadedFiles, String token, String app) {
        try {
            //Generate a collection of files
            Collection<File> files = new ArrayList<>();
            if (!uploadedFiles.isEmpty()) {
                for (MultipartFile f : uploadedFiles) {
                    if (!f.getOriginalFilename().equalsIgnoreCase("")) {
                        File convFile = new File(tempFileDirectory + "/" + f.getOriginalFilename());
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
                    .field("requestPayload", requestBody)
                    .field("uploadedFiles", files)
                    .asString();
            //Remove the hash and token from the request payload in the logger
            EuclasePayload payload = null;
            if (!requestBody.equalsIgnoreCase("GET") && !requestBody.equalsIgnoreCase("DELETE")) {
                payload = gson.fromJson(requestBody, EuclasePayload.class);
                payload.setHash(null);
                payload.setToken(null);
                payload.setUploadedFiles(null);
            }
            logger.info("EuclaseWS Service - " + app + " Request " + (payload == null ? requestBody : gson.toJson(payload)));
            //Log the error
            logger.info("EuclaseWS Service - " + app + " Response " + httpResponse.getBody());
            return httpResponse.getBody();
        } catch (Exception ex) {
            logger.info("EuclaseWS Service Response " + ex.getMessage());
            return ex.getMessage();
        }
    }

}
