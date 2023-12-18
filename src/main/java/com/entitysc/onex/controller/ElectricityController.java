package com.entitysc.onex.controller;

import com.entitysc.onex.constant.ResponseCodes;
import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonPayload;
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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/electricity")
public class ElectricityController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/single")
    public String electricity(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "electricity";
    }

    @PostMapping("/single/")
    public String electricity(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processSingleElectricity(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.REQUEST_PROCESSING.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "info";
            return "redirect:/electricity/single";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "electricity";
    }

    @GetMapping(value = "/bulk")
    public String bulkElectricity(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "bulkelectricity";
    }

    @PostMapping("/bulk/")
    public String bulkCableTv(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processBulkElectricity(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/electricity/bulk";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "bulkelectricity";
            }
        }
    }

    @GetMapping("/token")
    public String token(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "electricitytoken";
    }

    @GetMapping(value = "/transaction")
    public String electricityTransaction(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processElectricityTransaction(onexPayload).getData());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "electricitytransaction";
    }

    @PostMapping("/transaction/")
    public String electricityTransaction(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processElectricityTransaction(requestPayload).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "electricitytransaction";
    }

    @GetMapping("/lookup/{disco}/{billType}/{meternumber}")
    @ResponseBody
    public PylonPayload meterNumberLookup(@PathVariable("disco") String disco, @PathVariable("billType") String billType, @PathVariable("meternumber") String meternumber, Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        //Fetch a list of the beneficiary records
        PylonResponsePayload pylonPayload = clientService.processMeterLookup(disco, billType, meternumber);
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("beneficiaryList", pylonPayload.getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return pylonPayload.getData();
    }

    @GetMapping("/schedule")
    public String scheduledElectricity(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
         OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("dataList", clientService.processFetchScheduledElectricity(onexPayload.getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledelectricity";
    }
    
    @PostMapping("/schedule/")
    public String scheduleData(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        //Check if the process is Create or Update
        PylonResponsePayload response;
        if (requestPayload.getId().equalsIgnoreCase("")) {
            response = clientService.processScheduledElectricity(requestPayload);
        } else {
            response = clientService.processUpdateScheduledElectricity(requestPayload);
        }

        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/electricity/schedule";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("dataList", clientService.processFetchScheduledElectricity(requestPayload.getUsername()).getData());
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "success");
                return "scheduledelectricity";
            }
        }
    }

    @GetMapping(value = "/schedule/edit/{id}")
    public String editSchedule(@PathVariable("id") String id, Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        PylonResponsePayload response = clientService.processFetchScheduledElectricityUsingId(id);
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        response.getData().setProfileImage(userDetails.get(0));
        response.getData().setFirstName(userDetails.get(1));
        response.getData().setLastName(userDetails.get(2));
        response.getData().setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", response.getData());
        model.addAttribute("dataList", clientService.processFetchScheduledElectricity(response.getData().getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledelectricity";
    }

    @PostMapping("/schedule/delete")
    public String deleteSchedule(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processDeleteScheduledElectricity(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/electricity/schedule";
    }

    @PostMapping(value = "/schedule/status")
    public String updateScheduleStatus(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processUpdateScheduledElectricityStatus(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/electricity/schedule";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
