package com.sample.profileservice.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "twitter-client",
        url = "#{'${client.twitter.url}'}"
)
public interface TwitterClient {

    @GetMapping("/{screenName}/picture")
    Response getProfilePicture(@PathVariable("id") String screenName);

}
