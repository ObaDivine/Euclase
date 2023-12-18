package com.entitysc.onex.controller;

import com.entitysc.onex.constant.ResponseCodes;
import com.entitysc.onex.payload.DataListResponsePayload;
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
@RequestMapping("/data")
public class DataController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping(value = "/single")
    public String data(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "data";
    }

    @PostMapping("/single/")
    public String data(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processSingleData(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.REQUEST_PROCESSING.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "info";
            return "redirect:/data/single";
        }
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "data";
    }

    @GetMapping(value = "/bulk")
    public String bulkData(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "bulkdata";
    }

    @PostMapping("/bulk/")
    public String bulkData(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processBulkData(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/data/bulk";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "error");
                return "bulkdata";
            }
        }
    }

    @GetMapping(value = "/schedule")
    public String scheduledData(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("dataList", clientService.processFetchScheduledData(onexPayload.getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduleddata";
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
            response = clientService.processScheduledData(requestPayload);
        } else {
            response = clientService.processUpdateScheduledData(requestPayload);
        }

        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/data/schedule";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("dataList", clientService.processFetchScheduledData(requestPayload.getUsername()).getData());
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "success");
                return "scheduleddata";
            }
        }
    }

    @GetMapping(value = "/schedule/edit/{id}")
    public String editSchedule(@PathVariable("id") String id, Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        PylonResponsePayload response = clientService.processFetchScheduledDataUsingId(id);
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        response.getData().setProfileImage(userDetails.get(0));
        response.getData().setFirstName(userDetails.get(1));
        response.getData().setLastName(userDetails.get(2));
        response.getData().setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", response.getData());
        model.addAttribute("dataList", clientService.processFetchScheduledData(response.getData().getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduleddata";
    }

    @PostMapping("/schedule/delete")
    public String deleteSchedule(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processDeleteScheduledData(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/data/schedule";
    }

    @PostMapping(value = "/schedule/status")
    public String updateScheduleStatus(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processUpdateScheduledDataStatus(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/data/schedule";
    }

    @GetMapping(value = "/transaction")
    public String dataTransaction(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processDataTransaction(onexPayload).getData());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "datatransaction";
    }

    @PostMapping("/transaction/")
    public String dataTransaction(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processDataTransaction(requestPayload).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "datatransaction";
    }

    @GetMapping(value = "/plan/{telco}")
    @ResponseBody
    public List<PylonPayload> dataPlan(@PathVariable("telco") String telco, Model model, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        DataListResponsePayload response = clientService.processFetchDataPlan(telco);
        return response.getData();
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
