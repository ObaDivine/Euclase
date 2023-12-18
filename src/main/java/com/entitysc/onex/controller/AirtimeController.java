package com.entitysc.onex.controller;

import com.entitysc.onex.constant.ResponseCodes;
import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.entitysc.onex.service.ClientService;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/airtime")
public class AirtimeController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/single")
    public String airtime(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "airtime";
    }

    @PostMapping("/single/")
    public String airtime(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processSingleAirtime(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.REQUEST_PROCESSING.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "info";
            return "redirect:/airtime/single";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "airtime";
    }

    @GetMapping(value = "/transaction")
    public String airtimeTransaction(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processAirtimeTransaction(onexPayload).getData());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "airtimetransaction";
    }

    @PostMapping("/transaction/")
    public String airtimeTransaction(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processAirtimeTransaction(requestPayload).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "airtimetransaction";
    }

    @GetMapping(value = "/bulk")
    public String bulk(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "bulkairtime";
    }

    @PostMapping("/bulk/")
    public String bulkAirtime(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processBulkAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/airtime/bulk";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "bulkairtime";
            }
        }
    }

    @GetMapping(value = "/schedule")
    public String scheduled(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("dataList", clientService.processFetchScheduledAirtime(onexPayload.getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledairtime";
    }

    @PostMapping("/schedule/")
    public String scheduleAirtime(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        //Check if the process is Create or Update
        PylonResponsePayload response;
        if (requestPayload.getId().equalsIgnoreCase("")) {
            response = clientService.processScheduledAirtime(requestPayload);
        } else {
            response = clientService.processUpdateScheduledAirtime(requestPayload);
        }
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/airtime/schedule";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("dataList", clientService.processFetchScheduledAirtime(requestPayload.getUsername()).getData());
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "success");
                return "scheduledairtime";
            }
        }
    }

    @GetMapping(value = "/schedule/edit/{id}")
    public String editSchedule(@PathVariable("id") String id, Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        PylonResponsePayload response = clientService.processFetchScheduledAirtimeUsingId(id);
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        response.getData().setProfileImage(userDetails.get(0));
        response.getData().setFirstName(userDetails.get(1));
        response.getData().setLastName(userDetails.get(2));
        response.getData().setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", response.getData());
        model.addAttribute("dataList", clientService.processFetchScheduledAirtime(response.getData().getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledairtime";
    }

    @PostMapping("/schedule/delete")
    public String deleteSchedule(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processDeleteScheduledAirtime(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/airtime/schedule";
    }

    @PostMapping(value = "/schedule/status")
    public String updateScheduleStatus(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processUpdateScheduledAirtimeStatus(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/airtime/schedule";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
