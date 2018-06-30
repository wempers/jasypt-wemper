/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.stream.Stream;

/**
 * @author wemper
 * @version $Id: BeanNamePlaceholderRegistryPostProcessor.java,v 0.1 2018年06月29日 22:39 $Exp
 */
@Slf4j
public class BeanNamePlaceholderRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, Ordered {

    private Environment environment;

    BeanNamePlaceholderRegistryPostProcessor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) registry;
        Stream.of(bf.getBeanDefinitionNames())
                //Look for beans with placeholders name format: '${placeholder}' or '${placeholder:defaultValue}'
                .filter(name -> name.matches("\\$\\{[\\w.-]+(?>:[\\w.-]+)?\\}"))
                .forEach(placeholder -> {
                    String actualName = environment.resolveRequiredPlaceholders(placeholder);
                    BeanDefinition bd = bf.getBeanDefinition(placeholder);
                    bf.removeBeanDefinition(placeholder);
                    bf.registerBeanDefinition(actualName, bd);
                    log.debug("Registering new name '{}' for Bean definition with placeholder name: {}", actualName, placeholder);
                });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}

