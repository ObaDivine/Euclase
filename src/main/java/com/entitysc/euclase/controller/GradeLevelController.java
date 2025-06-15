package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.GradeLevelService;
import com.entitysc.euclase.service.PushNotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GradeLevelController {

    @Autowired
    GradeLevelService gradeLevelService;
    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/grade-level")
    @Secured("ROLE_MANAGE_GRADE_LEVEL")
    public String gradeLevel(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("gradeLevelCount", gradeLevelService.fetchGradeLevelList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "gradelevel";
    }

    @PostMapping("/grade-level/")
    public String gradeLevel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = gradeLevelService.processCreateGradeLevel(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/grade-level";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("gradeLevelCount", gradeLevelService.fetchGradeLevelList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "gradelevel";
    }

    @GetMapping(value = "/grade-level/list")
    @Secured("ROLE_MANAGE_GRADE_LEVEL")
    public String gradeLevelList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", gradeLevelService.fetchGradeLevelList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "gradelevellist";
    }

    @GetMapping("/grade-level/edit")
    @Secured("ROLE_MANAGE_GRADE_LEVEL")
    public String editGradeLevel(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = gradeLevelService.fetchGradeLevel(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/grade-level/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("gradeLevelCount", gradeLevelService.fetchGradeLevelList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "gradelevel";
    }

    @GetMapping("/grade-level/delete")
    @Secured("ROLE_MANAGE_GRADE_LEVEL")
    public String deleteGradeLevl(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = gradeLevelService.processDeleteGradeLevel(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/grade-level/list";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
