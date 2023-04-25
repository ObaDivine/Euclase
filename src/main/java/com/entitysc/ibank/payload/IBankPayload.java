package com.entitysc.ibank.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author briano
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IBankPayload {

    private String lastName;
    private String firstName;
    private String username;
    private String customerName;
    private String password;
    private String confirmPassword;
    private String mobileNumber;
    private String email;
    private String pin;
    private String confirmPin;
    private String qrCodeImage;
    private String profileImage;
    private String totp;
    private String otp1;
    private String otp2;
    private String otp3;
    private String otp4;
    private String otp5;
    private String otp6;
    private String salutation;
    private String accountNumber;
    private String accountName;
    private String bankCode;
    private String mnemonic;
    private String amount;
    private String description;
    private String beneficiary;
    private String telco;
    private String cabletvprovider;
    private String smartcard;
    private String meternumber;
    private String discos;
    private String bouquet;
    private String months;
    private String subscriptionCode;
    private String airtimeorData;
}
