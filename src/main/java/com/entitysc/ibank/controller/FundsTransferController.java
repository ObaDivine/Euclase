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
@RequestMapping("/funds-transfer")
public class FundsTransferController {

    @Autowired
    ClientService clientService;
    private static final Logger LOGGER = Logger.getLogger(FundsTransferController.class.getName());
    private String alertMessage = "";

    @GetMapping("/beneficiary")
    public String beneficiary(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "ftbeneficiary";
    }

    @GetMapping("/beneficiary/add")
    public String addBeneficiary(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "addbeneficiary";
    }

    @GetMapping("/beneficiary/remove")
    public String removeBeneficiary(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
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
        return "removebeneficiary";
    }

    @GetMapping("/intra")
    public String intraFundsTransfer(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        //Fetch a list of the beneficiary records
        PylonPayload pylonPayload = clientService.processTransferBeneficiary(principal.getName());
        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("ftPayload", new IBankPayload());
        model.addAttribute("beneficiaryList", pylonPayload.getDataList());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "intrafundstransfer";
    }

    @GetMapping("/inter")
    public String interFundsTransfer(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        //Fetch a list of the beneficiary records
        PylonPayload pylonPayload = clientService.processTransferBeneficiary(principal.getName());
        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("ftPayload", new IBankPayload());
        model.addAttribute("beneficiaryList", pylonPayload.getDataList());
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "interfundstransfer";
    }

    @GetMapping("/details")
    public String fundsTransferDetails(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        IBankPayload ibankPayload = new IBankPayload();
        List<String> userDetails = (List<String>) httpSession.getAttribute("IBANK_SESSION_DETAILS");
        ibankPayload.setProfileImage(userDetails.get(0));
        ibankPayload.setFirstName(userDetails.get(1));
        ibankPayload.setLastName(userDetails.get(2));
        ibankPayload.setSalutation(userDetails.get(3));

        model.addAttribute("ibankPayload", ibankPayload);
        model.addAttribute("alertMessage", alertMessage);
        resetAlertMessage();
        return "fundstransferdetails";
    }

    private void resetAlertMessage() {
        alertMessage = "";
    }
}
