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
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";
    @Value("${euclase.document.id.allowuser}")
    private String allowUserDocumentId;

    @GetMapping("/new")
    public String document(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "document";
    }

    @GetMapping("/expense")
    public String expenseDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("expenseTypeList", euclaseService.processFetchExpenseTypeList().getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "expensedocinit";
    }

    @PostMapping("/expense/")
    public String expenseDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateExpenseDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", "success");
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "expensedocprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("expenseTypeList", euclaseService.processFetchExpenseTypeList().getData());
        return "expensedocinit";
    }

    @GetMapping("/leave")
    public String leaveDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("leaveTypeList", euclaseService.processFetchLeaveTypeList().getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "leavedocinit";
    }

    @PostMapping("/leave/")
    public String leaveDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLeaveDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", "success");
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "leavedocprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("leaveTypeList", euclaseService.processFetchLeaveTypeList().getData());
        return "leavedocinit";
    }

    @GetMapping("/loan")
    public String loanDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("loanTypeList", euclaseService.processFetchLoanTypeList().getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "loandocinit";
    }

    @PostMapping("/loan/")
    public String loan(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLoanDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", "success");
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "loandocprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("loanTypeList", euclaseService.processFetchLoanTypeList().getData());
        return "loandocinit";
    }

    @GetMapping("/service-request")
    public String serviceRequestDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "servicerequestdocinit";
    }

    @PostMapping("/service-request/")
    public String serviceRequest(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateServiceRequestDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", "success");
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "servicerequestdocprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
        return "servicerequestdocinit";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
