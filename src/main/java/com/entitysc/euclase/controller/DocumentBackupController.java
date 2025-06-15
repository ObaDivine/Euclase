package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.DataListResponsePayload;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.DocumentService;
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
public class DocumentBackupController {

    @Autowired
    DocumentService documentService;
    @Autowired
    PushNotificationService notificationService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/backup")
    @Secured("ROLE_BACKUP_RESTORE")
    public String backup(Model model, HttpSession session, Principal principal) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("backupList", documentService.fetchBackupList().getData());
        model.addAttribute("documentCount", documentService.fetchBackupList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "backup";
    }

    @PostMapping("/backup/create")
    public String backup(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = documentService.processCreateBackup(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/backup";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("documentCount", documentService.fetchBackupList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "backup";
    }

    @GetMapping(value = "/backup/list")
    @Secured("ROLE_BACKUP_RESTORE")
    public String backupList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", documentService.fetchBackupList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "backuplist";
    }

    @GetMapping("/backup/edit")
    @Secured("ROLE_BACKUP_RESTORE")
    public String editBackup(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = documentService.fetchBackup(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/setup/backup/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("documentCount", documentService.fetchBackupList().getData().size());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "backup";
    }

    @GetMapping("/backup/delete")
    @Secured("ROLE_BACKUP_RESTORE")
    public String deleteBackup(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = documentService.processDeleteBackup(seid, principal.getName());
        alertMessage = response.getResponseMessage();
       alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/setup/backup/list";
    }

    @GetMapping(value = "/restore")
    @Secured("ROLE_BACKUP_RESTORE")
    public String restore(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", documentService.fetchRestoreList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        DataListResponsePayload pushNotifications = notificationService.fetchUserPushNotification(principal.getName());
        model.addAttribute("notification", pushNotifications.getData());
        model.addAttribute("unreadMessageCount", pushNotifications.getData() == null ? 0 : pushNotifications.getData().stream().filter(t -> !t.isMessageRead()).count());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "restorelist";
    }

    @GetMapping("/backup/restore")
    @Secured("ROLE_BACKUP_RESTORE")
    public String restoreBackup(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = documentService.processCreateRestore(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/setup/restore";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
