package com.sample.profileservice.service;

import com.sample.profileservice.model.dto.ImageModel;
import com.sample.profileservice.model.dto.ProfileModelRequestDto;
import com.sample.profileservice.model.enums.SocialNetworkType;
import com.sample.profileservice.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final SocialNetworkFactory socialNetworkFactory;

    /**
     * Each individual client call are stateless and each thread pulled from
     * default join pool for sample purpose
     * additionally for resource management, it could be nice to have
     * defining dedicated custom Executor pool
     *
     * @param targetListDto
     */
    @Override
    public void download(List<ProfileModelRequestDto> targetListDto) {

        log.info("Parallel stream is starting!!! ");

        targetListDto.parallelStream().forEach(t -> {
            SocialNetwork socialNetwork = socialNetworkFactory
                    .getProvider(SocialNetworkType.valueOf(t.getNetwork().toUpperCase(Locale.ROOT)));
            socialNetwork.downloadImage(t);
        });

    }

    @Override
    public byte[] getImage(String id, String socialNetwork) throws IOException {
        var singleImage = new ClassPathResource(FileUtils.getAssetPath(socialNetwork, id));
        return StreamUtils.copyToByteArray(singleImage.getInputStream());
    }

    @Override
    public List<ImageModel> getAllImages() throws IOException {
        File folder = new File(FileUtils.getFolderPath());
        File[] fileList = folder.listFiles();

        List<ImageModel> imageList = new ArrayList<>();
//        List<BufferedImage> imageList = new ArrayList<>();

        for (File file : fileList) {
            ImageModel model = new ImageModel();
            InputStream is = new FileInputStream(file);
            model.setImage(Base64.getEncoder().encodeToString(StreamUtils.copyToByteArray(is)));
            model.setId(file.getName());
            imageList.add(model);
            is.close();
        }
        return imageList;
    }
}


