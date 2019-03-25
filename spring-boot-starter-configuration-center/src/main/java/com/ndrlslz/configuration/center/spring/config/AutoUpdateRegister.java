package com.ndrlslz.configuration.center.spring.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import com.ndrlslz.configuration.center.spring.model.SpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class AutoUpdateRegister {
    private static final Integer INITIAL_DELAY = 1;
    private static final Integer DELAY = 1;
    private final ConfigurationTemplate configurationTemplate;
    private ConcurrentHashMap<WeakReference<?>, SpringConfig> autoUpdateSpringConfigMap;

    @Autowired
    public AutoUpdateRegister(ConfigurationTemplate configurationTemplate) {
        this.configurationTemplate = configurationTemplate;
        this.autoUpdateSpringConfigMap = new ConcurrentHashMap<>();

        Executors
                .newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).build())
                .scheduleWithFixedDelay(this::cleanSpringConfig, INITIAL_DELAY, DELAY, TimeUnit.MINUTES);
    }

    public void register(SpringConfig springConfig) {
        WeakReference<Object> beanRef = springConfig.getBeanRef();
        String propertyName = springConfig.getPropertyName();

        configurationTemplate.listen(beanRef, propertyName, springConfig::updateValue);
        autoUpdateSpringConfigMap.putIfAbsent(beanRef, springConfig);
    }

    private void cleanSpringConfig() {
        autoUpdateSpringConfigMap.forEach((beanRef, springConfig) -> {
            Object bean = beanRef.get();
            if (Objects.isNull(bean)) {
                configurationTemplate.unListen(beanRef, springConfig.getPropertyName());
                autoUpdateSpringConfigMap.remove(beanRef);
            }
        });
    }
}