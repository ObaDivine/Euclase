package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PasswordChangePayload;
import com.entitysc.ibank.payload.PylonPayload;
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
    public PylonPayload authenticateUser(IBankPayload requestPayload) {
        PylonPayload ibankResponsePayload = new PylonPayload();
        PylonPayload ibankRequestPayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            BeanUtils.copyProperties(requestPayload, ibankRequestPayload);
            ibankRequestPayload.setRequestId(genericService.generateRequestId());
            ibankRequestPayload.setToken(token);
            ibankRequestPayload.setRequestType("Login");
            ibankRequestPayload.setUserType("Admin");
            ibankRequestPayload.setHash(genericService.generateRequestString(token, ibankRequestPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI("/user/auth", gson.toJson(ibankRequestPayload), token, "Login");
            ibankResponsePayload = gson.fromJson(response, PylonPayload.class);
            return ibankResponsePayload;
        } catch (Exception ex) {
            ibankResponsePayload.setResponseCode("500");
            ibankResponsePayload.setResponseMessage(ex.getMessage());
            return ibankResponsePayload;
        }
    }

    @Override
    public PylonPayload changePassword(PasswordChangePayload requestPayload, String principal) {
        PylonPayload ibankResponsePayload = new PylonPayload();
        PylonPayload ibankRequestPayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            return null;
        } catch (Exception ex) {
            ibankResponsePayload.setResponseCode("500");
            ibankResponsePayload.setResponseMessage(ex.getMessage());
            return ibankResponsePayload;
        }
    }

    @Override
    public List<String> fetchRoleGroup() {
        PylonPayload ibankResponsePayload = new PylonPayload();
        PylonPayload ibankRequestPayload = new PylonPayload();
        String token = genericService.generatePylonAPIToken();
        try {
            ibankRequestPayload.setRequestId(genericService.generateRequestId());
            ibankRequestPayload.setToken(token);
            ibankRequestPayload.setRequestType("Roles");
            ibankRequestPayload.setUserType("Admin");
            ibankRequestPayload.setHash(genericService.generateRequestString(token, ibankRequestPayload));
            //Connect to ibank API
            String response = genericService.callPylonAPI("/admin/role/group/list", gson.toJson(ibankRequestPayload), token, "Login");
            ibankResponsePayload = gson.fromJson(response, PylonPayload.class);
            return (List) ibankResponsePayload.getDataList();
        } catch (Exception ex) {
            ibankResponsePayload.setResponseCode("500");
            ibankResponsePayload.setResponseMessage(ex.getMessage());
            return null;
        }
    }

}
