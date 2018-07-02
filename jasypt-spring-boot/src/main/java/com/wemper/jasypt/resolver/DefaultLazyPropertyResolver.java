/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.resolver;

import com.wemper.jasypt.EncryptablePropertyDetector;
import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.util.Singleton;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author fygu
 * @version $Id: DefaultLazyPropertyResolver.java,v 0.1 2018年06月29日 21:05 $Exp
 */
@Log4j2
public class DefaultLazyPropertyResolver implements EncryptablePropertyResolver {

  private Singleton<EncryptablePropertyResolver> singleton;

  public DefaultLazyPropertyResolver(EncryptablePropertyDetector propertyDetector, StringEncryptor encryptor, String customResolverBeanName, BeanFactory bf) {
    singleton = new Singleton<>(() ->
        Optional.of(customResolverBeanName)
            .filter(bf::containsBean)
            .map(name -> (EncryptablePropertyResolver) bf.getBean(name))
            .map(bean -> {
              log.info("Found Custom Resolver Bean {} with name: {}", bean, customResolverBeanName);
              return bean;
            })
            .orElseGet(() -> {
              log.info("Property Resolver custom Bean not found with name '{}'. Initializing Default Property Resolver", customResolverBeanName);
              return new DefaultPropertyResolver(encryptor, propertyDetector);
            }));
  }

  @Override
  public String resolvePropertyValue(String value) {
    return singleton.get().resolvePropertyValue(value);
  }
}
