package com.entitysc.euclase.payload;

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
public class EuclasePayload {

    private int id = 0;
    private String username;
    private String password;
    private String confirmPassword;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String firstName;
    private String lastName;
    private String middleName;
    private String dob;
    private String gender;
    private String customerName;
    private String salutation;
    private String mobileNumber;
    private String mobileNumberVerified;
    private String email;
    private String otp;
    private String totp;
    private String enableTwoFactorAuth;
    private int[] itemCounts;
    private int[] documentCount;
    private int[] documentViolatedSLACount;
    private int[] daysToDate;
    private String profileImage;
    private String status;
    private String departmentCode;
    private String departmentName;
    private String designationCode;
    private String designationName;
    private String departmentUnitCode;
    private String departmentUnitName;
    private String branchCode;
    private String branchName;
    private String location;
    private String gradeLevelCode;
    private String gradeLevelName;
    private String ordinalValue;
    private String documentGroupCode;
    private String documentGroupName;
    private String documentTypeCode;
    private String documentTypeName;
    private String leaveDays;
    private String purpose;
    private String transactionDate;
    private String hod;
    private String teamLead;
    private String branchHead;
    private String uniqueId;
    private String documentType;
    private String documentId;
    private String documentLink;
    private String paymentMethod;
    private String amount;
    private String narration;
    private String startDate;
    private String endDate;
    private String requestDate;
    private String allowUserDocumentId;
    private String priority;
    private String editor;
    private String editorData;
    private String documentTemplateName;
    private String documentTemplateBody;
    private String documentWorkflowBody;
    private String comment;
    private String commentFrom;
    private String commentTo;
    private List<MultipartFile> uploadedFiles;
    private MultipartFile uploadedFile;
    private String carbonCopy;
    public String slaExpiry;
    public String slaViolated;
    private String slaViolatedBy;
    private String originalFileName;
    private String createdAt;
    private String createdBy;
    private String search;
    private String signatureLink;
    private String title;
    private String loginDays;
    private String startTime;
    private String endTime;
    private String role;
    private String groupName;
    private String roleName;
    private String roleExist;
    private String principal;
    private String passwordChangeDate;
    private List<String> loginDay;
    private String transType;
    private String tag;
    private String newValue;
    private String action;
    private String securityQuestion;
    private String securityAnswer;
    private MultipartFile fileUpload;
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
}
