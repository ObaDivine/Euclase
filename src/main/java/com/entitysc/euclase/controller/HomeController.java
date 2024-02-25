package com.entitysc.euclase.controller;

import com.entitysc.euclase.payload.EuclasePayload;
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

/**
 *
 * @author briano
 */
@Controller
public class HomeController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
        return "signin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpSession session, HttpServletRequest httpRequest, Model model) throws Exception {
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
        return "redirect:/login";
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
            return "redirect:/login";
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
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "dashboard";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
