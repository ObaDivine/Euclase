package com.entitysc.ibank.service;

import com.entitysc.ibank.payload.PylonPayload;

/**
 *
 * @author briano
 */
public interface GenericService {

    String encryptString(String textToEncrypt, String token);

    String callPylonAPI(String url, String requestBody, String token, String app);

    String generatePylonAPIToken();

    String generateRequestId();

    String generateRequestString(String token, PylonPayload requestPayload);
   
}
