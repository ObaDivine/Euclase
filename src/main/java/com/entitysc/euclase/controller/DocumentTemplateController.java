package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.DocumentService;
import com.entitysc.euclase.service.DocumentTypeService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/setup")
public class DocumentTemplateController {

    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Value("${euclase.client.name}")
    private String companyName;
    @Value("${euclase.client.url}")
    private String companyUrl;
    private String alertMessage = "";
    private String alertMessageType = "";

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("companyName", companyName);
        model.addAttribute("companyUrl", companyUrl);
    }

    @GetMapping("/template")
    @Secured("ROLE_MANAGE_DOCUMENT_TEMPLATE")
    public String template(Model model, HttpSession httpSession, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("documentTypes", documentTypeService.fetchDocumentTypeList("All", "0").getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "template";
    }

    @PostMapping("/template/create")
    public String templateCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processUpdateDocumentTemplate(requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "documenttemplate";
    }

    @GetMapping("/template/edit")
    @Secured("ROLE_MANAGE_DOCUMENT_TEMPLATE")
    public String template(@RequestParam("seid") String seid, Model model, HttpSession session, Principal principal) {
        EuclaseResponsePayload response = documentTypeService.fetchDocumentType(seid);
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
            requestPayload.setId(response.getData().getId());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/setup/template";
        }
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documenttemplate";
    }

    @GetMapping("/workflow/edit")
    @Secured("ROLE_MANAGE_DOCUMENT_TEMPLATE")
    public String workflow(@RequestParam("seid") String seid, Model model, HttpSession session, Principal principal) {
        EuclaseResponsePayload response = documentTypeService.fetchDocumentType(seid);
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentWorkflowBody(response.getData().getDocumentWorkflowBody());
            requestPayload.setDocumentTypeName(response.getData().getDocumentTypeName());
            requestPayload.setId(response.getData().getId());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/setup/workflow";
        }
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentworkflow";
    }

    @PostMapping("/workflow/create")
    public String workflowCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processUpdateDocumentWorkflow(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "documentworkflow";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
