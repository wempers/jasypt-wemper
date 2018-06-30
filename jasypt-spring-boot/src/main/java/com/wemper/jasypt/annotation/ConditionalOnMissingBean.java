/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.annotation;

import com.wemper.jasypt.condition.OnMissingBeanCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author wemper
 * @version $Id: ConditionalOnMissingBean.java,v 0.1 2018年06月29日 22:33 $Exp
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingBeanCondition.class)
public @interface ConditionalOnMissingBean {
}
