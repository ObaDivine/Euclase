package com.entitysc.euclase.fe.service;

import com.entitysc.euclase.fe.payload.DataListResponsePayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author bokon
 */
@Service
public class CronJob extends EuclaseService {

    @Autowired
    PushNotificationService pushNotificationService;

    @Scheduled(fixedDelay = 30000) //This is every 30 seconds
    public void fetchUserNotifications() {
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra != null) {
                HttpServletRequest request = sra.getRequest();
                HttpSession httpSession = request.getSession(false);
                if (httpSession != null) {
                    String username = (String) httpSession.getAttribute("username");
                    //Fetch the user's notification
                    DataListResponsePayload responsePayload = pushNotificationService.fetchUserPushNotification(username);
                    httpSession.setAttribute("notification", responsePayload.getData());
                    httpSession.setAttribute("unreadMessageCount", responsePayload.getData() == null ? 0
                            : responsePayload.getData().stream().filter(t -> !t.isMessageRead()).count());
                }
            }
        } catch (Exception ex) {
            logger.info("Error retrieving user notification - " + ex.getMessage());
        }
    }
}
