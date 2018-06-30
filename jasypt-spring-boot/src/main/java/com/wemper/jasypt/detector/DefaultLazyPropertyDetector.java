/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.detector;

import com.wemper.jasypt.EncryptablePropertyDetector;
import com.wemper.jasypt.util.Singleton;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author fygu
 * @version $Id: DefaultLazyPropertyDetector.java,v1.0 2018年06月28日 17:51 $Exp
 */
@Slf4j
public class DefaultLazyPropertyDetector implements EncryptablePropertyDetector{

  private Singleton<EncryptablePropertyDetector> singleton;

  public DefaultLazyPropertyDetector(String prefix, String suffix, String customDetectorBeanName, BeanFactory bf) {
    singleton = new Singleton<>(() ->
        Optional.of(customDetectorBeanName)
            .filter(bf::containsBean)
            .map(name -> (EncryptablePropertyDetector) bf.getBean(name))
            .map(bean -> {
              log.info("Found Custom Detector Bean {} with name: {}", bean, customDetectorBeanName);
              return bean;
            })
            .orElseGet(() -> {
              log.info("Property Detector custom Bean not found with name '{}'. Initializing Default Property Detector", customDetectorBeanName);
              return new DefaultPropertyDetector(prefix, suffix);
            }));
  }

  @Override
  public boolean isEncrypted(String property) {
    return singleton.get().isEncrypted(property);
  }

  @Override
  public String unwrapEncryptedValue(String property) {
    return singleton.get().unwrapEncryptedValue(property);
  }

}
