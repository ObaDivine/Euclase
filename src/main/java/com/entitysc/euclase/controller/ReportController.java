package com.entitysc.euclase.controller;

import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.service.EuclaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/audit-log")
    public String auditLog(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportauditlog";
    }

    @PostMapping("/audit-log/")
    public String auditLog(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportauditlog";
    }

    @GetMapping("/backup")
    public String backupAndRestore(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbackup";
    }

    @PostMapping("/backup/")
    public String backupAndRestore(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportbackup";
    }

    @GetMapping("/document")
    public String documentReports(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdocument";
    }

    @PostMapping("/document/")
    public String document(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportdocument";
    }

    @GetMapping("/document/pending")
    public String pendingDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        //Set the payload to have required values
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setStartDate("1900-01-01");
        requestPayload.setEndDate("1900-01-01");
        requestPayload.setTransType("PendingDocument");
        requestPayload.setNewValue("PendingDocument");
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportpendingdocument";
    }

    @GetMapping("/document/digitized")
    public String digitizedDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdigitizeddocument";
    }

    @PostMapping("/document/digitized/")
    public String digitizedDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportdigitizeddocument";
    }

    @GetMapping("/document/branch")
    public String documentByBranch(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbranchdocument";
    }

    @PostMapping("/document/branch/")
    public String documentByBranch(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportbranchdocument";
    }

    @GetMapping("/document/department")
    public String documentByDepartment(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdepartmentdocument";
    }

    @PostMapping("/document/department/")
    public String documentByDepartment(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportdepartmentdocument";
    }

    @GetMapping("/document/unit")
    public String documentByUnit(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("unitList", euclaseService.processFetchDepartmentUnitList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportunitdocument";
    }

    @PostMapping("/document/unit/")
    public String documentByUnit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("unitList", euclaseService.processFetchDepartmentUnitList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportunitdocument";
    }

    @GetMapping("/document/group")
    public String documentByGroup(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("groupList", euclaseService.processFetchDocumentGroupList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportgroupdocument";
    }

    @PostMapping("/document/group/")
    public String documentByGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("groupList", euclaseService.processFetchDocumentGroupList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportgroupdocument";
    }

    @GetMapping("/document/type")
    public String documentByType(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("typeList", euclaseService.processFetchDocumentTypeList("All").getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reporttypedocument";
    }

    @PostMapping("/document/type/")
    public String documentByType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("typeList", euclaseService.processFetchDocumentTypeList("All").getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reporttypedocument";
    }

    @GetMapping("/document/sla")
    public String documentBySla(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("slaList", euclaseService.processFetchSLAList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportsladocument";
    }

    @PostMapping("/document/sla/")
    public String documentBySla(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("slaList", euclaseService.processFetchSLAList().getData());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportsladocument";
    }

    @GetMapping("/document/access-level")
    public String documentByAccessLevel(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportaccessleveldocument";
    }

    @PostMapping("/document/access-level/")
    public String documentByAccessLevel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", euclaseService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = euclaseService.processFetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "reportaccessleveldocument";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
