package com.entitysc.onex.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OneXPayload {

    private String id;
    private String title;
    private String lastName;
    private String firstName;
    private String username;
    private String customerName;
    private String password;
    private String confirmPassword;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String mobileNumber;
    private String mobileNumberVerified;
    private String email;
    private String gender;
    private String dob;
    private String pin;
    private String confirmPin;
    private String currentPin;
    private String newPin;
    private String confirmNewPin;
    private String qrCodeImage;
    private String profileImage;
    private String otp;
    private String totp;
    private String salutation;
    private String accountNumber;
    private String accountName;
    private String bankCode;
    private String mnemonic;
    private String amount;
    private String narration;
    private String beneficiary;
    private String telco;
    private String smartcard;
    private String meterNumber;
    private String disco;
    private String bouquet;
    private String months;
    private String subscriptionCode;
    private String subscriptionType;
    private String airtimeorData;
    private String dateRange;
    private String walletId;
    private String walletBalance;
    private String billType;
    private String enableTwoFactorAuth;
    private String frequency;
    private String executeTime;
    private String startDate;
    private String endDate;
    private String updateId;
    private String deleteId;
    private String viewingMonths;
    private String biller;
    private int[] itemCounts;
    private String securityQuestion;
    private String securityAnswer;
    private MultipartFile fileUpload;
}
