package com.sample.profileservice.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "facebookClient",
        url = "#{'${client.facebook.url}'}"
)
public interface FacebookClient {

    @GetMapping("/{id}/picture")
    Response getProfilePicture(@PathVariable("id") String id);

}
