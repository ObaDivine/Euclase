package com.entitysc.euclase.controller;

import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.service.EuclaseService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/manage")
    public String template(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "workflow";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
