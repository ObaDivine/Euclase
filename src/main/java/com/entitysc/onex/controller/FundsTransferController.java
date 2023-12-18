package com.entitysc.onex.controller;

import com.entitysc.onex.constant.ResponseCodes;
import com.entitysc.onex.payload.DataListResponsePayload;
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
@RequestMapping("/funds-transfer")
public class FundsTransferController {

    @Autowired
    ClientService clientService;
    private String alertMessage = "";
    private String alertMessageType = "";

    @GetMapping("/beneficiary")
    public String beneficiary(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));

        //Fetch a list of the beneficiary records
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(onexPayload.getUsername()).getData());
        model.addAttribute("bankList", clientService.processFetchBankList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "ftbeneficiary";
    }

    @PostMapping("/beneficiary/add")
    public String addTransferBeneficiary(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model, HttpSession httpSession, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processAddTransferBeneficiary(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/funds-transfer/beneficiary";
        }

        //Fetch a list of the beneficiary records
        DataListResponsePayload pylonPayload = clientService.processUserTransferBeneficiary(requestPayload.getUsername());
        model.addAttribute("beneficiaryList", pylonPayload.getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "ftbeneficiary";
    }

    @PostMapping("/beneficiary/delete")
    public String deleteBeneficiary(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model, HttpSession httpSession, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));

        PylonResponsePayload response = clientService.processDeleteTransferBeneficiary(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/funds-transfer/beneficiary";
    }

    @GetMapping("/single")
    public String singleFundsTransfer(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setWalletId(userDetails.get(6));
        onexPayload.setUsername(userDetails.get(8));

        //Fetch a list of the beneficiary records
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(onexPayload.getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "fundstransfer";
    }

    @PostMapping("/single/")
    public String singleFundsTransfer(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model, HttpSession httpSession, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setWalletId(userDetails.get(6));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processSingleFundsTransfer(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.REQUEST_PROCESSING.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/funds-transfer/single";
        }

        //Fetch a list of the beneficiary records
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(requestPayload.getUsername()).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "fundstransfer";
    }

    @GetMapping("/bulk")
    public String bulkFundsTransfer(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setWalletId(userDetails.get(6));
        onexPayload.setUsername(userDetails.get(8));

        //Fetch a list of the beneficiary records
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(onexPayload.getUsername()).getData());
        model.addAttribute("bankList", clientService.processFetchBankList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "bulkfundstransfer";
    }

    @PostMapping("/bulk/")
    public String bulkFundsTransfer(@ModelAttribute("onexPayload") OneXPayload requestPayload, Model model, HttpSession httpSession, Principal principal) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setWalletId(userDetails.get(6));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processBulkFundsTransfer(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.REQUEST_PROCESSING.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "info";
            return "redirect:/funds-transfer/single";
        }

        //Fetch a list of the beneficiary records
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(requestPayload.getUsername()));
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "bulkfundstransfer";
    }

    @GetMapping(value = "/transaction")
    public String fundsTransferTransaction(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processFundsTransferTransaction(onexPayload).getData());
        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "fundstransfertransaction";
    }

    @PostMapping("/transaction/")
    public String fundsTransferTransaction(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        model.addAttribute("dataList", clientService.processFundsTransferTransaction(requestPayload).getData());
        model.addAttribute("onexPayload", requestPayload);
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "error");
        resetAlertMessage();
        return "fundstransfertransaction";
    }

    @GetMapping("/schedule")
    public String scheduledFundsTransfer(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        OneXPayload onexPayload = new OneXPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        onexPayload.setProfileImage(userDetails.get(0));
        onexPayload.setFirstName(userDetails.get(1));
        onexPayload.setLastName(userDetails.get(2));
        onexPayload.setWalletId(userDetails.get(6));
        onexPayload.setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", onexPayload);
        model.addAttribute("dataList", clientService.processFetchScheduledFundsTransfer(onexPayload.getUsername()).getData());
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(onexPayload.getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledfundstransfer";
    }

    @PostMapping("/schedule/")
    public String scheduleFundsTransfer(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setWalletId(userDetails.get(6));
        requestPayload.setUsername(userDetails.get(8));
        //Check if the process is Create or Update
        PylonResponsePayload response;
        if (requestPayload.getId().equalsIgnoreCase("")) {
            response = clientService.processScheduledFundsTransfer(requestPayload);
        } else {
            response = clientService.processUpdateScheduledFundsTransfer(requestPayload);
        }

        switch (response.getResponseCode()) {
            case "00": {
                alertMessage = response.getResponseMessage();
                alertMessageType = "success";
                return "redirect:/funds-transfer/schedule";
            }
            default: {
                model.addAttribute("onexPayload", requestPayload);
                model.addAttribute("dataList", clientService.processFetchScheduledFundsTransfer(requestPayload.getUsername()).getData());
                model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(requestPayload.getUsername()).getData());
                model.addAttribute("alertMessage", response.getResponseMessage());
                model.addAttribute("alertMessageType", "success");
                return "scheduledfundstransfer";
            }
        }
    }

    @GetMapping(value = "/schedule/edit/{id}")
    public String editSchedule(@PathVariable("id") String id, Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        PylonResponsePayload response = clientService.processFetchScheduledFundsTransferUsingId(id);
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        response.getData().setProfileImage(userDetails.get(0));
        response.getData().setFirstName(userDetails.get(1));
        response.getData().setLastName(userDetails.get(2));
        response.getData().setUsername(userDetails.get(8));

        model.addAttribute("onexPayload", response.getData());
        model.addAttribute("dataList", clientService.processFetchScheduledFundsTransfer(response.getData().getUsername()).getData());
        model.addAttribute("beneficiaryList", clientService.processUserTransferBeneficiary(response.getData().getUsername()).getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", "success");
        resetAlertMessage();
        return "scheduledfundstransfer";
    }

    @PostMapping("/schedule/delete")
    public String deleteSchedule(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processDeleteScheduledFundsTransfer(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/funds-transfer/schedule";
    }

    @PostMapping(value = "/schedule/status")
    public String updateScheduleStatus(@ModelAttribute("onexPayload") OneXPayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        List<String> userDetails = (List<String>) httpSession.getAttribute("ONEX_SESSION_DETAILS");
        requestPayload.setProfileImage(userDetails.get(0));
        requestPayload.setFirstName(userDetails.get(1));
        requestPayload.setLastName(userDetails.get(2));
        requestPayload.setUsername(userDetails.get(8));
        PylonResponsePayload response = clientService.processUpdateScheduledFundsTransferStatus(requestPayload);
        alertMessage = response.getResponseMessage();
        alertMessageType = "success";
        return "redirect:/funds-transfer/schedule";
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
