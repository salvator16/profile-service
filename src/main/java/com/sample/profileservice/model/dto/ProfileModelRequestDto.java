package com.sample.profileservice.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProfileModelRequestDto {

    @NotBlank(message = "Target profile id or screen name is required")
    private String id;

    @NotBlank(message = "network type is required")
    private String network;

}
