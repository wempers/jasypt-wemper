/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt;

import com.wemper.jasypt.configuration.EnableEncryptablePropertiesConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fygu
 * @version $Id: JasyptSpringBootAutoConfiguration.java,v 0.1 2018年06月29日 22:02 $Exp
 */
@Configuration
@Import(EnableEncryptablePropertiesConfiguration.class)
public class JasyptSpringBootAutoConfiguration {
}
