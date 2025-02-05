package com.backend.allreva.firebase.infra;

import com.backend.allreva.firebase.dto.FcmMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "fcmClient", url = "https://fcm.googleapis.com/v1")
public interface FcmClient {

    @PostMapping("/projects/{projectId}/messages:send")
    String sendMessage(
            @RequestHeader("Authorization") final String authorization,
            @RequestBody final FcmMessage message,
            @PathVariable("projectId") final String projectId
    );
}
