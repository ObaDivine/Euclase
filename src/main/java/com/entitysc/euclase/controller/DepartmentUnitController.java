package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.BranchService;
import com.entitysc.euclase.service.CompanyService;
import com.entitysc.euclase.service.DepartmentService;
import com.entitysc.euclase.service.DepartmentUnitService;
import com.entitysc.euclase.service.PushNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/setup/department")
public class DepartmentUnitController {

    @Autowired
    DepartmentUnitService unitService;
    @Autowired
    CompanyService companyService;
    @Autowired
    BranchService branchService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/unit")
    @Secured("ROLE_MANAGE_DEPARTMENT_UNIT")
    public String departmentUnit(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("unitCount", unitService.fetchDepartmentUnitList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "departmentunit";
    }

    @PostMapping("/unit/")
    public String departmentUnit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = unitService.processCreateDepartmentUnit(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/unit";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("unitCount", unitService.fetchDepartmentUnitList().getData().size());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "departmentunit";
    }

    @GetMapping(value = "/unit/list")
    @Secured("ROLE_MANAGE_DEPARTMENT_UNIT")
    public String departmentUnitList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", unitService.fetchDepartmentUnitList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "departmentunitlist";
    }

    @GetMapping("/unit/edit")
    @Secured("ROLE_MANAGE_DEPARTMENT_UNIT")
    public String editDepartmentUnit(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = unitService.fetchDepartmentUnit(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/unit/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("unitCount", unitService.fetchDepartmentUnitList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "departmentunit";
    }

    @GetMapping("/unit/delete")
    @Secured("ROLE_MANAGE_DEPARTMENT_UNIT")
    public String deleteDepartmentUnit(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = unitService.processDeleteDepartmentUnit(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/department/unit/list";
    }

    @PostMapping("/unit/branch")
    @ResponseBody
    public List<EuclasePayload> getCompanyBranches(String company) {
        return branchService.fetchCompanyBranchList(company).getData();
    }

    @PostMapping("/unit/department")
    @ResponseBody
    public List<EuclasePayload> getBranchDepartment(String branch) {
        return departmentService.fetchBranchDepartmentList(branch).getData();
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
