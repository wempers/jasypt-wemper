/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.configuration;

import com.wemper.jasypt.EncryptablePropertyDetector;
import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.EncryptablePropertySource;
import com.wemper.jasypt.detector.DefaultLazyPropertyDetector;
import com.wemper.jasypt.encryptor.DefaultLazyEncryptor;
import com.wemper.jasypt.resolver.DefaultLazyPropertyResolver;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author fygu
 * @version $Id: EncryptablePropertyResolverConfiguration.java,v1.0 2018年06月28日 17:34 $Exp
 */
@Configuration
public class EncryptablePropertyResolverConfiguration {

    public static final String RESOLVER_BEAN_NAME = "lazyEncryptablePropertyResolver";
    private static final String ENCRYPTOR_BEAN_PLACEHOLDER = "${jasypt.encryptor.bean:jasyptStringEncryptor}";
    private static final String DETECTOR_BEAN_PLACEHOLDER = "${jasypt.encryptor.property.detector-bean:encryptablePropertyDetector}";
    private static final String RESOLVER_BEAN_PLACEHOLDER = "${jasypt.encryptor.property.resolver-bean:encryptablePropertyResolver}";
    private static final String ENCRYPTOR_BEAN_NAME = "lazyJasyptStringEncryptor";
    private static final String DETECTOR_BEAN_NAME = "lazyEncryptablePropertyDetector";

    @Bean
    public EnvCopy envCopy(ConfigurableEnvironment environment) {
        return new EnvCopy(environment);
    }

    @Bean(name = ENCRYPTOR_BEAN_NAME)
    public StringEncryptor stringEncryptor(@SuppressWarnings("SpringJavaAutowiringInspection") EnvCopy envCopy, BeanFactory bf) {
        String customEncryptorBeanName = envCopy.get().resolveRequiredPlaceholders(ENCRYPTOR_BEAN_PLACEHOLDER);
        return new DefaultLazyEncryptor(envCopy.get(), customEncryptorBeanName, bf);
    }

    @Bean(name = DETECTOR_BEAN_NAME)
    public EncryptablePropertyDetector encryptablePropertyDetector(@SuppressWarnings("SpringJavaAutowiringInspection") EnvCopy envCopy, BeanFactory bf) {
        String prefix = envCopy.get().resolveRequiredPlaceholders("${jasypt.encryptor.property.prefix:ENC(}");
        String suffix = envCopy.get().resolveRequiredPlaceholders("${jasypt.encryptor.property.suffix:)}");
        String customDetectorBeanName = envCopy.get().resolveRequiredPlaceholders(DETECTOR_BEAN_PLACEHOLDER);
        return new DefaultLazyPropertyDetector(prefix, suffix, customDetectorBeanName, bf);
    }

    @Bean(name = RESOLVER_BEAN_NAME)
    public EncryptablePropertyResolver encryptablePropertyResolver(@Qualifier(DETECTOR_BEAN_NAME) EncryptablePropertyDetector propertyDetector, @Qualifier(ENCRYPTOR_BEAN_NAME) StringEncryptor encryptor, BeanFactory bf, @SuppressWarnings("SpringJavaAutowiringInspection") EnvCopy envCopy) {
        String customResolverBeanName = envCopy.get().resolveRequiredPlaceholders(RESOLVER_BEAN_PLACEHOLDER);
        return new DefaultLazyPropertyResolver(propertyDetector, encryptor, customResolverBeanName, bf);
    }

    /**
     * Need a copy of the environment without the Enhanced property sources to avoid circular dependencies.
     */
    private static class EnvCopy {
        StandardEnvironment copy;

        EnvCopy(ConfigurableEnvironment environment) {
            copy = new StandardEnvironment();
            environment.getPropertySources().forEach(ps -> {
                PropertySource<?> original = ps instanceof EncryptablePropertySource ? ((EncryptablePropertySource) ps).getDelegate() : ps;
                copy.getPropertySources().addLast(original);
            });
        }

        ConfigurableEnvironment get() {
            return copy;
        }
    }


}
