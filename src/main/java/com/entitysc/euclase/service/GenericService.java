package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.PylonPayload;

/**
 *
 * @author briano
 */
public interface GenericService {

    String generatePylonAPIToken();

    String generateRequestString(String token, PylonPayload requestPayload);

    String encryptString(String textToEncrypt);

    String urlEncodeString(String urlToEncrypt);

    String formatDate(String dateToFormat, String currentFormat);

    String generateRequestId();

    String callPylonAPI(String url, String requestBody, String token, String app);
}
