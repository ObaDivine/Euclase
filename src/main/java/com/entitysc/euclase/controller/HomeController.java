package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonPayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.entitysc.euclase.service.EuclaseService;
import com.entitysc.euclase.service.GenericService;
import jakarta.servlet.RequestDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
public class HomeController implements ErrorController {

    @Autowired
    EuclaseService euclaseService;
    @Autowired
    GenericService genericService;
    private String alertMessage = "";
    private String alertMessageType = "";
    @Value("${euclase.encryption.key.web}")
    private String encryptionKey;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "signin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpSession httpSession, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonResponsePayload response = euclaseService.processSignin(requestPayload);
        switch (response.getResponseCode()) {
            case "00" -> {
                //Create authorities
                List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
                List<String> roles = Arrays.asList("AIRTIME,DATA,TRANSACTION_QUERY");
                for (String userRole : roles) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRole));
                }

                //Set the context authentication
                SecurityContext context = securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(requestPayload.getUsername(), requestPayload.getPassword(), newAuthorities));
                securityContextHolderStrategy.setContext(context);
                securityContextRepository.saveContext(context, httpRequest, httpResponse);

                //Check if two factor authentication is enabled
                if (response.getData().getEnableTwoFactorAuth().equalsIgnoreCase("true")) {
                    EuclasePayload euclasePayload = new EuclasePayload();
                    euclasePayload.setEnableTwoFactorAuth(response.getData().getEnableTwoFactorAuth());
                    euclasePayload.setUsername(response.getData().getUsername());
                    model.addAttribute("euclasePayload", euclasePayload);
                    model.addAttribute("alertMessage", alertMessage);
                    model.addAttribute("alertMessageType", alertMessageType);
                    resetAlertMessage();
                    return "signintwofa";
                }

                //Two factor authentication is not disabled
                return "redirect:/dashboard";
            }
            default -> {
                //Check if password has expired
                if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.ID_EXPIRED.getResponseCode())) {
                    model.addAttribute("euclasePayload", requestPayload);
                    model.addAttribute("alertMessage", response.getResponseMessage());
                    model.addAttribute("alertMessageType", "error");
                    return "changedefaultpassword";
                }
                //Remove the Google Authenticator Code
                requestPayload.setOtp("");
                model.addAttribute("euclasePayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "signin";
            }
        }
    }

    @PostMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        alertMessage = "Your session is terminated and you are logged out";
        alertMessageType = "success";
        return "redirect:/";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "about";
    }

    @GetMapping("/signup/activate")
    public String signupActivation(@RequestParam("seid") String seid, Model model, HttpServletRequest request, HttpSession httpSession) {
        String decryptedParam = genericService.decryptString(seid, encryptionKey);
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setUsername(decryptedParam);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "changedefaultpassword";
    }

    @PostMapping("/password/default")
    public String changeDefaultPassword(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processChangeDefaultPassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "changedefaultpassword";
    }

    @GetMapping("/change-password")
    public String passwordChange(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "changepassword";
    }

    @PostMapping("/change-password/")
    public String changePassword(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.changePassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/change-password";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "changepassword";
    }

    @GetMapping("/forgot-password")
    public String forgotChange(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "forgotpassword";
    }

    @PostMapping("/forgot-password/")
    public String forgotPassword(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.forgotPassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "forgotpassword";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal, Model model) {
        DataListResponsePayload response = euclaseService.processFetchProfileDetails(principal.getName());
        httpSession.setAttribute("profileImage", response.getPayload().getUsername() + ".png");
        httpSession.setAttribute("firstName", response.getPayload().getFirstName());
        httpSession.setAttribute("lastName", response.getPayload().getLastName());
        httpSession.setAttribute("title", response.getPayload().getSalutation());
        httpSession.setAttribute("mobileNumber", response.getPayload().getMobileNumber());
        httpSession.setAttribute("mobileNumberVerified", response.getPayload().getMobileNumberVerified());
        httpSession.setAttribute("walletId", response.getPayload().getWalletId());
        httpSession.setAttribute("walletBalance", response.getPayload().getWalletBalance());
        httpSession.setAttribute("username", response.getPayload().getUsername());
        httpSession.setAttribute("enableMFA", response.getPayload().getEnableTwoFactorAuth());
        httpSession.setAttribute("itemCounts", String.valueOf(response.getPayload().getItemCounts()));
        httpSession.setAttribute("signatureLink", response.getPayload().getSignatureLink());
        httpSession.setAttribute("documentCount", response.getPayload().getDocumentCount());
        httpSession.setAttribute("documentViolatedSLACount", response.getPayload().getDocumentViolatedSLACount());
        httpSession.setAttribute("daysToDate", response.getPayload().getDaysToDate());
        httpSession.setAttribute("dataList", response.getData());
        httpSession.setAttribute("documentArchiveGroupCode", response.getPayload().getDocumentGroupCode());

        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("notification", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "dashboard";
    }

    @GetMapping("/user")
    public String user(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("designationList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("gradeLevelList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("userCount", euclaseService.processFetchAppUserList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuser";
    }

    @PostMapping("/user/new")
    public String user(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setPrincipal(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateAppUser(principal.getName(), requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("designationList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("gradeLevelList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("userCount", euclaseService.processFetchAppUserList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("transType", "role");
        return "appuser";
    }

    @GetMapping(value = "/user/list")
    public String userList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuserlist";
    }

    @GetMapping("/user/edit")
    public String editUser(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchAppUser(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("designationList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("gradeLevelList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("userCount", euclaseService.processFetchAppUserList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuser";
    }

    @GetMapping("/user/details")
    public String userDetails(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchAppUser(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("designationList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("branchList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("gradeLevelList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "appuserdetails";
    }

    @PostMapping("/user/department-unit")
    @ResponseBody
    public List<PylonPayload> getDepartmentUnit(String department) {
        List<PylonPayload> units = euclaseService.processFetchDepartmentUnitList(department).getData();
        return units;
    }

    @GetMapping("/user/role")
    public String userRoles(Model model, Principal principal, HttpServletRequest httpRequest, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("groupRolesPayload", null);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "roles";
    }

    @PostMapping("/user/role/group")
    public String createRoleGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateRoleGroup(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/role";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("groupRolesPayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "role");
        return "roles";
    }

    @GetMapping("/user/role/edit")
    public String editRole(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchRoleGroup(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user/role";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "roles";
    }

    @GetMapping("/user/role/delete")
    public String deleteRole(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processDeleteRoleGroup(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/user/role";
    }

    @PostMapping("/user/group/roles/fetch")
    public String fetchGroupRoles(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        DataListResponsePayload response = euclaseService.processFetchGroupRoles(requestPayload.getGroupName());
        requestPayload.setRoleName(requestPayload.getGroupName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("groupRolesPayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "group");
        return "roles";
    }

    @PostMapping("/user/group/roles/update")
    public String updateGroupRoles(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processUpdateGroupRoles(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("groupRolesPayload", null);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        model.addAttribute("transType", "group");
        return "roles";
    }

    @GetMapping("/user/update")
    public String userUpdate(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("userCount", euclaseService.processFetchAppUserList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "appuserupdate";
    }

    @PostMapping("/user/update/")
    public String updateUser(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processUpdateUser(requestPayload, principal.getName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("roleList", euclaseService.processFetchRoleList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "appuserupdate";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403";
            }
        }
        return "error";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
