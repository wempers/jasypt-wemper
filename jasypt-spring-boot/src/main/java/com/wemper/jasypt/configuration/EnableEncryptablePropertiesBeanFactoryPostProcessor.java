/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.configuration;

import static com.wemper.jasypt.EncryptablePropertySourceConverter.convertPropertySources;
import static com.wemper.jasypt.configuration.EncryptablePropertyResolverConfiguration.RESOLVER_BEAN_NAME;

import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.InterceptionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * @author fygu
 * @version $Id: EnableEncryptablePropertiesBeanFactoryPostProcessor.java,v 0.1 2018年06月29日 22:26 $Exp
 */
public class EnableEncryptablePropertiesBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationListener<ApplicationEvent>, Ordered {

  private static final Logger LOG = LoggerFactory.getLogger(EnableEncryptablePropertiesBeanFactoryPostProcessor.class);
  private ConfigurableEnvironment environment;
  private InterceptionMode interceptionMode;

  public EnableEncryptablePropertiesBeanFactoryPostProcessor() {
    this.interceptionMode = InterceptionMode.PROXY;
  }

  public EnableEncryptablePropertiesBeanFactoryPostProcessor(ConfigurableEnvironment environment, InterceptionMode interceptionMode) {
    this.environment = environment;
    this.interceptionMode = interceptionMode;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    LOG.info("Post-processing PropertySource instances");
    EncryptablePropertyResolver propertyResolver = beanFactory.getBean(RESOLVER_BEAN_NAME, EncryptablePropertyResolver.class);
    MutablePropertySources propSources = environment.getPropertySources();
    convertPropertySources(interceptionMode, propertyResolver, propSources);
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE;
  }

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    LOG.debug("Application Event Raised: {}", event.getClass().getSimpleName());
  }
}
