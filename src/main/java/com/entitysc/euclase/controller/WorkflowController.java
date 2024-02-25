package com.entitysc.euclase.controller;

import com.entitysc.euclase.service.EuclaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author briano
 */
@Controller
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    EuclaseService euclaseService;
    private String alertMessage = "";
    private String alertMessageType = "";

    private void resetAlertMessage() {
        alertMessage = "";
        alertMessageType = "";
    }
}
