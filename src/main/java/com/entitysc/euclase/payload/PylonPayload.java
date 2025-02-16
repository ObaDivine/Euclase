package com.entitysc.euclase.payload;

import com.google.gson.annotations.SerializedName;
import java.util.List;
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
public class PylonPayload {

    private int id = 0;
    private int batchId = 0;
    private String appType;
    private String title;
    private String gender;
    private String dob;
    private String username;
    private String password;
    private String confirmPassword;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String requestId;
    private String hash;
    private String token;
    private String requestType;
    private String input;
    private String userType;
    private String mobileNumber;
    private String mobileNumberVerified;
    private String email;
    private String lastName;
    private String firstName;
    private String middleName;
    private String pin;
    private String confirmPin;
    private String currentPin;
    private String newPin;
    private String confirmNewPin;
    private String channel;
    private String telco;
    private String amount;
    private String subscriptionCode;
    private String subscriptionName;
    private String subscriptionType;
    private String subscriptionAmount;
    private String biller;
    private String smartcard;
    private String meterNumber;
    private String viewingMonths;
    private String disco;
    private String billType;
    private String activationId;
    private String otp;
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
    private String issuer;
    private String qRCodeImageUrl;
    private String walletId;
    private String walletBalance;
    private String recipient;
    private String message;
    private boolean messageRead;
    private String authorizationUrl;
    private String narration;
    private String transType;
    private String transRef;
    private String status;
    private String responseCode;
    private String responseMessage;
    private String vat;
    private String feeAmount;
    private String callbackUrl;
    private String sourceAccount;
    private String sourceAccountName;
    private String sourceBank;
    private String destinationAccount;
    private String destinationAccountName;
    private String destinationBank;
    private String recipientCode;
    //Smartcard Details
    @SerializedName(value = "CustomerName", alternate = {"customerName"})
    private String customerName;
    private String customerNumber;
    private String customerType;
    private String bouquet;
    private String dueDate;
    private String businessUnit;
    private String customerArrears;
    private String currentAddress;
    private String units;
    private String mainTokenTax;
    private String mainsTokenAmount;
    private String bonusToken;
    private String bonusTokenDescription;
    private String bonusTokenUnits;
    private String path;
    private String error;
    private String mnemonic;
    private String accountNumber;
    private String accountName;
    private String bankCode;
    private String bankName;
    private String bankCategory;
    private String createdAt;
    private String createdBy;
    private String enableTwoFactorAuth;
    private String frequency;
    private String executeTime;
    private String nextExecutionDate;
    private String profileImage;
    private String updateId;
    private String deleteId;
    private String beneficiary;
    private int[] itemCounts;
    private int[] documentCount;
    private int[] documentViolatedSLACount;
    private int[] daysToDate;
    private String securityQuestion;
    private String securityAnswer;
    private String balanceBf;
    private String closingBalance;
    private String departmentCode;
    private String departmentName;
    private String departmentUnitCode;
    private String departmentUnitName;
    private String designationCode;
    private String designationName;
    private String branchCode;
    private String branchName;
    private String location;
    private String gradeLevelCode;
    private String gradeLevelName;
    private String hod;
    private String teamLead;
    private String branchHead;
    private String documentGroupCode;
    private String documentGroupName;
    private String documentTypeCode;
    private String documentTypeName;
    private String ordinalValue;
    private String priority;
    private String documentTemplateName;
    private String documentTemplateBody;
    private String documentWorkflowBody;
    private boolean documentTemplateFormatted;
    private boolean documentWorkflowFormatted;
    private String uniqueId;
    private String serviceType;
    private String documentId;
    private String documentType;
    private String documentLink;
    private String purpose;
    private String loanRequestBy;
    private String transactionDate;
    private String paymentMethod;
    private String comment;
    private String commentFrom;
    private String commentTo;
    private List<MultipartFile> uploadedFiles;
    private String carbonCopy;
    private String slaExpiry;
    private String slaViolated;
    private String slaViolatedBy;
    private String originalFileName;
    private String search;
    private String signatureLink;
    private String loginDays;
    private String startTime;
    private String endTime;
    private String groupName;
    private String role;
    private String roleName;
    private String roleExist;
    private String principal;
    private String passwordChangeDate;
    private String tag;
    private String newValue;
    private String action;
    private String sentBy;
    private String sentTo;
    private String referenceDocument;
    private String publicHolidayName;
    private String publicHolidayDate;
    private String slaName;
    private String slaPeriod;
    private String slaValue;
    private String slaPriority;
    private String accessLevel;
    private String approvalRoute;
    private int version;
    private String documentName;
    private String documentExt;
    private String host;
    private String backupName;
    private String folder;
    private String runtime;
    private String remoteDirectory;
    private String remoteHost;
    private String remoteHostUsername;
    private String remoteHostPassword;

    /*
        Audit Log
     */
    private String refNo;
    private String auditClass;
    private String auditCategory;
    private String auditAction;
    private String oldValue;

    /*
        Document and Push Notification
     */
    private String notificationName;
    private String notificationType;
    private String notificationTrigger;
}
