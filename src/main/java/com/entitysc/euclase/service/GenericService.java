package com.entitysc.euclase.service;

import com.entitysc.euclase.payload.PylonPayload;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author briano
 */
public interface GenericService {

    String generatePylonAPIToken();

    String generateRequestString(String token, PylonPayload requestPayload);

    String encryptString(String textToEncrypt);
    
    String decryptString(String textToDecrypt, String encryptionKey);

    String urlEncodeString(String urlToEncrypt);

    String formatDate(String dateToFormat, String currentFormat);

    String generateRequestId();

    String callPylonAPI(String url, String requestBody, String token, String app);

    String callPylonAPI(String url, String requestJson, List<MultipartFile> uploadedFiles, String token, String app);
}
