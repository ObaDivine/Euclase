package com.entitysc.euclase.controller;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.entitysc.euclase.service.ChannelService;
import com.entitysc.euclase.service.RolesService;
import com.entitysc.euclase.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    ChannelService channelService;
    @Autowired
    UserService userService;
    @Autowired
    RolesService roleService;
    @Value("${euclase.client.name}")
    private String companyName;
    @Value("${euclase.client.url}")
    private String companyUrl;
    private String alertMessage = "";
    private String alertMessageType = "";

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        model.addAttribute("companyName", companyName);
        model.addAttribute("companyUrl", companyUrl);
    }

    @GetMapping("/")
    @Secured("ROLE_MANAGE_CHANNEL")
    public String channel(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("roleList", roleService.fetchRoleList().getData());
        model.addAttribute("ipList", channelService.fetchIPAddressList().getData());
        model.addAttribute("apiBandList", channelService.fetchApiBandList().getData());
        model.addAttribute("channelCount", channelService.fetchChannelList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "channel";
    }

    @PostMapping("/process")
    public String channel(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setPrincipal(principal.getName());
        EuclaseResponsePayload response = channelService.processCreateChannel(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("roleList", roleService.fetchRoleList().getData());
        model.addAttribute("ipList", channelService.fetchIPAddressList().getData());
        model.addAttribute("apiBandList", channelService.fetchApiBandList().getData());
        model.addAttribute("channelCount", channelService.fetchChannelList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "channel";
    }

    @GetMapping(value = "/list")
    @Secured("ROLE_MANAGE_CHANNEL")
    public String channelList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", channelService.fetchChannelList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "channellist";
    }

    @GetMapping("/edit")
    @Secured("ROLE_MANAGE_CHANNEL")
    public String editChannel(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = channelService.fetchChannel(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("roleList", roleService.fetchRoleList().getData());
        model.addAttribute("ipList", channelService.fetchIPAddressList().getData());
        model.addAttribute("apiBandList", channelService.fetchApiBandList().getData());
        model.addAttribute("channelCount", channelService.fetchChannelList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "channel";
    }

    @GetMapping("/delete")
    @Secured("ROLE_MANAGE_CHANNEL")
    public String deleteChannel(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = channelService.processDeleteChannel(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/channel/list";
    }

    @GetMapping("/details")
    @Secured("ROLE_MANAGE_CHANNEL")
    public String channelDetails(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = channelService.fetchChannel(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        model.addAttribute("transType", "role");
        resetAlertMessage();
        return "channeldetails";
    }

    @GetMapping("/ip")
    @Secured("ROLE_MANAGE_IP_ADDRESS")
    public String ipAddress(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("ipCount", channelService.fetchIPAddressList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "ipaddress";
    }

    @PostMapping("/ip/")
    public String ipAddress(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = channelService.processCreateIPAddress(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/ip";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("ipCount", channelService.fetchIPAddressList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "ipaddress";
    }

    @GetMapping(value = "/ip/list")
    @Secured("ROLE_MANAGE_IP_ADDRESS")
    public String ipAddressList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", channelService.fetchIPAddressList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "ipaddresslist";
    }

    @GetMapping("/ip/edit")
    @Secured("ROLE_MANAGE_IP_ADDRESS")
    public String editIpAddress(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = channelService.fetchIPAddress(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/ip/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("userList", userService.fetchAppUserList().getData());
        model.addAttribute("ipCount", channelService.fetchIPAddressList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "ipaddress";
    }

    @GetMapping("/ip/delete")
    @Secured("ROLE_MANAGE_IP_ADDRESS")
    public String deleteIpAddress(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = channelService.processDeleteIPAddress(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/channel/ip/list";
    }

    @GetMapping("/api/band")
    @Secured("ROLE_MANAGE_BILLING")
    public String apiBand(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("apiCount", channelService.fetchApiBandList().getData().size());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "apiband";
    }

    @PostMapping("/api/band/")
    public String apiBand(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = channelService.processCreateApiBand(requestPayload);
        if (response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/api/band";
        }
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("apiCount", channelService.fetchApiBandList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", "error");
        return "apiband";
    }

    @GetMapping(value = "/api/band/list")
    @Secured("ROLE_MANAGE_BILLING")
    public String apiBandList(Model model, HttpServletRequest httpRequest, HttpServletResponse httpResponse, HttpSession httpSession, Principal principal) {
        model.addAttribute("dataList", channelService.fetchApiBandList().getData());
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "apibandlist";
    }

    @GetMapping("/api/band/edit")
    @Secured("ROLE_MANAGE_BILLING")
    public String editIpApiBand(@RequestParam("seid") String seid, Model model, Principal principal, HttpServletRequest httpRequest) {
        EuclaseResponsePayload response = channelService.fetchApiBand(seid);
        if (!response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
            alertMessage = response.getResponseMessage();
            alertMessageType = "success";
            return "redirect:/channel/api/band/list";
        }
        model.addAttribute("euclasePayload", response.getData());
        model.addAttribute("apiCount", channelService.fetchApiBandList().getData().size());
        model.addAttribute("alertMessage", response.getResponseMessage());
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "apiband";
    }

    @GetMapping("/api/band/delete")
    @Secured("ROLE_MANAGE_BILLING")
    public String deleteApiBand(@RequestParam("seid") String seid, Model model, Principal principal) {
        EuclaseResponsePayload response = channelService.processDeleteApiBand(seid, principal.getName());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/channel/api/band/list";
    }

    @GetMapping("/top-up")
    @Secured("ROLE_MANAGE_BILLING")
    public String balanceTopUp(Model model, HttpServletRequest request, HttpServletResponse response, Principal principal, HttpSession session) {
        model.addAttribute("euclasePayload", new EuclasePayload());
        model.addAttribute("channelList", channelService.fetchChannelList().getData());
        model.addAttribute("alertMessage", alertMessage);
        model.addAttribute("alertMessageType", alertMessageType);
        resetAlertMessage();
        return "channeltopup";
    }

    @PostMapping("/top-up/")
    public String balanceTopUp(@ModelAttribute("euclasePayload") EuclasePayload requestPayload, HttpSession httpSession, Principal principal, Model model) {
        requestPayload.setUsername(principal.getName());
        EuclaseResponsePayload response = channelService.processBalanceTopUp(requestPayload);
        model.addAttribute("euclasePayload", requestPayload);
        model.addAttribute("channelList", channelService.fetchChannelList().getData().size());
        alertMessage = response.getResponseMessage();
        alertMessageType = response.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode()) ? "success" : "error";
        return "redirect:/channel/top-up";
    }

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
