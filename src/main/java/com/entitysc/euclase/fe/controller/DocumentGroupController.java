package com.entitysc.euclase.fe.controller;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.service.CompanyService;
import com.entitysc.euclase.fe.service.DocumentGroupService;
import com.entitysc.euclase.fe.service.DocumentTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/setup/document")
public class DocumentGroupController {

    @Autowired
    DocumentGroupService documentGroupService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    CompanyService companyService;
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

    @GetMapping("/group")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String documentGroup(Model model, HttpSession session, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("documentCount", documentGroupService.fetchDocumentGroupList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentgroup";
    }

    @PostMapping("/group/create")
    public String documentGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentGroupService.processCreateDocumentGroup(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/group";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("documentCount", documentGroupService.fetchDocumentGroupList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documentgroup";
    }

    @GetMapping(value = "/group/list")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String documentGroupList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", documentGroupService.fetchDocumentGroupList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentgrouplist";
    }

    @GetMapping("/group/edit")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String editDocumentGroup(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = documentGroupService.fetchDocumentGroup(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/group/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("companyList", companyService.fetchCompanyList().getData());
        model.addAttribute("documentCount", documentGroupService.fetchDocumentGroupList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "documentgroup";
    }

    @GetMapping("/group/delete")
    @Secured("ROLE_MANAGE_DOCUMENT_GROUP")
    public String deleteDocumentGroup(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = documentGroupService.processDeleteDocumentGroup(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/document/group/list";
    }

    @GetMapping("/group/type/{id}")
    @ResponseBody
    public List<EuclasePayload> getDocumentTypes(@PathVariable("id") String id) {
        return documentTypeService.fetchDocumentGroupTypeList(id).getData();
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
