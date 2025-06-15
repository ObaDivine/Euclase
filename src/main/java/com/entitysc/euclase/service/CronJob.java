package com.entitysc.euclase.service;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.EuclasePayload;
import com.entitysc.euclase.payload.EuclaseResponsePayload;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author bokon
 */
@Service
public class CronJob extends EuclaseService{

    @Value("${euclase.document.archivedir}")
    private String archiveDirectory;
    @Value("${euclasews.api.document.archive}")
    private String processDocumentArchivingUrl;

    @Scheduled(fixedDelay = 10000)
    public void archiveDocuments() {
        String token = generateEuclaseWSAPIToken();
        try {
            //Fetch all the files in the archive directory
            File[] files = new File(archiveDirectory).listFiles();
            if (files.length > 0) {
                //Loop through the files in the directory
                for (File f : files) {
                    //Check if it is a file
                    if (f.isFile()) {
                        //Push the file to EuclaseWS to archive
                        EuclasePayload EuclasePayload = new EuclasePayload();
                        EuclasePayload.setChannel("WEB");
                        EuclasePayload.setRequestBy("System");
                        EuclasePayload.setRequestId(generateRequestId());
                        EuclasePayload.setToken(token);
                        EuclasePayload.setRequestType("DocumentArchive");
                        EuclasePayload.setHash(generateRequestString(token, EuclasePayload));
                        //Connect to EuclaseWS API
                        List<MultipartFile> filesToArchive = new ArrayList<>();
                        //Conver the file to multipart file
                        MultipartFile fileMultipart = new MockMultipartFile(f.getName(), f.getName(), "text/plain", new FileInputStream(f));
                        filesToArchive.add(fileMultipart);
                        String response = callEuclaseWSAPI(processDocumentArchivingUrl, gson.toJson(EuclasePayload), filesToArchive, token, "Document Archiving");
                        EuclaseResponsePayload responsePayload = gson.fromJson(response, EuclaseResponsePayload.class);
                        if (responsePayload.getResponseCode().equalsIgnoreCase(ResponseCodes.SUCCESS_CODE.getResponseCode())) {
                            //Operation is successful. Delete the file from the directory
                            f.delete();
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
    }
}
