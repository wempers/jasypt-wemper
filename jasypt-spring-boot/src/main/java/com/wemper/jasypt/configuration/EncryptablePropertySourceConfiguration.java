/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author wemper
 * @version $Id: EncryptablePropertySourceConfiguration.java,v 0.1 2018年06月29日 22:29 $Exp
 */
@Configuration
@Import(EncryptablePropertyResolverConfiguration.class)
public class EncryptablePropertySourceConfiguration {

  @Bean
  public static EncryptablePropertySourceBeanFactoryPostProcessor encryptablePropertySourceAnnotationPostProcessor(ConfigurableEnvironment env) {
    return new EncryptablePropertySourceBeanFactoryPostProcessor(env);
  }

}
