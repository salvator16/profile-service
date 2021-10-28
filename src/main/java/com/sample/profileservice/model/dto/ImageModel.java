package com.sample.profileservice.model.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String image;


}
