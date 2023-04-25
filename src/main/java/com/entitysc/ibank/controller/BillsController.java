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
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/bills")
public class BillsController {

    @Autowired
    ClientService clientService;
    private static final Logger LOGGER = Logger.getLogger(FundsTransferController.class.getName());
    private String alertMessage = "";

    @GetMapping("/cabletv")
    public String cabletv(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        //Fetch a list of the beneficiary records
        PylonPayload pylonPayload = clientService.processTransferBeneficiary(principal.getName());
        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("beneficiaryList", pylonPayload.getDataList());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "cabletv";
    }

    @GetMapping("/cabletv/details")
    public String cabletvDetails(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        //Fetch a list of the beneficiary records
        PylonPayload pylonPayload = clientService.processTransferBeneficiary(principal.getName());
        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("beneficiaryList", pylonPayload.getDataList());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "cabletvdetails";
    }

    @GetMapping("/electricity")
    public String electricity(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "electricity";
    }

    @GetMapping("/electricity/token")
    public String token(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "electricitytoken";
    }

    @GetMapping("/electricity/details")
    public String electriciyDetails(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("electricityList", null);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "electricitydetails";
    }

    @GetMapping("/bulk")
    public String bulkBillsPayment(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("electricityList", null);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "bulkbills";
    }

    @GetMapping("/scheduled")
    public String scheduledBills(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("electricityList", null);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "scheduledbills";
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
