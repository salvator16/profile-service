package com.sample.profileservice.service;

import com.sample.profileservice.model.dto.ProfileModelRequestDto;

public interface SocialNetwork {

    String getProviderType();

    void downloadImage(ProfileModelRequestDto targetModel);

}
