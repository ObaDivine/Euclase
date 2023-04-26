package com.entitysc.ibank.payload;

import java.util.List;
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
public class PylonPayload {

    private String username;
    private String password;
    private String confirmPassword;
    private String requestId;
    private String hash;
    private String token;
    private String requestType;
    private String input;
    private String userType;
    private String responseCode;
    private String responseMessage;
    private List<?> dataList;
    private String mobileNumber;
    private String email;
    private String lastName;
    private String firstName;
    private String pin;
    private String confirmPin;
    private String channel;
    private String telco;
    private String customerName;
    private String amount;
    private String subscriptionCode;
    private String subscriptionName;
    private String subscriptionType;
    private String biller;
    private String smartcard;
    private String meterNumber;
    private String viewingMonths;
    private String disco;
    private String billType;
    private String activationId;
    private String totp;
    private String otp1;
    private String otp2;
    private String otp3;
    private String otp4;
    private String otp5;
    private String otp6;
    private String salutation;
    private String requestBy;
    private String airtimeorData;
    private String startDate;
    private String endDate;
}
