/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.annotation;

import com.wemper.jasypt.condition.OnMissingBeanCondition;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Conditional;

/**
 * @author fygu
 * @version $Id: ConditionalOnMissingBean.java,v 0.1 2018年06月29日 22:33 $Exp
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMissingBeanCondition.class)
public @interface ConditionalOnMissingBean {

}
