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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/setup")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    CompanyService companyService;
    @Autowired
    BranchService branchService;
    @Autowired
    DepartmentUnitService departmentUnitService;
    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/department")
    @Secured("ROLE_MANAGE_DEPARTMENT")
    public String department(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("departmentCount", departmentService.fetchDepartmentList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "department";
    }

    @PostMapping("/department/")
    public String department(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = departmentService.processCreateDepartment(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("departmentCount", departmentService.fetchDepartmentList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "department";
    }

    @GetMapping(value = "/department/list")
    @Secured("ROLE_MANAGE_DEPARTMENT")
    public String departmentList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", departmentService.fetchDepartmentList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "departmentlist";
    }

    @GetMapping("/department/edit")
    @Secured("ROLE_MANAGE_DEPARTMENT")
    public String editDepartment(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = departmentService.fetchDepartment(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("departmentCount", departmentService.fetchDepartmentList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "department";
    }

    @GetMapping("/department/delete")
    @Secured("ROLE_MANAGE_DEPARTMENT")
    public String deleteDepartment(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = departmentService.processDeleteDepartment(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/department/list";
    }

    @GetMapping("/department/unit/{id}")
    @ResponseBody
    public List<EuclasePayload> getDepartmentUnit(@PathVariable("id") String id) {
        return departmentUnitService.fetchUnitsInDepartmentList(id).getData();
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
