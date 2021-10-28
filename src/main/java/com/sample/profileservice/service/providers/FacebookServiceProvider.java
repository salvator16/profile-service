package com.sample.profileservice.service.providers;

import com.sample.profileservice.client.FacebookClient;
import com.sample.profileservice.exception.ImageNotFoundException;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;
import com.sample.profileservice.model.enums.SocialNetworkType;
import com.sample.profileservice.service.SocialNetwork;
import com.sample.profileservice.utils.FileUtils;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class FacebookServiceProvider implements SocialNetwork {

    private final FacebookClient facebookClient;

    @Override
    public String getProviderType() {
        return SocialNetworkType.FACEBOOK.name();
    }

    @Override
    public void downloadImage(ProfileModelRequestDto profileModelRequestDto) {

        File file = new File(FileUtils.generateFullPath(profileModelRequestDto.getNetwork(),
                profileModelRequestDto.getId()));

        if (FileUtils.isFileExists(file)){
            log.info("Image already exists for given id {}", profileModelRequestDto.getId());
            return;
        }
        try {

            Response responseFacebook = facebookClient.getProfilePicture(profileModelRequestDto.getId());

            if (responseFacebook.status() != HttpStatus.OK.value())
                throw new ImageNotFoundException("Image not found or Facebook client is not available");

            file.createNewFile();

            final byte[] byteArray = responseFacebook.body().asInputStream().readAllBytes();

            FileOutputStream fs = new FileOutputStream(file, false);

            fs.write(byteArray);
            fs.close();
        } catch (IOException e) {
            log.error("File io exception thrown for given id {} ", profileModelRequestDto.getId());
            throw new RuntimeException("File IO exception thrown, file not saved ", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

