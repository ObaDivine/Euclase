package com.entitysc.onex.service;

import com.entitysc.onex.payload.OneXPayload;
import com.entitysc.onex.payload.PylonResponsePayload;
import java.util.List;

/**
 *
 * @author briano
 */
public interface AdminService {

    PylonResponsePayload authenticateUser(OneXPayload requestPayload);

    PylonResponsePayload changePassword(OneXPayload requestPayload, String principal);

    List<String> fetchRoleGroup();
}
