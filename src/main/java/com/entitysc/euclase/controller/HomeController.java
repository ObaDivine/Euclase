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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
public class HomeController {

    @Autowired
    EuclaseService euclaseService;
        @Autowired
    GenericService genericService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
        return "signin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpSession httpSession, HttpServletRequest httpRequest, Model model) throws Exception {
        PylonResponsePayload response = euclaseService.processSignin(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                //Create authorities
                List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
                List<String> roles = Arrays.asList("AIRTIME,DATA,CABLE_TV,FUNDS_TRANSFER,ELECTRICITY,BVN_NIN,CREDIT_BUREAU,CARD_TOKENIZATION,WALLET,TRANSACTION_QUERY");
                for (String userRole : roles) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRole));
                }
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestPayload.getUsername(), requestPayload.getPassword(), newAuthorities));
                //Set session variables
                List<String> userDetails = (List<String>) httpRequest.getSession().getAttribute("EUCLASE_SESSION_DETAILS");
                if (userDetails == null || userDetails.isEmpty()) {
                    userDetails = new ArrayList<>();
                    httpRequest.getSession().setAttribute("EUCLASE_SESSION_DETAILS", userDetails);
                }

                //Check if two factor authentication is enabled
                if (response.getData().getEnableTwoFactorAuth().equalsIgnoreCase("true")) {
                    EuclasePayload euclasePayload = new EuclasePayload();
                    euclasePayload.setEnableTwoFactorAuth(response.getData().getEnableTwoFactorAuth());
                    euclasePayload.setUsername(response.getData().getUsername());
                    model.addAttribute("euclasePayload", euclasePayload);
                    model.addAttribute("alertMessage", alertMessage);
                    model.addAttribute("alertMessageType", alertMessageType);
                    resetAlertMessage();
                    //Set session variables
                    model.addAttribute("sessionDetails", userDetails);
                    return "signintwofa";
                }

                //Two factor authentication is not disabled
                return "redirect:/dashboard";
            }
            default: {
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

    @GetMapping("/signup/activate")
    public String signupActivation(Model model, HttpServletRequest request, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
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
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.changePassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/change-password";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
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
        PylonResponsePayload response = euclaseService.processFetchProfileDetails(principal.getName());
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails.isEmpty()) {
            userDetails.add(response.getData().getUsername() + ".png");
            userDetails.add(response.getData().getFirstName());
            userDetails.add(response.getData().getLastName());
            userDetails.add(response.getData().getSalutation());
            userDetails.add(response.getData().getMobileNumber());
            userDetails.add(response.getData().getMobileNumberVerified());
            userDetails.add(response.getData().getWalletId());
            userDetails.add(response.getData().getWalletBalance());
            userDetails.add(response.getData().getUsername());
            userDetails.add(response.getData().getEnableTwoFactorAuth());
            userDetails.add(String.valueOf(response.getData().getItemCounts()));
            userDetails.add(response.getData().getSignatureLink());
            httpRequest.getSession().setAttribute("EUCLASE_SESSION_DETAILS", userDetails);
        } else {
            userDetails.set(0, response.getData().getUsername() + ".png");
            userDetails.set(1, response.getData().getFirstName());
            userDetails.set(2, response.getData().getLastName());
            userDetails.set(3, response.getData().getSalutation());
            userDetails.set(4, response.getData().getMobileNumber());
            userDetails.set(5, response.getData().getMobileNumberVerified());
            userDetails.set(6, response.getData().getWalletId());
            userDetails.set(7, response.getData().getWalletBalance());
            userDetails.set(8, response.getData().getUsername());
            userDetails.set(9, response.getData().getEnableTwoFactorAuth());
            userDetails.add(10, String.valueOf(response.getData().getItemCounts()));
            userDetails.add(11, response.getData().getSignatureLink());
            httpRequest.getSession().setAttribute("EUCLASE_SESSION_DETAILS", userDetails);
        }

        //Set the session variables
        EuclasePayload euclasePayload = new EuclasePayload();
        euclasePayload.setProfileImage(response.getData().getUsername() + ".png");
        euclasePayload.setFirstName(response.getData().getFirstName());
        euclasePayload.setLastName(response.getData().getLastName());
        euclasePayload.setSalutation(response.getData().getSalutation());
        euclasePayload.setMobileNumber(response.getData().getMobileNumber());
        euclasePayload.setMobileNumberVerified(response.getData().getMobileNumberVerified());
        euclasePayload.setUsername(response.getData().getUsername());
        euclasePayload.setEnableTwoFactorAuth(response.getData().getEnableTwoFactorAuth());
        euclasePayload.setItemCounts(response.getData().getItemCounts());
        euclasePayload.setSignatureLink(response.getData().getSignatureLink());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "dashboard";
    }

    @GetMapping("/user")
    public String user(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", null);
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
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setPrincipal(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateAppUser(principal.getName(), requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/user";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("departmentUnitList", null);
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
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("euclasePayload", requestPayload);
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
        model.addAttribute("departmentUnitList", null);
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
        model.addAttribute("departmentUnitList", null);
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
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("euclasePayload", requestPayload);
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
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
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
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
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
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processUpdateGroupRoles(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("dataList", euclaseService.processFetchRoleList().getData());
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
