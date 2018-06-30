/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.annotation;

import com.wemper.jasypt.configuration.EncryptablePropertySourceConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wemper
 * @version $Id: EncryptablePropertySources.java,v 0.1 2018年06月29日 22:36 $Exp
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EncryptablePropertySourceConfiguration.class)
public @interface EncryptablePropertySources {
    EncryptablePropertySource[] value();
}
