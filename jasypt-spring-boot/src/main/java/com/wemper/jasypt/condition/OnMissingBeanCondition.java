/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.condition;

import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * @author fygu
 * @version $Id: OnMissingBeanCondition.java,v 0.1 2018年06月29日 21:09 $Exp
 */
public class OnMissingBeanCondition extends SpringBootCondition implements ConfigurationCondition {


  @Override
  public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Map<String, Object> beanAttributes = metadata.getAnnotationAttributes(Bean.class.getName());
    String beanName = ((String[]) beanAttributes.get("name"))[0];
    if (StringUtils.isEmpty(beanName)) {
      throw new IllegalStateException("OnMissingBeanCondition can't detect bean name!");
    }
    boolean missingBean = !context.getBeanFactory().containsBean(context.getEnvironment().resolveRequiredPlaceholders(beanName));
    return missingBean ? ConditionOutcome.match(beanName + " not found") : ConditionOutcome.noMatch(beanName + " found");
  }

  @Override
  public ConfigurationPhase getConfigurationPhase() {
    return ConfigurationPhase.REGISTER_BEAN;
  }
}
