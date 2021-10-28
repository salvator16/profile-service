package com.sample.profileservice.service;

import com.sample.profileservice.model.dto.ImageModel;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;

import java.io.IOException;
import java.util.List;

public interface ProfileService {

    void download(List<ProfileModelRequestDto> targetListDto);

    byte[] getImage(String id, String socialNetwork) throws IOException;

    List<ImageModel> getAllImages() throws IOException;
}
