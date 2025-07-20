package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.CompanyService;
import com.entitysc.euclase.service.DocumentTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/setup/document")
public class DocumentTypeController {

    @Autowired
    CompanyService companyService;
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

    @GetMapping("/type")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String documentType(Model model, HttpSession httpSession, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("documentCount", documentTypeService.fetchDocumentTypeList("All", httpSession.getAttribute("companyId").toString()).getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documenttype";
    }

    @PostMapping("/type/create")
    public String documentType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentTypeService.processCreateDocumentType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/type";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("documentCount", documentTypeService.fetchDocumentTypeList("All", httpSession.getAttribute("companyId").toString()).getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documenttype";
    }

    @GetMapping(value = "/type/list")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String documentTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", documentTypeService.fetchDocumentTypeList("All", httpSession.getAttribute("companyId").toString()).getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documenttypelist";
    }

    @GetMapping("/type/edit")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String editDocumentType(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest, HttpSession httpSession) {
        EuclaseResponsePayload response = documentTypeService.fetchDocumentType(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/type/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", documentTypeService.fetchDocumentTypeList("All", httpSession.getAttribute("companyId").toString()).getData().size());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documenttype";
    }

    @GetMapping("/type/delete")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String deleteDocumentType(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = documentTypeService.processDeleteDocumentType(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/document/type/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
