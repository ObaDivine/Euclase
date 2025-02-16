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

    DataListResponsePayload processFetchProfileDetails(String username);

    String generateDocumentId(String documentType);

    PylonResponsePayload processUpdateUserGenericDetails(EuclasePayload requestPayload, String principal);

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
     *
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

    DataListResponsePayload processFetchDocumentDetails(String id, String principal);

    DataListResponsePayload processFetchDocumentWorkflow(String id);

    PylonResponsePayload processApproveDocument(EuclasePayload requestPayload);

    PylonResponsePayload processDeleteDraftDocument(String id, String principal);

    DataListResponsePayload processSearchDocument(String search, String principal);

    PylonResponsePayload processDocumentSignature(EuclasePayload requestPayload);

    PylonResponsePayload processDocumentArchiving(EuclasePayload requestPayload);

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

    /**
     * Public Holiday
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreatePublicHoliday(EuclasePayload requestPayload);

    PylonResponsePayload processFetchPublicHoliday(String id);

    PylonResponsePayload processDeletePublicHoliday(String id, String principal);

    DataListResponsePayload processFetchPublicHolidayList();

    /**
     * Service Level Agreement
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateSLA(EuclasePayload requestPayload);

    PylonResponsePayload processFetchSLA(String id);

    PylonResponsePayload processDeleteSLA(String id, String principal);

    DataListResponsePayload processFetchSLAList();

    /**
     * Backup and Restore
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateBackup(EuclasePayload requestPayload);

    PylonResponsePayload processFetchBackup(String id);

    PylonResponsePayload processDeleteBackup(String id, String principal);

    DataListResponsePayload processFetchBackupList();

    PylonResponsePayload processCreateRestore(String id, String principal);

    DataListResponsePayload processFetchRestoreList();

    /**
     * Reports
     *
     *
     * @param requestPayload
     * @return
     */
    DataListResponsePayload processReports(EuclasePayload requestPayload);

    /**
     * Document Notification
     *
     * @param requestPayload
     * @return
     */
    PylonResponsePayload processCreateNotification(EuclasePayload requestPayload);

    PylonResponsePayload processFetchNotification(String id);

    PylonResponsePayload processDeleteNotification(String id, String principal);

    DataListResponsePayload processFetchNotificationList(String principal);

    PylonResponsePayload processCreatePushNotification(EuclasePayload requestPayload, String principal);

    PylonResponsePayload processDeletePushNotification(String id, String principal, boolean batch);

    PylonResponsePayload processFetchPushNotification(String id, boolean batch)
            ;

    DataListResponsePayload processFetchPushNotificationList();

    DataListResponsePayload processFetchUserPushNotification(String principal);

    PylonResponsePayload processUpdateSelfPushNotification(String id, String principal, String readStatus);

}
