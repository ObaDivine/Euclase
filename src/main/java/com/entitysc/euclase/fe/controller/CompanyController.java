package com.entitysc.euclase.fe.controller;

import com.entitysc.euclase.fe.constant.ResponseCodes;
import com.entitysc.euclase.fe.payload.EuclasePayload;
import com.entitysc.euclase.fe.payload.EuclaseResponsePayload;
import com.entitysc.euclase.fe.service.BranchService;
import com.entitysc.euclase.fe.service.CompanyService;
import com.entitysc.euclase.fe.service.DocumentGroupService;
import com.entitysc.euclase.fe.service.UserService;
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
@RequestMapping("/setup")
public class CompanyController {

    @Autowired
    CompanyService companyService;
    @Autowired
    BranchService branchService;
    @Autowired
    DocumentGroupService documentGroupService;
    @Autowired
    UserService userService;
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

    @GetMapping("/company")
    @Secured("ROLE_MANAGE_COMPANY")
    public String company(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("companyCount", companyService.fetchCompanyList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "company";
    }

    @PostMapping("/company/")
    public String company(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = companyService.processCreateCompany(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/company";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("companyCount", companyService.fetchCompanyList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "company";
    }

    @GetMapping(value = "/company/list")
    @Secured("ROLE_MANAGE_COMPANY")
    public String companyList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", companyService.fetchCompanyList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "companylist";
    }

    @GetMapping("/company/edit")
    @Secured("ROLE_MANAGE_COMPANY")
    public String editCompany(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = companyService.fetchCompany(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/company/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("companyCount", companyService.fetchCompanyList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "company";
    }

    @GetMapping("/company/delete")
    @Secured("ROLE_MANAGE_COMPANY")
    public String deleteCompany(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = companyService.processDeleteCompany(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/company/list";
    }

    @GetMapping("/company/branch/{id}")
    @ResponseBody
    public List<EuclasePayload> getBranches(@PathVariable("id") String id) {
        return branchService.fetchCompanyBranchList(id).getData();
    }

    @GetMapping("/company/document/group/{id}")
    @ResponseBody
    public List<EuclasePayload> getDocumentGroup(@PathVariable("id") String id) {
        return documentGroupService.fetchCompanyDocumentGroupList(id).getData();
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
