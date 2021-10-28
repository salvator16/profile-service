package com.sample.profileservice.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static final String folderPath = "\\src\\main\\resources\\images\\";
    public static final String fileExtension = ".jpg";
    public static final String assetPath = "/images/";

    public static String currentWorkingDirectory() {
        String location;
        File file = new File(".");
        try {
            location = file.getCanonicalPath();
        } catch (IOException e) {
            log.error("Could not reach current working directoy {}", e.getMessage());
            location = file.getAbsolutePath();
        }
        return location;
    }

    public static boolean isFileExists(File file) {
        return file.exists();
    }

    public static String generateFullPath(String network, String id) {
        return new StringBuilder(currentWorkingDirectory())
                .append(folderPath)
                .append(network)
                .append("-")
                .append(id)
                .append(fileExtension)
                .toString();
    }

    public static String getFolderPath() {
        return new StringBuilder(currentWorkingDirectory())
                .append(folderPath)
                .toString();
    }

    public static String getAssetPath(String network, String id) {
        return new StringBuilder()
                .append(assetPath)
                .append(network)
                .append("-")
                .append(id)
                .append(fileExtension)
                .toString();
    }

}
