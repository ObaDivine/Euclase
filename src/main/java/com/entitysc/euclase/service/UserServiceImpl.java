package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author briano
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Gson gson;

    @Override
    public EuclaseResponsePayload authenticateUser(EuclasePayload requestPayload) {
        try {
            return null;
        } catch (Exception ex) {
            EuclaseResponsePayload responsePayload = new EuclaseResponsePayload();
            responsePayload.setResponseCode("500");
            responsePayload.setResponseMessage(ex.getMessage());
            return responsePayload;
        }
    }

    @Override
    public String changePassword(EuclasePayload requestPayload, String principal) {
        try {
            return "";
        } catch (Exception ex) {
//            PylonResponsePayload responsePayload = new PylonResponsePayload();
//            responsePayload.setResponseCode("500");
//            responsePayload.setResponseMessage(ex.getMessage());
//            return responsePayload;
            return ex.getMessage();
        }
    }

    @Override
    public List<String> fetchRoleGroup() {
        try {
            return null;
        } catch (Exception ex) {
//            PylonResponsePayload responsePayload = new PylonResponsePayload();
//            responsePayload.setResponseCode("500");
//            responsePayload.setResponseMessage(ex.getMessage());
//            return responsePayload;
            return null;
        }
    }

}
