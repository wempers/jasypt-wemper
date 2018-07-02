/*
 * wemper.org
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
import org.springframework.core.env.StandardEnvironment;

/**
 * @author fygu
 * @version $Id: StandardEncryptableEnvironment.java,v 0.1 2018年06月29日 21:54 $Exp
 */
public class StandardEncryptableEnvironment extends StandardEnvironment implements ConfigurableEnvironment {

  private final EncryptablePropertyResolver resolver;
  private final InterceptionMode interceptionMode;
  private MutablePropertySources encryptablePropertySources;
  private MutablePropertySources originalPropertySources;

  public StandardEncryptableEnvironment() {
    this(InterceptionMode.WRAPPER);
  }

  public StandardEncryptableEnvironment(InterceptionMode interceptionMode) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), new DefaultPropertyDetector());
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableEnvironment(InterceptionMode interceptionMode, EncryptablePropertyDetector detector) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(new DefaultLazyEncryptor(this), detector);
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(encryptor, new DefaultPropertyDetector());
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableEnvironment(StringEncryptor encryptor) {
    this.interceptionMode = InterceptionMode.WRAPPER;
    this.resolver = new DefaultPropertyResolver(encryptor, new DefaultPropertyDetector());
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableEnvironment(InterceptionMode interceptionMode, StringEncryptor encryptor, EncryptablePropertyDetector detector) {
    this.interceptionMode = interceptionMode;
    this.resolver = new DefaultPropertyResolver(encryptor, detector);
    actuallyCustomizePropertySources();
  }

  public StandardEncryptableEnvironment(InterceptionMode interceptionMode, EncryptablePropertyResolver resolver) {
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

