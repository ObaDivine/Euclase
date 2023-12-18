package com.entitysc.onex.service;

import com.entitysc.onex.payload.PylonPayload;

/**
 *
 * @author briano
 */
public interface GenericService {

    String encryptString(String textToEncrypt);

    String urlEncodeString(String urlToEncrypt);

    String callPylonAPI(String url, String requestBody, String token, String app);

    String generatePylonAPIToken();

    String generateRequestId();

    String generateRequestString(String token, PylonPayload requestPayload);

    String formatDate(String dateToFormat, String currentFormat);
}
