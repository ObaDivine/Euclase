package com.entitysc.euclase.service;

import com.entitysc.euclase.constant.ResponseCodes;
import com.entitysc.euclase.payload.PylonPayload;
import com.entitysc.euclase.payload.PylonResponsePayload;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CronJob {

    @Autowired
    GenericService genericService;
    @Autowired
    Gson gson;
    @Value("${euclase.document.archivedir}")
    private String archiveDirectory;
    @Value("${pylon.api.document.archive}")
    private String processDocumentArchivingUrl;

//    @Scheduled(fixedDelay = 10000)
    public void archiveDocuments() {
        String token = genericService.generatePylonAPIToken();
        try {
            //Fetch all the files in the archive directory
            File[] files = new File(archiveDirectory).listFiles();
            if (files.length > 0) {
                //Loop through the files in the directory
                for (File f : files) {
                    //Check if it is a file
                    if (f.isFile()) {
                        //Push the file to Pylon to archive
                        PylonPayload pylonPayload = new PylonPayload();
                        pylonPayload.setChannel("WEB");
                        pylonPayload.setRequestBy("System");
                        pylonPayload.setRequestId(genericService.generateRequestId());
                        pylonPayload.setToken(token);
                        pylonPayload.setRequestType("DocumentArchive");
                        pylonPayload.setHash(genericService.generateRequestString(token, pylonPayload));
                        //Connect to Pylon API
                        List<MultipartFile> filesToArchive = new ArrayList<>();
                        //Conver the file to multipart file
                        MultipartFile fileMultipart = new MockMultipartFile(f.getName(), f.getName(), "text/plain", new FileInputStream(f));
                        filesToArchive.add(fileMultipart);
                        String response = genericService.callPylonAPI(processDocumentArchivingUrl, gson.toJson(pylonPayload), filesToArchive, token, "Document Archiving");
                        PylonResponsePayload responsePayload = gson.fromJson(response, PylonResponsePayload.class);
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
