package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.RolesService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/user")
public class RolesController {

    @Autowired
    RolesService roleService;
    @Value("${euclase.client.name}")
    private String companyName;
    @Value("${euclase.client.url}")
    private String companyUrl;
    private String alertMessage = "";
    private String alertMessageType = "";

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("companyName", companyName);
        model.addAttribute("companyUrl", companyUrl);
    }

    @GetMapping("/role")
    @Secured("ROLE_MANAGE_ROLES")
    public String userRoles(Model model, Principal principal, HttpServletRequest httpRequest, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", roleService.fetchRoleList().getData());
        model.addAttribute("groupRolesPayload", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "roles";
    }

    @PostMapping("/role/group")
    public String createRoleGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = roleService.processCreateRoleGroup(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/role";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", roleService.fetchRoleList().getData());
        model.addAttribute("groupRolesPayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "role");
        return "roles";
    }

    @GetMapping("/role/edit")
    @Secured("ROLE_MANAGE_ROLES")
    public String editRole(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = roleService.fetchRoleGroup(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/role";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("dataList", roleService.fetchRoleList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "roles";
    }

    @GetMapping("/role/delete")
    @Secured("ROLE_MANAGE_ROLES")
    public String deleteRole(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = roleService.processDeleteRoleGroup(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/user/role";
    }

    @PostMapping("/group/roles/fetch")
    public String fetchGroupRoles(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        DataListResponsePayload response = roleService.fetchGroupRoles(requestPayload.getGroupName());
        requestPayload.setRoleName(requestPayload.getGroupName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", roleService.fetchRoleList().getData());
        model.addAttribute("groupRolesPayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "group");
        return "roles";
    }

    @PostMapping("/group/roles/update")
    public String updateGroupRoles(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = roleService.processUpdateGroupRoles(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", roleService.fetchRoleList().getData());
        model.addAttribute("groupRolesPayload", null);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "group");
        return "roles";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
