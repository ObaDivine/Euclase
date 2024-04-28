package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.entitysc.euclase.service.EuclaseService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
    @Value("${euclase.document.type}")
    private String documentType;
    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String document(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentTypes", documentType.split(","));
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "document";
    }

    @GetMapping("/new")
    public String newDocument(@RequestParam("seid") String seid, Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        //Check if the signature is set. Index 11 holds the signature
        if (userDetails.get(11) == null || userDetails.get(11).equalsIgnoreCase("")) {
            alertMessage = messageSource.getMessage("appMessages.signature.notset", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/";
        }

        requestPayload.setDocumentType(seid);
        //Generate document id
        String documentId = euclaseService.generateDocumentId(requestPayload.getDocumentType());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("documentTypeList", euclaseService.processFetchDocumentTypeList(seid).getData());
        model.addAttribute("sessionDetails", userDetails);
        resetAlertMessage();
        return "documentinit";
    }

    @PostMapping("/init")
    public String documentInit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDocument(requestPayload);
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
        model.addAttribute("leaveTypeList", euclaseService.processFetchDocumentTypeList(requestPayload.getDocumentType()).getData());
        return "leavedocinit";
    }

    @PostMapping("/process")
    public String processDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/leave";
        }

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("leaveTypeList", euclaseService.processFetchDocumentTypeList(requestPayload.getDocumentType()).getData());
        resetAlertMessage();
        return "leavedocprocess";
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
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }
        requestPayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        requestPayload.setDocumentType(response.getPayload().getDocumentType());
        requestPayload.setDocumentId(response.getPayload().getDocumentId());
        BeanUtils.copyProperties(response.getPayload(), requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("documentList", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documentdetails";
    }

    @GetMapping("/pending")
    public String pendingDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchPendingDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "pendingdocument";
    }

    @GetMapping("/draft")
    public String draftDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDraftDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", requestPayload);
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
        requestPayload.setDocumentType("Expense");
        //Generate document id
        String documentId = seid;
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        requestPayload.setDocumentType(response.getPayload().getDocumentTypeCode());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("documentTypeList", euclaseService.processFetchDocumentTypeList(seid).getData());
        resetAlertMessage();
        return "expensedocinit";
    }

    @GetMapping("/draft/delete")
    public String deleteDraftDocument(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processDeleteDraftDocument(dt, seid);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/approve/details")
    public String approveDocument(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        //Check if the signature is set. Index 11 holds the signature
        if (userDetails.get(11) == null || userDetails.get(11).equalsIgnoreCase("")) {
            alertMessage = messageSource.getMessage("appMessages.signature.notset", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        requestPayload.setEditorData(response.getPayload().getDocumentTemplateBody());
        requestPayload.setEditor(response.getPayload().getDocumentTemplateBody());
        requestPayload.setDocumentTemplateName(response.getPayload().getDocumentTemplateName());
        requestPayload.setDocumentType(response.getPayload().getDocumentType());
        requestPayload.setDocumentId(response.getPayload().getDocumentId());
        model.addAttribute("euclasePayload", requestPayload);
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
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = euclaseService.processFetchDocumentWorkflow(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("workflowList", response.getWorkflowData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "workflowdocument";
    }

    @GetMapping("/search")
    public String documentSearch(@RequestParam("search") String search, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processSearchDocument(search).getData());
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/signature")
    public String signature(Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        requestPayload.setSignatureLink(userDetails.get(11));

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
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
        requestPayload.setSignatureLink(userDetails.get(11));
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
