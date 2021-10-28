package com.sample.profileservice.service.providers;

import com.sample.profileservice.client.TwitterClient;
import com.sample.profileservice.exception.ImageNotFoundException;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;
import com.sample.profileservice.model.enums.SocialNetworkType;
import com.sample.profileservice.service.SocialNetwork;
import com.sample.profileservice.utils.FileUtils;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class TwitterServiceProvider implements SocialNetwork {

    private final TwitterClient twitterClient;

    @Override
    public String getProviderType() {
        return SocialNetworkType.TWITTER.name();
    }

    @Override
    public void downloadImage(ProfileModelRequestDto profileModelRequestDto) {

        File file = new File(FileUtils.generateFullPath(profileModelRequestDto.getNetwork() ,
                profileModelRequestDto.getId()));

        if (FileUtils.isFileExists(file)) {
            log.info("Image already exists for given id {}", profileModelRequestDto.getId());
            return;
        }

        try {
            Response responseTwitter = twitterClient.getProfilePicture(profileModelRequestDto.getId());

            if (responseTwitter.status() != HttpStatus.OK.value())
                throw new ImageNotFoundException("Image not found or Twitter client is not available");

            file.createNewFile();

            FileOutputStream fs = new FileOutputStream(file, false);

            byte[] buffer = new byte[4096];
            int rc = responseTwitter.body().asInputStream().read(buffer);

            while (rc != -1) {
                fs.write(rc);
                rc = responseTwitter.body().asInputStream().read(buffer);
            }

            fs.close();

        } catch (IOException e) {
            log.error("File io exception thrown for given id {} ", profileModelRequestDto.getId());
            throw new RuntimeException("File IO exception thrown, file not saved ", e);
        }

    }
}
