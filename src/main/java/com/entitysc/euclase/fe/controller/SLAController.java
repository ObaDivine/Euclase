package com.entitysc.euclase.fe.controller;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.service.SLAService;
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
@RequestMapping("/setup")
public class SLAController {

    @Autowired
    SLAService slaService;
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

    @GetMapping("/sla")
    @Secured("ROLE_MANAGE_SLA")
    public String sla(Model model, HttpSession session, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("slaList", slaService.fetchSLAList().getData());
        model.addAttribute("documentCount", slaService.fetchSLAList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "sla";
    }

    @PostMapping("/sla/create")
    public String sla(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = slaService.processCreateSLA(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/sla";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", slaService.fetchSLAList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "sla";
    }

    @GetMapping(value = "/sla/list")
    @Secured("ROLE_MANAGE_SLA")
    public String slaList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", slaService.fetchSLAList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "slalist";
    }

    @GetMapping("/sla/edit")
    @Secured("ROLE_MANAGE_SLA")
    public String editSla(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = slaService.fetchSLA(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/sla/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", slaService.fetchSLAList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "sla";
    }

    @GetMapping("/sla/delete")
    @Secured("ROLE_MANAGE_SLA")
    public String deleteSla(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = slaService.processDeleteSLA(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/sla/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
