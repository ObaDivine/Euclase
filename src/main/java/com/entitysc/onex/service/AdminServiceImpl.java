package com.entitysc.onex.service;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author briano
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    GenericService genericService;
    @Autowired
    Gson gson;

    @Override
    public PylonResponsePayload authenticateUser(OneXPayload requestPayload) {
        PylonPayload onexResponsePayload = new PylonPayload();
        PylonPayload onexRequestPayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            BeanUtils.copyProperties(requestPayload, onexRequestPayload);
            onexRequestPayload.setRequestId(genericService.generateRequestId());
            onexRequestPayload.setToken(token);
            onexRequestPayload.setRequestType("Login");
            onexRequestPayload.setUserType("Admin");
            onexRequestPayload.setHash(genericService.generateRequestString(token, onexRequestPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI("/user/auth", gson.toJson(onexRequestPayload), token, "Login");
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload = gson.fromJson(response, PylonResponsePayload.class);
            return responsePayload;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public PylonResponsePayload changePassword(OneXPayload requestPayload, String principal) {
        PylonPayload onexResponsePayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            return null;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public List<String> fetchRoleGroup() {
        PylonPayload onexResponsePayload = new PylonPayload();
        PylonPayload onexRequestPayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            onexRequestPayload.setRequestId(genericService.generateRequestId());
            onexRequestPayload.setToken(token);
            onexRequestPayload.setRequestType("Roles");
            onexRequestPayload.setUserType("Admin");
            onexRequestPayload.setHash(genericService.generateRequestString(token, onexRequestPayload));
            //Connect to onex API
            String response = genericService.callPylonAPI("/admin/role/group/list", gson.toJson(onexRequestPayload), token, "Login");
            onexResponsePayload = gson.fromJson(response, PylonPayload.class);
//            return (List) onexResponsePayload.getData();
            return null;
        } catch (Exception ex) {
            PylonResponsePayload responsePayload = new PylonResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return null;
        }
    }

}
