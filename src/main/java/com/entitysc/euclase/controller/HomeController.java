package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
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
import com.entitysc.euclase.service.PushNotificationService;
import com.entitysc.euclase.service.UserService;
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

/**
 *
 * @author briano
 */
@Controller
public class HomeController implements ErrorController {

    @Autowired
    UserService userService;
    @Autowired
    PushNotificationService notificationService;
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
        EuclaseResponsePayload response = userService.processSignin(requestPayload);
        switch (response.getResponseCode()) {
            case "00" -> {
                //Create authorities
                List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
                List<String> roles = Arrays.asList(response.getData().getRole().split(","));
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
    public String about(Model model, HttpServletRequest request, HttpSession httpSession, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "about";
    }

    @GetMapping("/signup/activate")
    public String signupActivation(@RequestParam("seid") String seid, Model model, HttpServletRequest request, HttpSession httpSession) {
        String decryptedParam = userService.decryptString(seid, encryptionKey);
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
        EuclaseResponsePayload response = userService.processChangeDefaultPassword(requestPayload);
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
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "changepassword";
    }

    @PostMapping("/change-password/")
    public String changePassword(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = userService.changePassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";

            //Logout the user and return to login page
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, auth);
            }
            return "redirect:/";
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
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "forgotpassword";
    }

    @PostMapping("/forgot-password/")
    public String forgotPassword(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        EuclaseResponsePayload response = userService.forgotPassword(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "forgotpassword";
    }

    @GetMapping("/security-question")
    public String securityQuestion(HttpServletRequest request, HttpServletResponse response, Principal principal, Model model) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "changesecurityquestion";
    }

    @PostMapping("/security-question/")
    public String securityQuestion(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Model model, Principal principal) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = userService.changeSecurityQuestion(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/dashboard";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "changesecurityquestion";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal, Model model) {
        EuclaseResponsePayload response = userService.fetchProfileDetails(principal.getName());
        //Check if security question has been set
        if (response.getData().getSecurityQuestion().equalsIgnoreCase("NA")) {
            return "redirect:/security-question";
        }
        httpSession.setAttribute("profileImage", response.getData().getUsername() + ".png");
        httpSession.setAttribute("firstName", response.getData().getFirstName());
        httpSession.setAttribute("lastName", response.getData().getLastName());
        httpSession.setAttribute("title", response.getData().getSalutation());
        httpSession.setAttribute("mobileNumber", response.getData().getMobileNumber());
        httpSession.setAttribute("mobileNumberVerified", response.getData().getMobileNumberVerified());
        httpSession.setAttribute("username", response.getData().getUsername());
        httpSession.setAttribute("enableMFA", response.getData().getEnableTwoFactorAuth());
        httpSession.setAttribute("itemCounts", String.valueOf(response.getData().getItemCounts()));
        httpSession.setAttribute("signatureLink", response.getData().getSignatureLink());
        httpSession.setAttribute("documentCount", response.getData().getDocumentCount());
        httpSession.setAttribute("documentViolatedSLACount", response.getData().getDocumentViolatedSLACount());
        httpSession.setAttribute("daysToDate", response.getData().getDaysToDate());
        httpSession.setAttribute("dataList", response.getData());
        httpSession.setAttribute("documentArchiveGroupCode", response.getData().getDocumentGroupCode());
        httpSession.setAttribute("releaseDate", response.getData().getReleaseDate());
        httpSession.setAttribute("releaseVersion", response.getData().getReleaseVersion());
        httpSession.setAttribute("companyName", response.getData().getCompanyName());
        httpSession.setAttribute("branchName", response.getData().getBranchName());
        httpSession.setAttribute("departmentName", response.getData().getDepartmentName());
        httpSession.setAttribute("unitName", response.getData().getDepartmentUnitName());
        httpSession.setAttribute("companyId", response.getData().getCompany());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "dashboard";
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
