package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.IBankPayload;
import com.entitysc.ibank.payload.PasswordChangePayload;
import com.entitysc.ibank.payload.PylonPayload;
import java.util.List;

/**
 *
 * @author briano
 */
public interface AdminService {

    PylonPayload authenticateUser(IBankPayload requestPayload);

    PylonPayload changePassword(PasswordChangePayload requestPayload, String principal);

    List<String> fetchRoleGroup();
}
