package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/template")
public class TemplateController {

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
        return "template";
    }

    @PostMapping("/create")
    public String templateCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
//        PylonResponsePayload response = euclaseService.processCreateDepartment(requestPayload);
//        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
//            alertMessage = response.getResponseMessage();
//            alertMessageType = "success";
//            return "redirect:/template/department";
//        }
        requestPayload.setEditor("<figure class=\"table\"><table><tbody><tr><td>Request Date</td><td>{{REQUEST_DATE}}</td></tr><tr><td>Request By</td><td>{{REQUEST_BY}}</td></tr></tbody></table></figure>");
        model.addAttribute("euclasePayload", requestPayload);
//        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "template";
    }

    @GetMapping("/expense")
    public String templateExpense(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentTemplate("Expense");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/template/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "templateexpense";
    }

    @GetMapping("/service")
    public String templateService(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentTemplate("Service");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/template/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "templateservice";
    }

    @GetMapping("/leave")
    public String templateLeave(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentTemplate("Leave");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/template/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "templateleave";
    }

    @GetMapping("/loan")
    public String templateLoan(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentTemplate("Loan");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/template/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "templateloan";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
