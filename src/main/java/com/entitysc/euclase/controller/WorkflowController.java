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
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/manage")
    public String workflow(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("expenseList", euclaseService.processFetchExpenseTypeList().getData());
        model.addAttribute("serviceList", euclaseService.processFetchServiceRequestList().getData());
        model.addAttribute("loanList", euclaseService.processFetchLoanTypeList().getData());
        model.addAttribute("leaveList", euclaseService.processFetchLeaveTypeList().getData());
        model.addAttribute("sessionDetails", userDetails);
        return "workflow";
    }

    @PostMapping("/create")
    public String workflowCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDocumentWorkflow(requestPayload);

        //Check the type of document
        if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Expense")) {
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
            resetAlertMessage();
            return "workflowexpense";
        } else if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Leave")) {
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
            resetAlertMessage();
            return "workflowleave";
        } else if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Loan")) {
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
            resetAlertMessage();
            return "workflowloan";
        } else {
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
            resetAlertMessage();
            return "workflowservice";
        }
    }

    @GetMapping("/expense")
    public String expenseWorkflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentWorkflow("Expense");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentTemplateBody(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/workflow/manage";
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
        return "workflowexpense";
    }

    @GetMapping("/service-request")
    public String serviceRequestWorkflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentWorkflow("Service");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentTemplateBody(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/workflow/manage";
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
        return "workflowservice";
    }

    @GetMapping("/loan")
    public String loanWorkflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentWorkflow("Loan");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentTemplateBody(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/workflow/manage";
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
        return "workflowloan";
    }

    @GetMapping("/leave")
    public String leaveWorkflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentWorkflow("Leave");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentTemplateBody(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", new EuclasePayload());
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/workflow/manage";
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
        return "workflowleave";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
