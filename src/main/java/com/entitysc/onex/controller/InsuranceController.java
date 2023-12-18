package com.entitysc.onex.controller;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.service.ClientService;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/motor")
    public String motorInsurance(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "motorinsurance";
    }

    @GetMapping("/life")
    public String lifeInsurance(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "lifeinsurance";
    }

    @GetMapping("/home")
    public String homeInsurance(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "homeinsurance";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
