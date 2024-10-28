package com.entitysc.euclase.controller;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.RequestOutputItem;
import com.asprise.imaging.core.Result;
import com.asprise.imaging.core.scan.twain.Source;
import com.asprise.imaging.scan.ui.workbench.AspriseScanUI;
import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.entitysc.euclase.service.EuclaseService;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String document(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("documentTypes", euclaseService.processFetchDocumentTypeList("All").getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "document";
    }

    @GetMapping("/new")
    public String newDocument(@RequestParam("seid") String seid, Model model, HttpSession httpSession) {
        PylonResponsePayload response = euclaseService.processFetchDocumentType(seid);
        EuclasePayload requestPayload = new EuclasePayload();
        requestPayload.setDocumentWorkflowBody(response.getData().getDocumentWorkflowBody());
        requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
        requestPayload.setDocumentType(String.valueOf(response.getData().getId())); //Pass document type id for lookup
        //Check if the signature is set. Index 11 holds the signature
        if (httpSession.getAttribute("signatureLink").toString() == null || httpSession.getAttribute("signatureLink").toString().equalsIgnoreCase("")) {
            alertMessage = messageSource.getMessage("appMessages.signature.notset", new Object[0], Locale.ENGLISH);
            alertMessageType = "error";
            return "redirect:/document/";
        }

        requestPayload.setDocumentType(seid);
        //Generate document id
        String documentId = euclaseService.generateDocumentId(response.getData().getDocumentGroupCode());
        requestPayload.setDocumentId(documentId);
        requestPayload.setAllowUserDocumentId(allowUserDocumentId);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentinit";
    }

    @PostMapping("/init")
    public String documentInit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        PylonResponsePayload response = euclaseService.processCreateDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
            model.addAttribute("euclasePayload", requestPayload);
            model.addAttribute("alertMessage", response.getResponseMessage());
            model.addAttribute("alertMessageType", "success");
            resetAlertMessage();
            return "documentprocess";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documentinit";
    }

    @PostMapping("/process")
    public String processDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        PylonResponsePayload response = euclaseService.processDocument(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/document/";
        }

        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "documentprocess";
    }

    @GetMapping("/my")
    public String myDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("dataList", euclaseService.processFetchMyDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "mydocument";
    }

    @GetMapping("/details")
    public String documentDetails(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
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
        model.addAttribute("dataList", euclaseService.processFetchPendingDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "pendingdocument";
    }

    @GetMapping("/draft")
    public String draftDocument(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession httpSession) {
        model.addAttribute("dataList", euclaseService.processFetchDraftDocuments(principal.getName()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        PylonResponsePayload response = euclaseService.processDeleteDraftDocument(dt, seid);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/document/draft";
    }

    @GetMapping("/approve/details")
    public String approveDocument(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
//        requestPayload.setProfileImage(httpSession.getAttribute("profileImage").toString());
//        requestPayload.setFirstName(httpSession.getAttribute("firstName").toString());
//        requestPayload.setLastName(httpSession.getAttribute("lastName").toString());
//        requestPayload.setUsername(httpSession.getAttribute("username").toString());
//        requestPayload.setSignatureLink(httpSession.getAttribute("signatureLink").toString());
        DataListResponsePayload response = euclaseService.processFetchDocumentDetails(dt, seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/document/pending";
        }

        //Check if the signature is set. Index 11 holds the signature
        if (httpSession.getAttribute("signatureLink").toString() == null || httpSession.getAttribute("signatureLink").toString().equalsIgnoreCase("")) {
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
        PylonResponsePayload response = euclaseService.processApproveDocument(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/pending";
    }

    @GetMapping("/workflow")
    public String documentWorkflow(@RequestParam("dt") String dt, @RequestParam("seid") String seid, Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
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
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "signature";
    }

    @PostMapping("/signature/process")
    public String uploadSignature(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        PylonResponsePayload response = euclaseService.processDocumentSignature(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/signature";
    }

    @GetMapping("/scan")
    public String scan(Model model, Principal principal, HttpSession httpSession) {
        try {
            Imaging imaging = new Imaging("myApp", 0);
            RequestOutputItem out = new RequestOutputItem(Imaging.OUTPUT_SAVE, Imaging.FORMAT_PDF).setSavePath("C\\Brian\\Aspire.pdf");
            Request req = new Request().addOutputItem(out);
            Result resultX = new AspriseScanUI().setRequest(req).setInstruction("Scan <b>test</b>").showDialog(null, "Dialog Title", true, null);

            List<Source> sourcesWithCaps = imaging.scanListSources(false, "all", true, true);
            Result result = imaging.scan(Request.fromJson("{" + "\"output_settings\" : [ {" + "  \"type\" : \"save\","
                    + "  \"format\" : \"pdf\"," + "  \"save_path\" : \"C\\\\Brian\\\\Aspire.pdf\"" + "} ]" + "}"), "select", true, false);
            String okon = "The main man";
        } catch (Exception ex) {
            String kalis = "The man";
        }

        return "documentscan";
    }

    @GetMapping("/archive")
    public String upload(Model model, Principal principal, HttpSession httpSession) {
        EuclasePayload requestPayload = new EuclasePayload();
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentarchive";
    }

    @PostMapping("/archive/process")
    public String uploadDocument(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        PylonResponsePayload response = euclaseService.processDocumentArchiving(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/document/archive";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
