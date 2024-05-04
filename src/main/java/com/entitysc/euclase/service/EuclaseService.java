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
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateDepartment(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDepartment(String id);

    PylonResponsePayload processDeleteDepartment(String id, String principal);

    DataListResponsePayload processFetchDepartmentList();

    /**
     * ******************Department Unit Transactions **************** @param
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateDepartmentUnit(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDepartmentUnit(String id);

    PylonResponsePayload processDeleteDepartmentUnit(String id, String principal);

    DataListResponsePayload processFetchDepartmentUnitList();

    DataListResponsePayload processFetchDepartmentUnitList(String departmentCode);

    /**
     * ***************** Designation Transactions ******************* @param
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateDesignation(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDesignation(String id);

    PylonResponsePayload processDeleteDesignation(String id, String principal);

    DataListResponsePayload processFetchDesignationList();

    /**
     * ***************** Branch Transactions *********************** @param
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateBranch(EuclasePayload requestPayload);

    PylonResponsePayload processFetchBranch(String id);

    PylonResponsePayload processDeleteBranch(String id, String principal);

    DataListResponsePayload processFetchBranchList();

    /**
     * **************** Grade Level Transactions ******************* @param
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateGradeLevel(EuclasePayload requestPayload);

    PylonResponsePayload processFetchGradeLevel(String id);

    PylonResponsePayload processDeleteGradeLevel(String id, String principal);

    DataListResponsePayload processFetchGradeLevelList();

    /**
     * Document Group
     * @param requestPayload
     * @return
     */

    PylonResponsePayload processCreateDocumentGroup(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDocumentGroup(String id);

    PylonResponsePayload processDeleteDocumentGroup(String id, String principal);

    DataListResponsePayload processFetchDocumentGroupList();

    /**
     * **************** Document Transactions ******************* @param
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processDocument(EuclasePayload requestPayload);

    PylonResponsePayload processCreateDocumentType(EuclasePayload requestPayload);

    PylonResponsePayload processCreateDocument(EuclasePayload requestPayload);

    PylonResponsePayload processFetchDocumentType(String id);

    PylonResponsePayload processDeleteDocumentType(String id, String principal);

    DataListResponsePayload processFetchDocumentTypeList(String id);

    /**
     * **************** Document Template
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processUpdateDocumentTemplate(EuclasePayload requestPayload);

    /**
     * **************** Document Workflow
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processUpdateDocumentWorkflow(EuclasePayload requestPayload);

    /**
     * *************** My Documents
     *
     ******************
     * @param principal
     * @return
     */
    DataListResponsePayload processFetchMyDocuments(String principal);

    DataListResponsePayload processFetchPendingDocuments(String principal);

    DataListResponsePayload processFetchDraftDocuments(String principal);

    DataListResponsePayload processFetchDocumentDetails(String documentType, String id);

    DataListResponsePayload processFetchDocumentWorkflow(String documentType, String id);

    PylonResponsePayload processApproveDocument(EuclasePayload requestPayload);

    PylonResponsePayload processDeleteDraftDocument(String documentType, String id);

    DataListResponsePayload processSearchDocument(String search);

    PylonResponsePayload processDocumentSignature(EuclasePayload requestPayload);

    /**
     * ********************Application Users ******************* @param
     *
     * @param principal
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateAppUser(String principal, EuclasePayload requestPayload);

    PylonResponsePayload processFetchAppUser(String id);

    PylonResponsePayload processDeleteAppUser(String id, String principal);

    DataListResponsePayload processFetchAppUserList();

    DataListResponsePayload processFetchRoleList();

    PylonResponsePayload processCreateRoleGroup(EuclasePayload requestPayload);

    PylonResponsePayload processFetchRoleGroup(String id);

    PylonResponsePayload processDeleteRoleGroup(String id, String principal);

    DataListResponsePayload processFetchGroupRoles(String groupName);

    PylonResponsePayload processUpdateGroupRoles(EuclasePayload requestPayload);

    PylonResponsePayload processChangeDefaultPassword(EuclasePayload requestPayload);
}
