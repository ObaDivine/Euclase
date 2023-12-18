package com.entitysc.onex.controller;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.entitysc.onex.service.ClientService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Brian A. Okon okon.brian@gmail.com
 */
@Controller
public class ClientController {

    @Autowired
    PushNotificationController pushNotification;
    @Autowired
    ClientService clientService;
    @Autowired
    MessageSource messageSource;
    @Value("${pylon.qrcode.image.url}")
    private String qrCodeImageUrl;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "index";
    }

    @PostMapping("/contact")
    public String contactUs(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model) {
        String response = clientService.processContactUs(requestPayload);
        alertMessage = response;
        alertMessageType = "success";
        return "redirect:/";

    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("ONEX_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
        return "signin";
    }

    @PostMapping("/login/")
    public String login(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpSession session, HttpServletRequest httpRequest, Model model) throws Exception {
        PylonResponsePayload response = clientService.processSignin(requestPayload);
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
                List<String> userDetails = (List<String>) httpRequest.getSession().getAttribute("ONEX_SESSION_DETAILS");
                if (userDetails == null || userDetails.isEmpty()) {
                    userDetails = new ArrayList<>();
                    httpRequest.getSession().setAttribute("ONEX_SESSION_DETAILS", userDetails);
                }

                //Check if two factor authentication is enabled
                if (response.getData().getEnableTwoFactorAuth().equalsIgnoreCase("true")) {
                    OneXPayload onexPayload = new OneXPayload();
                    onexPayload.setEnableTwoFactorAuth(response.getData().getEnableTwoFactorAuth());
                    onexPayload.setUsername(response.getData().getUsername());
                    model.addAttribute("onexPayload", onexPayload);
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
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "signin";
            }
        }
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "signup";
    }

    @PostMapping("/signup/")
    public String processSignup(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonResponsePayload response = clientService.processSignUp(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/signup";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "signup";
            }
        }
    }

    @PostMapping("/signup/activation")
    public String processSignupActivation(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonResponsePayload response = clientService.processSignUpActivation(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode();
        return "redirect:/dashboard";
    }

    @PostMapping("/qrcode/generate")
    public String generateQRCode(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model, HttpServletRequest request) {
        PylonResponsePayload response = clientService.processQRCodeGenerate(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                OneXPayload onexPayload = new OneXPayload();
                onexPayload.setUsername(response.getData().getUsername());
                onexPayload.setQrCodeImage(qrCodeImageUrl + requestPayload.getUsername() + ".png");
                model.addAttribute("onexPayload", onexPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "success");
                resetAlertMessage();
                return "authenticator";
            }
            case "02": {
                alertMessage = response.getResponseMessage();
                OneXPayload onexPayload = new OneXPayload();
                onexPayload.setUsername(response.getData().getUsername());
                onexPayload.setQrCodeImage(qrCodeImageUrl + requestPayload.getUsername() + ".png");
                model.addAttribute("onexPayload", onexPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                resetAlertMessage();
                return "authenticator";
            }
            default: {
                alertMessage = response.getResponseMessage();
                return "redirect:/signin";
            }
        }
    }

    @GetMapping("/otp/mobile/{mobileNumber}")
    @ResponseBody
    public String twoFactorAuthentication(@PathVariable("mobileNumber") String mobileNumber, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
        PylonResponsePayload response = clientService.processSendOTP(mobileNumber, "Entity One X Mobile Number Verification. Please use the OTP");
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "success";
            }
            default: {
                alertMessage = response.getResponseMessage();
                alertMessageType = "error";
                return "failed";
            }
        }
    }

    @PostMapping("/login/twofa")
    public String twofa(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpSession session, HttpServletRequest httpRequest, Model model) throws Exception {
        PylonResponsePayload response = clientService.processValidateTwoFactor(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                return "redirect:/dashboard";
            }
            default: {
                requestPayload.setOtp("");
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "signintwofa";
            }
        }
    }

    @GetMapping("/twofa/")
    public String twofa(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        if (!userDetails.isEmpty()) {
            onexPayload.setProfileImage(userDetails.get(0));
            onexPayload.setFirstName(userDetails.get(1));
            onexPayload.setLastName(userDetails.get(2));
            onexPayload.setSalutation(userDetails.get(3));
            onexPayload.setMobileNumber(userDetails.get(4));
            onexPayload.setMobileNumberVerified(userDetails.get(5));
            onexPayload.setWalletId(userDetails.get(6));
            onexPayload.setWalletBalance(userDetails.get(7));
            onexPayload.setUsername(userDetails.get(8));
            onexPayload.setEnableTwoFactorAuth(userDetails.get(9));
            onexPayload.setQrCodeImage(qrCodeImageUrl + userDetails.get(8) + ".png");
        }
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "authenticator";
    }

    @PostMapping("/twofa/update")
    public String enableTwofa(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpSession httpSession, HttpServletRequest httpRequest, Model model) throws Exception {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        PylonResponsePayload response = clientService.processEnableTwoFactor(userDetails.get(8));
        userDetails.set(9, response.getData().getEnableTwoFactorAuth());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/twofa/";
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
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "changepassword";
    }

    @PostMapping("/change-password/")
    public String changePassword(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.changePassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/change-password";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "changepassword";
    }

    @GetMapping("/forgot-password")
    public String forgotChange(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "forgotpassword";
    }

    @PostMapping("/forgot-password/")
    public String forgotPassword(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        PylonResponsePayload response = clientService.forgotPassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/login";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "forgotpassword";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal, Model model) {
        PylonResponsePayload response = clientService.processFetchProfileDetails(principal.getName());
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
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
            httpRequest.getSession().setAttribute("ONEX_SESSION_DETAILS", userDetails);
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
            httpRequest.getSession().setAttribute("ONEX_SESSION_DETAILS", userDetails);
        }

        //Set the session variables
        OneXPayload onexPayload = new OneXPayload();
        onexPayload.setProfileImage(response.getData().getUsername() + ".png");
        onexPayload.setFirstName(response.getData().getFirstName());
        onexPayload.setLastName(response.getData().getLastName());
        onexPayload.setSalutation(response.getData().getSalutation());
        onexPayload.setMobileNumber(response.getData().getMobileNumber());
        onexPayload.setMobileNumberVerified(response.getData().getMobileNumberVerified());
        onexPayload.setWalletId(response.getData().getWalletId());
        onexPayload.setWalletBalance(response.getData().getWalletBalance());
        onexPayload.setUsername(response.getData().getUsername());
        onexPayload.setEnableTwoFactorAuth(response.getData().getEnableTwoFactorAuth());
        onexPayload.setItemCounts(response.getData().getItemCounts());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "dashboard";
    }

    @GetMapping("/pin")
    public String pin(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "changepin";
    }

    @PostMapping("/pin/")
    public String changePin(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.changePin(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/pin";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "changepin";
    }

    @GetMapping("/security-question")
    public String securityQuestion(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "changesecurityquestion";
    }

    @PostMapping("/security-question/")
    public String securityQuestion(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.changeSecurityQuestion(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/security-question";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "changesecurityquestion";
    }

    @GetMapping("/terms")
    public String terms(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("onexPayload", new OneXPayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "privacy";
    }

    @PostMapping("/wallet/fund")
    public String fundWalletFromCard(@ModelAttribute("onexPayload") @Valid OneXPayload requestPayload, HttpSession httpSession, Model model, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processFundWalletUsingCard(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase("00")) {
            userDetails.set(7, response.getData().getWalletBalance());
            return "redirect:" + response.getData().getAuthorizationUrl();
        }
        alertMessage = response.getResponseMessage();
        alertMessageType = "error";
        return "redirect:/dashboard";
    }

    @GetMapping("/callback/wallet/fund")
    public String fundWalletCallback(@RequestParam("trxref") String trxref, @RequestParam("reference") String reference) {
        clientService.processFundWalletPaymentConfirm(trxref);
        alertMessage = messageSource.getMessage("appMessages.success.fundwallet", new Object[0], Locale.ENGLISH);
        alertMessageType = "success";
        return "redirect:/dashboard";
    }

    @PostMapping("/webhook")
    public void callbacks(@RequestBody PylonPayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model, Principal principal) {
        clientService.processCallback(requestPayload);
        //Send push notification to the customer
        pushNotification.pushNotification(requestPayload);
    }

    @GetMapping("/faq")
    public String faq(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal, Model model) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        if (!userDetails.isEmpty()) {
            onexPayload.setProfileImage(userDetails.get(0));
            onexPayload.setFirstName(userDetails.get(1));
            onexPayload.setLastName(userDetails.get(2));
            onexPayload.setSalutation(userDetails.get(3));
            onexPayload.setMobileNumber(userDetails.get(4));
            onexPayload.setMobileNumberVerified(userDetails.get(5));
            onexPayload.setWalletId(userDetails.get(6));
            onexPayload.setWalletBalance(userDetails.get(7));
            onexPayload.setUsername(userDetails.get(8));
            onexPayload.setEnableTwoFactorAuth(userDetails.get(9));
            onexPayload.setQrCodeImage(qrCodeImageUrl + userDetails.get(8) + ".png");
        }
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "faq";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
