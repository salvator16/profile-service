package com.sample.profileservice;

import com.sample.profileservice.exception.ImageNotFoundException;
import com.sample.profileservice.model.dto.ImageModel;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;
import com.sample.profileservice.service.ProfileService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class ProfileServiceApplicationTests extends AbstractRestTest{

    private static final String FACEBOOK = "facebook";
    private static final String TWITTER = "twitter";

    public static String PROFILE_GET_ALL_IMAGE_URI = "/api/profilePics/all-images";

    ResultMatcher statusOk = MockMvcResultMatchers.status().isOk();

    @Autowired
    private ProfileService profileService;

    @Test
    public void whenFeignClientIsActiveForFacebook() {
        profileService.download(Collections.singletonList(createTargetRequestDto("123123", FACEBOOK)));
    }

    @Test
    public void whenGetImage_thenByteArrayShouldBeReturnedForFacebook() throws IOException {
        byte[] testArray = profileService.getImage("123123", FACEBOOK);
        Assert.assertTrue(!Objects.isNull(testArray));
    }

    @Test
    public void whenProfileResourceGetAllImage_thenResponseMustBeBiggerThanTwo() throws Exception {

        List<ImageModel> modelList = (List<ImageModel>) doGetAndReturnList(PROFILE_GET_ALL_IMAGE_URI, statusOk, ImageModel.class);
        Assert.assertTrue( modelList.size() >= 2);
    }


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void whenDownloadFacebookImage_thenRuleIsApplied() {
        exception.expect(ImageNotFoundException.class);
        exception.expectMessage("Image not found or Facebook client is not available");
        profileService.download(Collections.singletonList(createTargetRequestDto("121233123", FACEBOOK)));
    }

    private ProfileModelRequestDto createTargetRequestDto(String id, String network) {
        ProfileModelRequestDto dto = new ProfileModelRequestDto();
        dto.setId(id);
        dto.setNetwork(network);
        return dto;
    }



}
