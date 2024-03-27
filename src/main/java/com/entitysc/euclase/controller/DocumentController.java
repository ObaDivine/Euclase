package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.entitysc.euclase.service.EuclaseService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        requestPayload.setDocumentType("Expense");
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

    @PostMapping("/expense/init")
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

    @PostMapping("/expense/process")
    public String processExpenseDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateExpenseDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            resetAlertMessage();
            return "redirect:/document/expense";
        }
        requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
        requestPayload.setEditor(response.getData().getDocumentTemplateBody());
        requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("expenseTypeList", euclaseService.processFetchExpenseTypeList().getData());
        resetAlertMessage();
        return "expensedocprocess";
    }

    @GetMapping("/leave")
    public String leaveDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setDocumentType("Leave");
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

    @PostMapping("/leave/init")
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

    @PostMapping("/leave/process")
    public String processLeaveDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processLeaveDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/leave";
        }

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("leaveTypeList", euclaseService.processFetchLeaveTypeList().getData());
        resetAlertMessage();
        return "leavedocprocess";
    }

    @GetMapping("/loan")
    public String loanDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setDocumentType("Loan");
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

    @PostMapping("/loan/init")
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

    @PostMapping("/loan/process")
    public String processLoanDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLoanDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/loan";
        }

        requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
        requestPayload.setEditor(response.getData().getDocumentTemplateBody());
        requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("loanTypeList", euclaseService.processFetchLoanTypeList().getData());
        resetAlertMessage();
        return "loandocprocess";
    }

    @GetMapping("/service-request")
    public String serviceRequestDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setDocumentType("Service");
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

    @PostMapping("/service-request/init")
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

    @PostMapping("/service-request/process")
    public String processServiceRequestDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateServiceRequestDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/service-request";
        }
        requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
        requestPayload.setEditor(response.getData().getDocumentTemplateBody());
        requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
        resetAlertMessage();
        return "servicerequestdocprocess";
    }

    @GetMapping("/my")
    public String myDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchMyDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/details")
    public String documentDetails(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }
        euclasePayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        euclasePayload.setDocumentType(response.getPayload().getDocumentType());
        euclasePayload.setDocumentId(response.getPayload().getDocumentId());
        BeanUtils.copyProperties(response.getPayload(), euclasePayload);
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("documentList", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documentdetails";
    }

    @GetMapping("/pending")
    public String pendingDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchPendingDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "pendingdocument";
    }

    @GetMapping("/draft")
    public String draftDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDraftDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "draftdocument";
    }

    @GetMapping("/draft/details")
    public String draftDocumentDetails(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        BeanUtils.copyProperties(response.getPayload(), requestPayload);
        if (dt.equalsIgnoreCase("Expense")) {
            //Set session variables
            List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
            if (userDetails == null) {
                userDetails = new ArrayList<>();
            }

            requestPayload.setDocumentType("Expense");
            //Generate document id
            String documentId = seid;
            requestPayload.setDocumentId(documentId);
            requestPayload.setAllowUserDocumentId(allowUserDocumentId);
            requestPayload.setExpenseType(response.getPayload().getExpenseTypeCode());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", alertMessage);
            model.addAttribute("alertMessageType", alertMessageType);
            model.addAttribute("expenseTypeList", euclaseService.processFetchExpenseTypeList().getData());
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "expensedocinit";
        } else if (dt.equalsIgnoreCase("Service")) {
            //Set session variables
            List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
            if (userDetails == null) {
                userDetails = new ArrayList<>();
            }

            requestPayload.setDocumentType("Service");
            //Generate document id
            String documentId = seid;
            requestPayload.setDocumentId(documentId);
            requestPayload.setAllowUserDocumentId(allowUserDocumentId);
            requestPayload.setServiceType(response.getPayload().getServiceRequestCode());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", alertMessage);
            model.addAttribute("alertMessageType", alertMessageType);
            model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "servicerequestdocinit";
        } else if (dt.equalsIgnoreCase("Loan")) {
            //Set session variables
            List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
            if (userDetails == null) {
                userDetails = new ArrayList<>();
            }

            requestPayload.setDocumentType("Loan");
            //Generate document id
            String documentId = seid;
            requestPayload.setDocumentId(documentId);
            requestPayload.setAllowUserDocumentId(allowUserDocumentId);
            requestPayload.setLoanType(response.getPayload().getLoanTypeCode());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", alertMessage);
            model.addAttribute("alertMessageType", alertMessageType);
            model.addAttribute("loanTypeList", euclaseService.processFetchLoanTypeList().getData());
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "loandocinit";
        } else if (dt.equalsIgnoreCase("Leave")) {
            //Set session variables
            List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
            if (userDetails == null) {
                userDetails = new ArrayList<>();
            }

            requestPayload.setDocumentType("Leave");
            //Generate document id
            String documentId = seid;
            requestPayload.setDocumentId(documentId);
            requestPayload.setAllowUserDocumentId(allowUserDocumentId);
            requestPayload.setLeaveType(response.getPayload().getLeaveTypeCode());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", alertMessage);
            model.addAttribute("alertMessageType", alertMessageType);
            model.addAttribute("leaveTypeList", euclaseService.processFetchLeaveTypeList().getData());
            model.addAttribute("sessionDetails", userDetails);
            resetAlertMessage();
            return "leavedocinit";
        }

        //Unknow document type
        alertMessage = "Unknown document type";
        alertMessageType = "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/draft/delete")
    public String deleteDraftDocument(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processDeleteDraftDocument(dt, seid);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/approve/details")
    public String approveDocument(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }
        euclasePayload.setEditorData(response.getPayload().getDocumentTemplateBody());
        euclasePayload.setEditor(response.getPayload().getDocumentTemplateBody());
        euclasePayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        euclasePayload.setDocumentType(response.getPayload().getDocumentType());
        euclasePayload.setDocumentId(response.getPayload().getDocumentId());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("documentList", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "approvedocument";
    }

    @PostMapping("/approve")
    public String approveDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processApproveDocument(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/pending";
    }

    @GetMapping("/workflow")
    public String documentWorkflow(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentWorkflow(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "workflowdocument";
    }

    @GetMapping("/search")
    public String documentSearch(@RequestParam("search") String search, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processSearchDocument(search).getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/signature")
    public String signature(Model model, Principal principal, HttpSession httpSession) {
        //Set session variables
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        EuclasePayload euclasePayload = new EuclasePayload();
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        euclasePayload.setSignatureLink(userDetails.get(10));

        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("serviceTypeList", euclaseService.processFetchServiceRequestList().getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "signature";
    }

    @PostMapping("/signature/process")
    public String uploadSignature(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processDocumentSignature(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/signature";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
