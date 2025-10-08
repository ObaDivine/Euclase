package com.entitysc.euclase.fe.controller;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.service.BranchService;
import com.entitysc.euclase.fe.service.CompanyService;
import com.entitysc.euclase.fe.service.DepartmentService;
import com.entitysc.euclase.fe.service.DepartmentUnitService;
import com.entitysc.euclase.fe.service.DesignationService;
import com.entitysc.euclase.fe.service.GradeLevelService;
import com.entitysc.euclase.fe.service.RolesService;
import com.entitysc.euclase.fe.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentUnitService departmentUnitService;
    @Autowired
    DesignationService designationService;
    @Autowired
    GradeLevelService gradeLevelService;
    @Autowired
    BranchService branchService;
    @Autowired
    RolesService rolesService;
    @Value("${euclase.client.name}")
    private String companyName;
    @Value("${euclase.client.url}")
    private String companyUrl;
    @Autowired
    Gson gson;
    private String alertMessage = "";
    private String alertMessageType = "";

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("companyName", companyName);
        model.addAttribute("companyUrl", companyUrl);
    }

    @GetMapping("/")
    @Secured("ROLE_CREATE_APP_USER")
    public String user(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("designationList", designationService.fetchDesignationList().getData());
        model.addAttribute("gradeLevelList", gradeLevelService.fetchGradeLevelList().getData());
        model.addAttribute("roleList", rolesService.fetchRoleList().getData());
        model.addAttribute("userCount", userService.fetchAppUserList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuser";
    }

    @PostMapping("/new")
    public String user(@ModelAttribute("euclasePayload") EuclasePayload serializedPayload, HttpSession httpSession, Principal principal, Model model) {
        EuclasePayload requestPayload = gson.fromJson(serializedPayload.getSerializedForm(), EuclasePayload.class);
        requestPayload.setPrincipal(principal.getName());
        EuclaseResponsePayload response = userService.processCreateAppUser(principal.getName(), requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("designationList", designationService.fetchDesignationList().getData());
        model.addAttribute("gradeLevelList", gradeLevelService.fetchGradeLevelList().getData());
        model.addAttribute("roleList", rolesService.fetchRoleList().getData());
        model.addAttribute("userCount", userService.fetchAppUserList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("transType", "role");
        return "appuser";
    }

    @GetMapping(value = "/list")
    @Secured("ROLE_CREATE_APP_USER")
    public String userList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", userService.fetchAppUserList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuserlist";
    }

    @GetMapping("/edit")
    @Secured("ROLE_CREATE_APP_USER")
    public String editUser(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = userService.fetchAppUser(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("designationList", designationService.fetchDesignationList().getData());
        model.addAttribute("gradeLevelList", gradeLevelService.fetchGradeLevelList().getData());
        model.addAttribute("roleList", rolesService.fetchRoleList().getData());
        model.addAttribute("userCount", userService.fetchAppUserList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuser";
    }

    @GetMapping("/details")
    @Secured("ROLE_CREATE_APP_USER")
    public String userDetails(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = userService.fetchAppUser(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("departmentList", departmentService.fetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", departmentUnitService.fetchDepartmentUnitList().getData());
        model.addAttribute("designationList", designationService.fetchDesignationList().getData());
        model.addAttribute("branchList", branchService.fetchBranchList().getData());
        model.addAttribute("gradeLevelList", gradeLevelService.fetchGradeLevelList().getData());
        model.addAttribute("roleList", rolesService.fetchRoleList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuserdetails";
    }

    @PostMapping("/department-unit")
    @ResponseBody
    public List<EuclasePayload> getDepartmentUnit(String department) {
        return departmentUnitService.fetchDepartmentUnitList(department).getData();
    }

    @GetMapping("/update")
    @Secured("ROLE_MANAGE_APP_USER")
    public String userUpdate(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("userCount", userService.fetchAppUserList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "appuserupdate";
    }

    @PostMapping("/update/")
    public String updateUser(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        EuclaseResponsePayload response = userService.processUpdateUserGenericDetails(requestPayload, principal.getName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "appuserupdate";
    }

    @GetMapping("/online")
    @Secured("ROLE_CREATE_APP_USER")
    public String userOnline(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, Model model, HttpSession httpSession) {
        model.addAttribute("dataList", userService.fetchAppUserOnline(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuseronline";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
