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
    private String customerName;
    private String salutation;
    private String mobileNumber;
    private String mobileNumberVerified;
    private String otp;
    private String totp;
    private String enableTwoFactorAuth;
    private int[] itemCounts;
    private String profileImage;
    private String status;
    private String department;
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
    private String leaveTypeCode;
    private String leaveTypeName;
    private String leaveType;
    private String leaveDays;
    private String leaveReason;
    private String loanTypeCode;
    private String loanTypeName;
    private String loanType;
    private String loanPurpose;
    private String expenseDate;
    private String expenseTypeCode;
    private String expenseTypeName;
    private String expenseType;
    private String serviceRequestCode;
    private String serviceRequestName;
    private String serviceType;
    private String hod;
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
    private String comment;
    private String commentFrom;
    private String commentTo;
    private List<MultipartFile> uploadedFiles;
    private MultipartFile uploadedFile;
    private String carbonCopy;
    public String slaExpiry;
    private String originalFileName;
    private String createdAt;
    private String createdBy;
    private String search;
    private String signatureLink;
}
