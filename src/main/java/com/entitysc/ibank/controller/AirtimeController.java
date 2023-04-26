package com.entitysc.ibank.controller;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PylonPayload;
import com.entitysc.ibank.service.ClientService;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;
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
@RequestMapping("/airtime")
public class AirtimeController {

    @Autowired
    ClientService clientService;
    private static final Logger LOGGER = Logger.getLogger(FundsTransferController.class.getName());
    private String alertMessage = "";

    @GetMapping("/")
    public String airtime(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "airtime";
    }

    @PostMapping(value = "/pay")
    public String airtime(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processSingleAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/airtime/";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "airtime";
            }
        }
    }

    @GetMapping(value = "/data")
    public String data(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "data";
    }

    @PostMapping(value = "/data/pay")
    public String data(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processSingleData(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/airtime/data/";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "data";
            }
        }
    }

    @GetMapping(value = "/details")
    public String details(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "airtimehistory";
    }

    @PostMapping(value = "/details/")
    public String details(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processAirtimeHistory(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("dataList", response.getDataList());
                model.addAttribute("alertMessage", alertMessage);
                resetAlertMessage();
                return "airtimehistory";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "airtimehistory";
            }
        }
    }

    @GetMapping(value = "/bulk")
    public String bulk(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "bulkairtime";
    }

    @PostMapping(value = "/bulk/pay")
    public String bulkAirtime(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processBulkAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/airtime/bulk";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "bulkairtime";
            }
        }
    }

    @GetMapping(value = "/scheduled")
    public String scheduled(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "scheduledairtime";
    }

    @PostMapping(value = "/scheduled/pay")
    public String scheduleAirtime(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processScheduledAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/airtime/scheduled";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "scheduledairtime";
            }
        }
    }

    @GetMapping(value = "/data/bulk")
    public String bulkData(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "bulkdata";
    }

    @PostMapping(value = "/data/bulk/pay")
    public String bulkData(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processBulkAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/data/bulk";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "bulkdata";
            }
        }
    }

    @GetMapping(value = "/data/scheduled")
    public String scheduledData(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "scheduleddata";
    }

    @PostMapping(value = "/data/scheduled/pay")
    public String scheduleData(@ModelAttribute("ibankPayload") IBankPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setSalutation(userDetails.get(3));
        requestPayload.setUsername(principal.getName());
        PylonPayload response = clientService.processScheduledAirtime(requestPayload);
        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                return "redirect:/data/scheduled";
            }
            default: {
                model.addAttribute("ibankPayload", requestPayload);
                model.addAttribute("alertMessage", response.getResponseMessage());
                return "scheduleddata";
            }
        }
    }

    @GetMapping(value = "/data/plan/{telco}")
    @ResponseBody
    public List<PylonPayload> dataPlan(@PathVariable("telco") String telco, Model model, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));
        PylonPayload response = clientService.processFetchDataPlan(telco);
        return (List<PylonPayload>) response.getDataList();
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
