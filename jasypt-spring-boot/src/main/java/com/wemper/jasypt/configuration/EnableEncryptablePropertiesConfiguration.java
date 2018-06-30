/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.configuration;

import com.wemper.jasypt.InterceptionMode;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author fygu
 * @version $Id: EnableEncryptablePropertiesConfiguration.java,v1.0 2018年06月28日 17:33 $Exp
 */
@Configuration
@Import(EncryptablePropertyResolverConfiguration.class)
public class EnableEncryptablePropertiesConfiguration implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Bean
    public static EnableEncryptablePropertiesBeanFactoryPostProcessor enableEncryptablePropertySourcesPostProcessor(ConfigurableEnvironment environment) {
        boolean proxyPropertySources = environment.getProperty("jasypt.encryptor.proxyPropertySources", Boolean.TYPE, false);
        InterceptionMode interceptionMode = proxyPropertySources ? InterceptionMode.PROXY : InterceptionMode.WRAPPER;
        return new EnableEncryptablePropertiesBeanFactoryPostProcessor(environment, interceptionMode);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
       // log.info("Bootstraping jasypt-string-boot auto configuration in context: {}", applicationContext.getId());
    }
}
