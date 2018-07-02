/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.environment;

import static com.wemper.jasypt.EncryptablePropertySourceConverter.convertPropertySources;
import static com.wemper.jasypt.EncryptablePropertySourceConverter.proxyPropertySources;

import com.wemper.jasypt.EncryptablePropertyDetector;
import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.InterceptionMode;
import com.wemper.jasypt.detector.DefaultPropertyDetector;
import com.wemper.jasypt.encryptor.DefaultLazyEncryptor;
import com.wemper.jasypt.resolver.DefaultPropertyResolver;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * @author wemper
 * @version $Id: StandardEncryptableServletEnvironment.java,v 0.1 2018年06月29日 21:16 $Exp
 */
public class StandardEncryptableServletEnvironment extends StandardServletEnvironment implements ConfigurableEnvironment {

  private final EncryptablePropertyResolver resolver;
  private final InterceptionMode interceptionMode;
  private MutablePropertySources encryptablePropertySources;
  private MutablePropertySources originalPropertySources;

  public StandardEncryptableServletEnvironment() {
    this(InterceptionMode.WRAPPER);
  }

  public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), new DefaultPropertyDetector());
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, EncryptablePropertyDetector detector) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), detector);
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(encryptor, new DefaultPropertyDetector());
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor, EncryptablePropertyDetector detector) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(encryptor, detector);
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableServletEnvironment(InterceptionMode interceptionMode, EncryptablePropertyResolver resolver) {
    this.interceptionMode = interceptionMode;
    this.resolver = resolver;
    actuallyCustomizePropertySources();
  }

  @Override
  protected void customizePropertySources(MutablePropertySources propertySources) {
    super.customizePropertySources(propertySources);
    this.originalPropertySources = propertySources;
  }

  protected void actuallyCustomizePropertySources() {
    convertPropertySources(interceptionMode, resolver, originalPropertySources);
    encryptablePropertySources = proxyPropertySources(interceptionMode, resolver, originalPropertySources);
  }

  @Override
  public MutablePropertySources getPropertySources() {
    return encryptablePropertySources;
  }
}
