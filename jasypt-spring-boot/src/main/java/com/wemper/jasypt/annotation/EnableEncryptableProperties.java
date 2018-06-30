/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.annotation;

import com.wemper.jasypt.configuration.EnableEncryptablePropertiesConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wemper
 * @version $Id: EnableEncryptableProperties.java,v 0.1 2018年06月29日 22:34 $Exp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableEncryptablePropertiesConfiguration.class)
public @interface EnableEncryptableProperties {
}
