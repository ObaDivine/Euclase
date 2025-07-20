package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.BranchService;
import com.entitysc.euclase.service.ChannelService;
import com.entitysc.euclase.service.CompanyService;
import com.entitysc.euclase.service.DepartmentService;
import com.entitysc.euclase.service.DocumentGroupService;
import com.entitysc.euclase.service.DocumentService;
import com.entitysc.euclase.service.DocumentTypeService;
import com.entitysc.euclase.service.PushNotificationService;
import com.entitysc.euclase.service.SLAService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ChannelService channelService;
    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    DocumentGroupService documentGroupService;
    @Autowired
    SLAService slaService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    BranchService branchService;
    @Autowired
    CompanyService companyService;
    @Autowired
    PushNotificationService notificationService;
    @Value("${euclase.client.name}")
    private String companyName;
    @Value("${euclase.client.url}")
    private String companyUrl;
    private String alertMessage = "";
    private String alertMessageType = "";
    private String startDate = "";
    private String endDate = "";
    private String recordType = "";
    
        @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("companyName", companyName);
        model.addAttribute("companyUrl", companyUrl);
    }

    @GetMapping("/audit-log")
    @Secured("ROLE_AUDIT_LOG")
    public String auditLog(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportauditlog";
    }

    @PostMapping("/audit-log/")
    public String auditLog(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportauditlog";
    }

    @GetMapping("/backup")
    @Secured("ROLE_REPORTS")
    public String backupAndRestore(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbackup";
    }

    @PostMapping("/backup/")
    public String backupAndRestore(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbackup";
    }

    @GetMapping("/document")
    @Secured("ROLE_REPORTS")
    public String documentReports(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdocument";
    }

    @PostMapping("/document/")
    public String document(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdocument";
    }

    @GetMapping("/document/pending")
    @Secured("ROLE_REPORTS")
    public String pendingDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        //Set the payload to have required values
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setStartDate("1900-01-01");
        requestPayload.setEndDate("1900-01-01");
        requestPayload.setTransType("PendingDocument");
        requestPayload.setNewValue("PendingDocument");
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportpendingdocument";
    }

    @GetMapping("/document/digitized")
    @Secured("ROLE_REPORTS")
    public String digitizedDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdigitizeddocument";
    }

    @PostMapping("/document/digitized/")
    public String digitizedDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdigitizeddocument";
    }

    @GetMapping("/document/company")
    @Secured("ROLE_REPORTS")
    public String documentByCompany(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportcompanydocument";
    }

    @PostMapping("/document/company/")
    public String documentByCompany(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
          model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportcompanydocument";
    }

    @GetMapping("/document/branch")
    @Secured("ROLE_REPORTS")
    public String documentByBranch(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbranchdocument";
    }

    @PostMapping("/document/branch/")
    public String documentByBranch(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the branch value and set the new value for unison
        requestPayload.setNewValue(requestPayload.getBranch());
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportbranchdocument";
    }

    @GetMapping("/document/department")
    @Secured("ROLE_REPORTS")
    public String documentByDepartment(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("branchList", branchService.fetchBranchList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdepartmentdocument";
    }

    @PostMapping("/document/department/")
    public String documentByDepartment(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the department value and set the new value for unison
        requestPayload.setNewValue(requestPayload.getDepartment());
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("branchList", branchService.fetchBranchList().getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdepartmentdocument";
    }

    @GetMapping("/document/unit")
    @Secured("ROLE_REPORTS")
    public String documentByUnit(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("branchList", branchService.fetchBranchList().getData());
        model.addAttribute("departmentList", departmentService.fetchDepartmentList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportunitdocument";
    }

    @PostMapping("/document/unit/")
    public String documentByUnit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the unit value and set the new value for unison
        requestPayload.setNewValue(requestPayload.getDepartmentUnit());
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("branchList", branchService.fetchBranchList().getData());
        model.addAttribute("departmentList", departmentService.fetchDepartmentList().getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportunitdocument";
    }

    @GetMapping("/document/group")
    @Secured("ROLE_REPORTS")
    public String documentByGroup(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportgroupdocument";
    }

    @PostMapping("/document/group/")
    public String documentByGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the document group value and set the new value for unison
        requestPayload.setNewValue(requestPayload.getDocumentGroup());
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportgroupdocument";
    }

    @GetMapping("/document/type")
    @Secured("ROLE_REPORTS")
    public String documentByType(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("groupList", documentGroupService.fetchDocumentGroupList().getData());
        model.addAttribute("typeList", documentTypeService.fetchDocumentTypeList("Company", httpSession.getAttribute("companyId").toString()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reporttypedocument";
    }

    @PostMapping("/document/type/")
    public String documentByType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the documnent type value and set the new value for unison
        requestPayload.setNewValue(requestPayload.getDocumentType());
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("groupList", documentGroupService.fetchDocumentGroupList().getData());
        model.addAttribute("typeList", documentTypeService.fetchDocumentTypeList("Company", httpSession.getAttribute("companyId").toString()).getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reporttypedocument";
    }

    @GetMapping("/document/sla")
    @Secured("ROLE_REPORTS")
    public String documentBySla(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("slaList", slaService.fetchSLAList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportsladocument";
    }

    @PostMapping("/document/sla/")
    public String documentBySla(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("slaList", slaService.fetchSLAList().getData());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportsladocument";
    }

    @GetMapping("/document/access-level")
    @Secured("ROLE_REPORTS")
    public String documentByAccessLevel(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportaccessleveldocument";
    }

    @PostMapping("/document/access-level/")
    public String documentByAccessLevel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportaccessleveldocument";
    }

    @GetMapping("/document/access")
    @Secured("ROLE_REPORTS")
    public String documentByAccess(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdocumentaccess";
    }

    @PostMapping("/document/access/")
    public String documentByAccess(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportdocumentaccess";
    }

    @GetMapping("/api/billing")
    @Secured("ROLE_REPORTS")
    public String apiBilling(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("channelList", channelService.fetchChannelList().getData());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportapibilling";
    }

    @PostMapping("/api/billing/")
    public String apiBilling(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        model.addAttribute("dataList", documentService.processReports(requestPayload).getData());
        model.addAttribute("channelList", channelService.fetchChannelList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportapibilling";
    }

    @GetMapping("/email")
    @Secured("ROLE_REPORTS")
    public String emailReport(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportemail";
    }

    @PostMapping("/email/")
    public String emailReport(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        //Retrieve the report filter for use with the notification
        startDate = requestPayload.getStartDate();
        endDate = requestPayload.getEndDate();
        recordType = requestPayload.getRecordType();
        model.addAttribute("dataList", documentService.processNotificationReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", "Report Processing complete");
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "reportemail";
    }

    @GetMapping("/email/retry")
    @Secured("ROLE_REPORTS")
    public String retryEmailNotification(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.retryEmailNotification(seid, principal.getName());
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setStartDate(startDate);
        requestPayload.setEndDate(endDate);
        requestPayload.setRecordType(recordType);
        requestPayload.setTransType("Email");
        requestPayload.setNewValue("Email");
        model.addAttribute("dataList", documentService.processNotificationReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        resetAlertMessage();
        return "reportemail";
    }

    @GetMapping("/email/delete")
    @Secured("ROLE_REPORTS")
    public String deleteEmailNotification(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.deleteEmailNotification(seid, principal.getName());
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setStartDate(startDate);
        requestPayload.setEndDate(endDate);
        requestPayload.setRecordType(recordType);
        requestPayload.setTransType("Email");
        requestPayload.setNewValue("Email");
        model.addAttribute("dataList", documentService.processNotificationReports(requestPayload).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        resetAlertMessage();
        return "reportemail";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
