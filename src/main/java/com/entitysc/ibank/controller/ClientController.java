package com.entitysc.ibank.controller;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PasswordChangePayload;
import com.entitysc.ibank.payload.PylonPayload;
import com.entitysc.ibank.service.ClientService;
import com.entitysc.ibank.service.GenericService;
import com.google.gson.Gson;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Brian A. Okon okon.brian@gmail.com
 */
@Controller
public class ClientController {

    @Autowired
    GenericService genericService;
    @Autowired
    ClientService clientService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    Gson gson;
    @Value("${pylon.qrcode.image.url}")
    private String qrCodeImageUrl;
    @Value("${pylon.profile.image.url}")
    private String profileImageUrl;
    private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());
    private String alertMessage = "";

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal) {
        model.addAttribute("ibankPayload", new IBankPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("ibankPayload", new IBankPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("IBANK_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
        return "signin";
    }

    @PostMapping("/login/")
    public String login(@ModelAttribute("ibankPayload") @Valid IBankPayload requestPayload, HttpSession session, HttpServletRequest httpRequest, Model model) throws Exception {
        PylonPayload response = clientService.processSignin(requestPayload);
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
                List<String> userDetails = (List<String>) httpRequest.getSession().getAttribute("IBANK_SESSION_DETAILS");
                if (userDetails == null) {
                    userDetails = new ArrayList<>();
                    httpRequest.getSession().setAttribute("IBANK_SESSION_DETAILS", userDetails);
                }
                userDetails.add(profileImageUrl + response.getEmail() + ".png");
                userDetails.add(response.getFirstName());
                userDetails.add(response.getLastName());
                userDetails.add(response.getSalutation());
                httpRequest.getSession().setAttribute("IBANK_SESSION_DETAILS", userDetails);
                return "redirect:/dashboard";
            }
            default: {
                //Remove the Google Authenticator Code
                requestPayload.setOtp1("");
                requestPayload.setOtp2("");
                requestPayload.setOtp3("");
                requestPayload.setOtp4("");
                requestPayload.setOtp5("");
                requestPayload.setOtp6("");
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "signin";
            }
        }
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("ibankPayload", new IBankPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "signup";
    }

    @PostMapping("/signup/")
    public String processSignup(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonPayload response = clientService.processSignUp(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/signup";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "signup";
            }
        }
    }

    @GetMapping("/signup/activate")
    public String signUpActivation(@RequestParam("id") String id, Model model, HttpServletRequest request) {
        PylonPayload response = clientService.processSignUpActivation(id);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/signin";
            }
            case "30": {
                IBankPayload ibankPayload = new IBankPayload();
                ibankPayload.setEmail(response.getEmail());
                ibankPayload.setQrCodeImage(qrCodeImageUrl + response.getEmail() + ".png");
                model.addAttribute("ibankPayload", ibankPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                resetAlertMessage();
                return "authenticator";
            }
            default: {
                alertMessage = response.getResponseMessage();
                return "redirect:/signin";
            }
        }
    }

    @PostMapping("/signup/auth/")
    public String twoFactorAuthentication(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonPayload response = clientService.validateTwoFactorAuthentication(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/signin";
            }
            default: {
                requestPayload.setQrCodeImage(qrCodeImageUrl + response.getEmail() + ".png");
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                resetAlertMessage();
                return "authenticator";
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
        return "redirect:/login";
    }

    @GetMapping("/password/change")
    public String passwordChange(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("passwordPayload", new PasswordChangePayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "changepassword";
    }

    @PostMapping("/password/update")
    public String changePassword(@ModelAttribute("passwordPayload") @Valid IBankPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model, Principal principal) {
        PylonPayload response = clientService.changePassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            return "redirect:/users/record/list";
        }
        model.addAttribute("dataPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        return "changepassword";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal, Model model) {
        IBankPayload ibankPayload = new IBankPayload();      
        List<String> userDetails = (List<String>)httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));
        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "dashboard";
    }

    @GetMapping("/terms")
    public String terms(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("ibankPayload", new IBankPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("ibankPayload", new IBankPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "privacy";
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
