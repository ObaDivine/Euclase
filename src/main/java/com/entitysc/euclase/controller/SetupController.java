package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.entitysc.euclase.service.EuclaseService;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    EuclaseService euclaseService;
    @Value("${euclase.document.type}")
    private String documentType;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/department")
    public String department(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        model.addAttribute("departmentCount", euclaseService.processFetchDepartmentList().getData().size());
        return "department";
    }

    @PostMapping("/department/")
    public String department(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDepartment(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("departmentCount", euclaseService.processFetchDepartmentList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "department";
    }

    @GetMapping(value = "/department/list")
    public String departmentList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "departmentlist";
    }

    @GetMapping("/department/edit")
    public String editDepartment(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchDepartment(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("departmentCount", euclaseService.processFetchDepartmentList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "department";
    }

    @GetMapping("/department/delete")
    public String deleteDepartment(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteDepartment(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/department/list";
    }

    @GetMapping("/department/unit")
    public String departmentUnit(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        return "departmentunit";
    }

    @PostMapping("/department/unit/")
    public String departmentUnit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDepartmentUnit(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/unit";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        return "departmentunit";
    }

    @GetMapping(value = "/department/unit/list")
    public String departmentUnitList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "departmentunitlist";
    }

    @GetMapping("/department/unit/edit")
    public String editDepartmentUnit(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchDepartmentUnit(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/unit/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        resetAlertMessage();
        return "departmentunit";
    }

    @GetMapping("/department/unit/delete")
    public String deleteDepartmentUnit(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteDepartmentUnit(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/department/unit/list";
    }

    @GetMapping("/designation")
    public String designation(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        model.addAttribute("designationCount", euclaseService.processFetchDesignationList().getData().size());
        return "designation";
    }

    @PostMapping("/designation/")
    public String designation(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDesignation(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/designation";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("designationCount", euclaseService.processFetchDesignationList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "designation";
    }

    @GetMapping(value = "/designation/list")
    public String designationList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "designationlist";
    }

    @GetMapping("/designation/edit")
    public String editDesignation(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchDesignation(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/designation/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("designationCount", euclaseService.processFetchDesignationList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "designation";
    }

    @GetMapping("/designation/delete")
    public String deleteDesignation(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteDesignation(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/designation/list";
    }

    @GetMapping("/branch")
    public String branch(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        model.addAttribute("branchCount", euclaseService.processFetchBranchList().getData().size());
        return "branch";
    }

    @PostMapping("/branch/")
    public String branch(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateBranch(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/branch";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("branchCount", euclaseService.processFetchBranchList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "branch";
    }

    @GetMapping(value = "/branch/list")
    public String branchList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "branchlist";
    }

    @GetMapping("/branch/edit")
    public String editBranch(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchBranch(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/branch/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("branchCount", euclaseService.processFetchBranchList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "branch";
    }

    @GetMapping("/branch/delete")
    public String deleteBranch(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteBranch(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/branch/list";
    }

    @GetMapping("/grade-level")
    public String gradeLevel(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        model.addAttribute("gradeLevelCount", euclaseService.processFetchGradeLevelList().getData().size());
        return "gradelevel";
    }

    @PostMapping("/grade-level/")
    public String gradeLevel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateGradeLevel(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/grade-level";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("gradeLevelCount", euclaseService.processFetchGradeLevelList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "gradelevel";
    }

    @GetMapping(value = "/grade-level/list")
    public String gradeLevelList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "gradelevellist";
    }

    @GetMapping("/grade-level/edit")
    public String editGradeLevel(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchGradeLevel(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/grade-level/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("gradeLevelCount", euclaseService.processFetchGradeLevelList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "gradelevel";
    }

    @GetMapping("/grade-level/delete")
    public String deleteGradeLevl(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteGradeLevel(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/grade-level/list";
    }

    /**
     * ************ Document Group
     *
     ***************
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/document/type")
    public String documentType(Model model, HttpSession session) {
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
        model.addAttribute("documentCount", euclaseService.processFetchDocumentTypeList("All").getData().size());
        model.addAttribute("documentTypes", documentType.split(","));
        return "documenttype";
    }

    @PostMapping("/document/type/create")
    public String documentType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDocumentType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/type";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", euclaseService.processFetchDocumentTypeList("All").getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documenttype";
    }

    @GetMapping(value = "/document/type/list")
    public String documentTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDocumentTypeList("All").getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documenttypelist";
    }

    @GetMapping("/document/type/edit")
    public String editDocumentType(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchDocumentType(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/type/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", euclaseService.processFetchDocumentTypeList("All").getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documenttype";
    }

    @GetMapping("/document/type/delete")
    public String deleteDocumentType(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteDocumentType(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/document/type/list";
    }

    /**
     * ************ Document Group
     *
     ***************
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/document/group")
    public String documentGroup(Model model, HttpSession session) {
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
        model.addAttribute("documentCount", euclaseService.processFetchDocumentGroupList().getData().size());
        return "documentgroup";
    }

    @PostMapping("/document/group/create")
    public String documentGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDocumentGroup(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/group";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", euclaseService.processFetchDocumentGroupList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documentgroup";
    }

    @GetMapping(value = "/document/group/list")
    public String documentGroupList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchDocumentGroupList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documentgrouplist";
    }

    @GetMapping("/document/group/edit")
    public String editDocumentGroup(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchDocumentGroup(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/group/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", euclaseService.processFetchDocumentGroupList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "documentgroup";
    }

    @GetMapping("/document/group/delete")
    public String deleteDocumentGroup(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteDocumentGroup(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/document/group/list";
    }

    @GetMapping("/template")
    public String template(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "template";
    }

    @PostMapping("/template/create")
    public String templateCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateDocumentTemplate(requestPayload);

        //Check the type of document
        if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Expense")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
            return "redirect:/template/expense";
        } else if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Leave")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
            return "redirect:/template/leave";
        } else if (requestPayload.getDocumentTemplateName().equalsIgnoreCase("Loan")) {
            alertMessage = response.getResponseMessage();
            alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
            return "redirect:/template/loan";
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
            return "redirect:/template/service-request";
        }
    }

    @GetMapping("/expense")
    public String templateExpense(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentTemplate("Expense");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setEditorData(response.getData().getDocumentTemplateBody());
            requestPayload.setEditor(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/template/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "templateexpense";
    }

    @GetMapping("/workflow")
    public String workflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentWorkflow("Expense");
        EuclasePayload requestPayload = new EuclasePayload();
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            requestPayload.setDocumentTemplateBody(response.getData().getDocumentTemplateBody());
            requestPayload.setDocumentTemplateName(response.getData().getDocumentTemplateName());
            model.addAttribute("euclasePayload", requestPayload);
        } else if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.RECORD_NOT_EXIST_CODE.getResponseCode())) {
            model.addAttribute("euclasePayload", requestPayload);
        } else {
            alertMessage = response.getResponseMessage();
            alertMessageType = "error";
            return "redirect:/workflow/manage";
        }
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }

        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        model.addAttribute("sessionDetails", userDetails);
        return "workflowexpense";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
