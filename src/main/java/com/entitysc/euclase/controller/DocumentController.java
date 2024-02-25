package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.entitysc.euclase.service.EuclaseService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";
    @Value("${euclase.document.id.allowuser}")
    private String allowUserDocumentId;

    @PostMapping("/")
    public String documentHome(@ModelAttribute("euclasePayload") @Valid EuclasePayload requestPayload, Model model, HttpSession httpSession, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("expenseTypeList", euclaseService.processFetchExpenseTypeList().getData());
        model.addAttribute("leaveTypeList", euclaseService.processFetchLeaveTypeList().getData());
        model.addAttribute("loanTypeList", euclaseService.processFetchLoanTypeList().getData());
        model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);

        //Check the type of document
        if (requestPayload.getDocumentType().equalsIgnoreCase("Expense")) {
            resetAlertMessage();
            return "expensedoc";
        } else if (requestPayload.getDocumentType().equalsIgnoreCase("Leave")) {
            resetAlertMessage();
            return "leavedoc";
        } else if (requestPayload.getDocumentType().equalsIgnoreCase("Loan")) {
            resetAlertMessage();
            return "loandoc";
        } else {
            return "servicedoc";
        }
    }

    @PostMapping("/expense")
    public String expenseDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateExpenseDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/template";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "expensedoc";
    }

    @PostMapping("/leave")
    public String leaveDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLeaveDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/template";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "leavedoc";
    }

    @PostMapping("/loan")
    public String loan(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLoanDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/template";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "loandoc";
    }

    @PostMapping("/service-request")
    public String expense(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateServiceRequestDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/template";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "servicedoc";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
