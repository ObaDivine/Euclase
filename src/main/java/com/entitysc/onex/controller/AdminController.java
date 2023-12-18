package com.entitysc.onex.controller;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.entitysc.onex.service.AdminService;
import com.entitysc.onex.service.GenericService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
@RequestMapping("/admin")
public class AdminController {

    private String alertMessage = "";
    @Autowired
    GenericService genericService;
    @Autowired
    AdminService adminService;
    @Autowired
    MessageSource messageSource;
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
    @Value("${onex.default.password}")
    private String defaultPassword;

    @GetMapping("/login")
    public String admin(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal) {
        model.addAttribute("loginPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "adminlogin";
    }

    @PostMapping("/auth")
    public String login(@ModelAttribute("loginPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model, Principal principal) throws Exception {
        PylonResponsePayload response = adminService.authenticateUser(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                //Create authorities
                List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
                for (String userRole : (List<String>) response.getData()) {
                    newAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userRole));
                }
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestPayload.getUsername(), requestPayload.getPassword(), newAuthorities));
                return "redirect:/admin/dashboard";
            }
            case "01": {
                alertMessage = response.getResponseMessage();
                return "redirect:/login";
            }
            default: {
                alertMessage = response.getResponseMessage();
                return "redirect:/login";
            }
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "admindashboard";
    }

    @PostMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        alertMessage = "Your session is terminated and you are logged out";
        return "redirect:/admin/login";
    }

    @GetMapping("/password/change")
    public String passwordChange(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("passwordPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "changepassword";
    }

    @PostMapping("/password/change/process")
    public String passwordChange(@ModelAttribute("passwordPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, Model model) {
        PylonResponsePayload response = adminService.changePassword(requestPayload, principal.getName());
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            return "redirect:/users/record/list";
        }
        model.addAttribute("dataPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        return "changepassword";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("mainPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "admindashboard";
    }

    @GetMapping("/roles")
    public String roles(Model model, Principal principal) {
        model.addAttribute("roleList", adminService.fetchRoleGroup());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "roles";
    }

    @GetMapping("/permissions")
    public String permission(Model model, Principal principal) {
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "permissions";
    }

    @GetMapping("/users")
    public String users(Model model, Principal principal) {
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "appusers";
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
