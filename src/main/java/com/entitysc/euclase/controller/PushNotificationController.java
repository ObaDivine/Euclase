package com.entitysc.euclase.controller;

import com.entitysc.euclase.payload.EuclasePayload;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author briano
 */
@RestController
@CrossOrigin(origins = "*")
public class PushNotificationController {

    @Autowired
    Gson gson;
    String memberId = "";
    Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/events/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(HttpSession httpSession) {
        memberId = httpSession.getId();
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("INIT")); 
        } catch (IOException ex) {
            Logger.getLogger(PushNotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        sseEmitter.onCompletion(() -> removeSSEvent(memberId));
        addOrReplaceEmitter(memberId, sseEmitter);
        return sseEmitter;
    }

    public void pushNotification(EuclasePayload pylonPayload) {
        Optional<SseEmitter> emitter = getSSEvent(memberId);
        emitter.ifPresent(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("callback")
                        .comment("Push Notification")
                        .data(gson.toJson(pylonPayload)));
            } catch (IOException | IllegalStateException e) {
                removeSSEvent(memberId);
            }
        });
    }

    private Optional<SseEmitter> getSSEvent(String memberId) {
        return Optional.ofNullable(emitters.get(memberId));
    }

    private void addOrReplaceEmitter(String memberId, SseEmitter emitter) {
        emitters.put(memberId, emitter);
    }

    private void removeSSEvent(String memberId) {
        if (emitters != null && emitters.containsKey(memberId)) {
            emitters.remove(memberId);
        }
    }

//    @PostMapping(value = "/events/callback/notification")
//    public void pushNotificationByUrl(@RequestParam String callbackData) {
//        for (SseEmitter emitter : emitters) {
//            try {
//                emitter.send(SseEmitter.event().name("callback").data(callbackData));
//            } catch (IOException ex) {
//                removeSSEvent(memberId);
//            }
//        }
//    }
}
