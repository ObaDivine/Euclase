package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import java.util.List;

/**
 *
 * @author briano
 */
public interface UserService {

    EuclaseResponsePayload authenticateUser(EuclasePayload requestPayload);

    String changePassword(EuclasePayload requestPayload, String principal);

    List<String> fetchRoleGroup();
}
