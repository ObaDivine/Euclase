package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
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
@RequestMapping("/push/notification")
public class NotificationController {

    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String pushNotification(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("documentCount", notificationService.fetchPushNotificationList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "pushnotification";
    }

    @PostMapping("/create")
    public String pushNotification(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession session, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = notificationService.processCreatePushNotification(requestPayload, principal.getName());
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/push/notification/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", notificationService.fetchPushNotificationList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "pushnotification";
    }

    @GetMapping("/edit")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String pushNotification(@RequestParam("seid") String id, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.fetchPushNotification(id, false);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/push/notification/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", notificationService.fetchPushNotificationList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "pushnotification";
    }

    @GetMapping("/batch/edit")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String batchUpdatePushNotification(@RequestParam("seid") String id, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.fetchPushNotification(id, true);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/push/notification/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", notificationService.fetchPushNotificationList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "pushnotification";
    }

    @GetMapping("/list")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String pushNotification(Model model, Principal principal) {
        model.addAttribute("dataList", notificationService.fetchPushNotificationList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "pushnotificationlist";
    }

    @GetMapping("/delete")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String deletePushNotification(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.processDeletePushNotification(seid, principal.getName(), false);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/push/notification/list";
    }

    @GetMapping("/batch/delete")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String batchDeletePushNotification(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = notificationService.processDeletePushNotification(seid, principal.getName(), true);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/push/notification/list";
    }

    @GetMapping("/self/delete")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String deletePushNotificationByUser(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = notificationService.processDeletePushNotification(seid, principal.getName(), false);
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        String requestUri = httpRequest.getHeader("Referer");
        return "redirect:" + requestUri;
    }

    @GetMapping("/update")
    @Secured("ROLE_MANAGE_NOTIFICATION")
    public String pushNotificationRead(@RequestParam("seid") String seid, @RequestParam("rstat") String readStatus, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = notificationService.processUpdateSelfPushNotification(seid, principal.getName(), readStatus);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        String requestUri = httpRequest.getHeader("Referer");
        return "redirect:" + requestUri;
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
