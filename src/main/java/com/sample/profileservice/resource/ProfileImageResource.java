package com.sample.profileservice.resource;

import com.sample.profileservice.model.dto.ImageModel;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;
import com.sample.profileservice.model.dto.ProfileModelResponseDto;
import com.sample.profileservice.service.ProfileService;
import com.sample.profileservice.service.SocialNetworkFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/profilePics")
public class ProfileImageResource {

    private final ProfileService profileService;

    private final SocialNetworkFactory socialNetworkFactory;

    @Operation(summary = "Get single image with given social network and id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK Image Result as a byte",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content) })
    @GetMapping(value = "/{socialNetwork}/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("socialNetwork") final String socialNetwork,
                                           @PathVariable("id") final String id) throws IOException {
        return ResponseEntity.ok(profileService.getImage(id, socialNetwork));
    }

    @Operation(summary = "Get all images under file system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK Image Results as base64 encoded",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content) })
    @GetMapping(value = "/all-images")
    public ResponseEntity<List<ImageModel>> getAllImages() throws IOException {
        return ResponseEntity.ok().body(profileService.getAllImages());
    }


    @Operation(summary = "Download images with given social media target list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "All target images are downloaded, and return create URI s",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Image Not Found",
                    content = @Content) })
    @PostMapping
    @ResponseBody
    public ResponseEntity<List<ProfileModelResponseDto>> downloadSocialNetworkImages(@RequestBody List<ProfileModelRequestDto> modelDtoList) {
        profileService.download(modelDtoList);
        return getImageDownloadResponse(modelDtoList);
    }

    private ResponseEntity<List<ProfileModelResponseDto>> getImageDownloadResponse(List<ProfileModelRequestDto> dtoList) {

        List<ProfileModelResponseDto> responseDtoList = dtoList.stream().map( dt -> {
            final URI uri = URI.create("/api/profilePics" + "/" + dt.getNetwork() + "/" + dt.getId());

            ProfileModelResponseDto responseDto = new ProfileModelResponseDto();

            responseDto.setId(dt.getId());
            responseDto.setNetwork(dt.getNetwork());
            responseDto.setUrl(uri.toString());

            return responseDto;

        }).collect(Collectors.toList());

        return ResponseEntity.created(URI.create("/api/profilePics")).body(responseDtoList);
    }


}
