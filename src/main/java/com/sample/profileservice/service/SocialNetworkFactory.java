package com.sample.profileservice.service;

import com.sample.profileservice.model.enums.SocialNetworkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SocialNetworkFactory {

    @Autowired
    private List<SocialNetwork> socialNetworkList;

    private final Map<String, SocialNetwork> providerCache = new HashMap<>();

    @PostConstruct
    public void registerProviderBeansToCache() {
        socialNetworkList.forEach( provider -> {
            providerCache.put(provider.getProviderType(), provider);
        });
    }

    public SocialNetwork getProvider(SocialNetworkType networkType) {
        SocialNetwork socialNetwork = providerCache.get(networkType.name());
        if (Objects.isNull(socialNetwork))
            throw new IllegalArgumentException("No provider found with given network type");
        return socialNetwork;
    }

}
