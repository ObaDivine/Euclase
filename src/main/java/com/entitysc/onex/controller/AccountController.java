package com.entitysc.onex.controller;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.entitysc.onex.service.ClientService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/statement")
    public String transactionHistory(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processTransactionHistory(onexPayload).getData());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "accountstatement";
    }

    @PostMapping("/statement/")
    public String transactionHistory(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processTransactionHistory(requestPayload).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "accountstatement";
    }

    @GetMapping("/details")
    public String accountDetails(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "accountdetails";
    }

    @PostMapping("/image/upload")
    public String imageUpload(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processImageUpload(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/account/details";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
