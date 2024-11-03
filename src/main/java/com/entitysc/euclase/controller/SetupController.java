package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import java.security.Principal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.entitysc.euclase.service.EuclaseService;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/department")
    public String department(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("departmentCount", euclaseService.processFetchDepartmentList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "department";
    }

    @PostMapping("/department/")
    public String department(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateDepartment(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("departmentCount", euclaseService.processFetchDepartmentList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "department";
    }

    @GetMapping(value = "/department/list")
    public String departmentList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
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
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        resetAlertMessage();
        return "departmentunit";
    }

    @PostMapping("/department/unit/")
    public String departmentUnit(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateDepartmentUnit(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/department/unit";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "departmentunit";
    }

    @GetMapping(value = "/department/unit/list")
    public String departmentUnitList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchDepartmentUnitList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("unitCount", euclaseService.processFetchDepartmentUnitList().getData().size());
        model.addAttribute("departmentList", euclaseService.processFetchDepartmentList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
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
        model.addAttribute("designationCount", euclaseService.processFetchDesignationList().getData().size());
        resetAlertMessage();
        return "designation";
    }

    @PostMapping("/designation/")
    public String designation(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
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
        model.addAttribute("dataList", euclaseService.processFetchDesignationList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("branchCount", euclaseService.processFetchBranchList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "branch";
    }

    @PostMapping("/branch/")
    public String branch(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateBranch(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/branch";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
        model.addAttribute("branchCount", euclaseService.processFetchBranchList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "branch";
    }

    @GetMapping(value = "/branch/list")
    public String branchList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchBranchList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        model.addAttribute("userList", euclaseService.processFetchAppUserList().getData());
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
        model.addAttribute("gradeLevelCount", euclaseService.processFetchGradeLevelList().getData().size());
        resetAlertMessage();
        return "gradelevel";
    }

    @PostMapping("/grade-level/")
    public String gradeLevel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
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
        model.addAttribute("dataList", euclaseService.processFetchGradeLevelList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
     * ************ Document Type
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
        model.addAttribute("documentCount", euclaseService.processFetchDocumentTypeList("All").getData().size());
        model.addAttribute("documentGroup", euclaseService.processFetchDocumentGroupList().getData());
        resetAlertMessage();
        return "documenttype";
    }

    @PostMapping("/document/type/create")
    public String documentType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateDocumentType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/document/type";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", euclaseService.processFetchDocumentTypeList("All").getData().size());
        model.addAttribute("documentGroup", euclaseService.processFetchDocumentGroupList().getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "documenttype";
    }

    @GetMapping(value = "/document/type/list")
    public String documentTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchDocumentTypeList("All").getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
        model.addAttribute("documentGroup", euclaseService.processFetchDocumentGroupList().getData());
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
     * *Document Group
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
        model.addAttribute("documentCount", euclaseService.processFetchDocumentGroupList().getData().size());
        resetAlertMessage();
        return "documentgroup";
    }

    @PostMapping("/document/group/create")
    public String documentGroup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
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
        model.addAttribute("dataList", euclaseService.processFetchDocumentGroupList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
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
    public String template(Model model, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("documentTypes", euclaseService.processFetchDocumentTypeList("All").getData());
        resetAlertMessage();
        return "template";
    }

    @PostMapping("/template/create")
    public String templateCreate(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processUpdateDocumentTemplate(requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "documenttemplate";
    }

    @GetMapping("/template/edit")
    public String template(@RequestParam("seid") String seid, Model model, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentType(seid);
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

    @GetMapping("/workflow")
    public String workflow(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("documentTypes", euclaseService.processFetchDocumentTypeList("All").getData());
        resetAlertMessage();
        return "workflow";
    }

    @GetMapping("/workflow/edit")
    public String workflow(@RequestParam("seid") String seid, Model model, HttpSession session) {
        PylonResponsePayload response = euclaseService.processFetchDocumentType(seid);
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
        PylonResponsePayload response = euclaseService.processUpdateDocumentWorkflow(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error");
        return "documentworkflow";
    }

    /**
     * Public Holidays
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/holidays")
    public String publicHoliday(Model model, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("documentCount", euclaseService.processFetchPublicHolidayList().getData().size());
        resetAlertMessage();
        return "publicholiday";
    }

    @PostMapping("/holidays/create")
    public String publicHoliday(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreatePublicHoliday(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/holidays";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", euclaseService.processFetchPublicHolidayList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "publicholiday";
    }

    @GetMapping(value = "/holidays/list")
    public String publicHolidayList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchPublicHolidayList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "publicholidaylist";
    }

    @GetMapping("/holidays/edit")
    public String editPublicholiday(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchPublicHoliday(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/holidays/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", euclaseService.processFetchPublicHolidayList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "publicholiday";
    }

    @GetMapping("/holidays/delete")
    public String deletePublicHoliday(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeletePublicHoliday(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/holidays/list";
    }

    /**
     * Service Level Agreement
     *
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/sla")
    public String sla(Model model, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("slaList", euclaseService.processFetchSLAList().getData());
        model.addAttribute("documentCount", euclaseService.processFetchSLAList().getData().size());
        resetAlertMessage();
        return "sla";
    }

    @PostMapping("/sla/create")
    public String sla(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        PylonResponsePayload response = euclaseService.processCreateSLA(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/sla";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", euclaseService.processFetchSLAList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "sla";
    }

    @GetMapping(value = "/sla/list")
    public String slaList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", euclaseService.processFetchSLAList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "slalist";
    }

    @GetMapping("/sla/edit")
    public String editSla(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchSLA(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/sla/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", euclaseService.processFetchSLAList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "sla";
    }

    @GetMapping("/sla/delete")
    public String deleteSla(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteSLA(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/sla/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
