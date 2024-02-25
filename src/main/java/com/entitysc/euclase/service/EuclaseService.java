package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;

/**
 *
 * @author briano
 */
public interface EuclaseService {

    PylonResponsePayload processSignin(EuclasePayload requestPayload);

    PylonResponsePayload changePassword(EuclasePayload requestPayload);

    PylonResponsePayload forgotPassword(EuclasePayload requestPayload);

    PylonResponsePayload processFetchProfileDetails(String username);

    String generateDocumentId(String documentType);

    /**
     * ********************Department Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateDepartment(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDepartment(String id);

    PylonResponsePayload processDeleteDepartment(String id, String principal);

    DataListResponsePayload processFetchDepartmentList();

    /**
     * ******************Department Unit Transactions **************** @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateDepartmentUnit(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDepartmentUnit(String id);

    PylonResponsePayload processDeleteDepartmentUnit(String id, String principal);

    DataListResponsePayload processFetchDepartmentUnitList();

    /**
     * ***************** Designation Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateDesignation(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDesignation(String id);

    PylonResponsePayload processDeleteDesignation(String id, String principal);

    DataListResponsePayload processFetchDesignationList();

    /**
     * ***************** Branch Transactions *********************** @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateBranch(EuclasePayload requestPayload);

    PylonResponsePayload processFetchBranch(String id);

    PylonResponsePayload processDeleteBranch(String id, String principal);

    DataListResponsePayload processFetchBranchList();

    /**
     * **************** Grade Level Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateGradeLevel(EuclasePayload requestPayload);

    PylonResponsePayload processFetchGradeLevel(String id);

    PylonResponsePayload processDeleteGradeLevel(String id, String principal);

    DataListResponsePayload processFetchGradeLevelList();

    /**
     * **************** Leave Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateLeaveType(EuclasePayload requestPayload);

    PylonResponsePayload processCreateLeaveDocument(EuclasePayload requestPayload);

    PylonResponsePayload processFetchLeaveType(String id);

    PylonResponsePayload processDeleteLeaveType(String id, String principal);

    DataListResponsePayload processFetchLeaveTypeList();

    /**
     * **************** Loan Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateLoanType(EuclasePayload requestPayload);

    PylonResponsePayload processCreateLoanDocument(EuclasePayload requestPayload);

    PylonResponsePayload processFetchLoanType(String id);

    PylonResponsePayload processDeleteLoanType(String id, String principal);

    DataListResponsePayload processFetchLoanTypeList();

    /**
     * **************** Expense Transactions ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateExpenseType(EuclasePayload requestPayload);

    PylonResponsePayload processCreateExpenseDocument(EuclasePayload requestPayload);

    PylonResponsePayload processFetchExpenseType(String id);

    PylonResponsePayload processDeleteExpenseType(String id, String principal);

    DataListResponsePayload processFetchExpenseTypeList();

    /**
     * **************** Service Request ******************* @param
     * requestPayload
     *
     * @return
     */
    PylonResponsePayload processCreateServiceRequest(EuclasePayload requestPayload);

    PylonResponsePayload processCreateServiceRequestDocument(EuclasePayload requestPayload);

    PylonResponsePayload processFetchServiceRequest(String id);

    PylonResponsePayload processDeleteServiceRequest(String id, String principal);

    DataListResponsePayload processFetchServiceRequestList();

    /**
     * **************** Document Template
     */
    PylonResponsePayload processFetchDocumentTemplate(String templateName);

    PylonResponsePayload processCreateDocumentTemplate(EuclasePayload requestPayload);

}
