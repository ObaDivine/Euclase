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
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        //Set session variables
        List<String> userDetails = (List<String>) session.getAttribute("EUCLASE_SESSION_DETAILS");
        if (userDetails == null) {
            userDetails = new ArrayList<>();
        }
        model.addAttribute("sessionDetails", userDetails);
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
            alertMessageType = "info";
            return "redirect:/setup/branch";
        }
        model.addAttribute("euclasePayload", requestPayload);
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

    @GetMapping("/leave")
    public String leaveType(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "leavetype";
    }

    @PostMapping("/leave/")
    public String leaveType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLeaveType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/leave";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "leavetype";
    }

    @GetMapping(value = "/leave/list")
    public String leaveTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchLeaveTypeList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "leavetypelist";
    }

    @GetMapping("/leave/edit")
    public String editLeaveType(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchLeaveType(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/leave/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "leavetype";
    }

    @GetMapping("/leave/delete")
    public String deleteLeaveType(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteLeaveType(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/leave/list";
    }

    @GetMapping("/loan")
    public String loanType(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "loantype";
    }

    @PostMapping("/loan/")
    public String loanType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateLoanType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/loan";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "loantype";
    }

    @GetMapping(value = "/loan/list")
    public String loanTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchLoanTypeList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "loantypelist";
    }

    @GetMapping("/loan/edit")
    public String editLoanType(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchLoanType(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/loan/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "loantype";
    }

    @GetMapping("/loan/delete")
    public String deleteLoan(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteLoanType(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/loan/list";
    }

    @GetMapping("/expense")
    public String expenseType(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "expensetype";
    }

    @PostMapping("/expense/")
    public String expenseType(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateExpenseType(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/expense";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "expensetype";
    }

    @GetMapping(value = "/expense/list")
    public String expenseTypeList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchExpenseTypeList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "expensetypelist";
    }

    @GetMapping("/expense/edit")
    public String editExpenseType(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchExpenseType(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/expense/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "expensetype";
    }

    @GetMapping("/expense/delete")
    public String deleteExpenseType(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteExpenseType(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/expense/list";
    }

    @GetMapping("/service-request")
    public String serviceRequest(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
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
        return "servicerequest";
    }

    @PostMapping("/service-request/")
    public String serviceRequest(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = euclaseService.processCreateServiceRequest(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/service-request";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "servicerequest";
    }

    @GetMapping(value = "/service-request/list")
    public String serviceRequestList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        EuclasePayload euclasePayload = new EuclasePayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("EUCLASE_SESSION_DETAILS");
        euclasePayload.setProfileImage(userDetails.get(0));
        euclasePayload.setFirstName(userDetails.get(1));
        euclasePayload.setLastName(userDetails.get(2));
        euclasePayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", euclaseService.processFetchServiceRequestList().getData());
        model.addAttribute("euclasePayload", euclasePayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "servicerequestlist";
    }

    @GetMapping("/service-request/edit")
    public String editServiceRequest(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        PylonResponsePayload response = euclaseService.processFetchServiceRequest(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/service-request/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "servicerequest";
    }

    @GetMapping("/service-request/delete")
    public String deleteServiceRequest(@RequestParam("seid") String seid, Model model, Principal principal) {
        PylonResponsePayload response = euclaseService.processDeleteServiceRequest(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/service-request/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
