/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.wrapper;

import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.EncryptablePropertySource;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

/**
 * @author fygu
 * @version $Id: EncryptableEnumerablePropertySourceWrapper.java,v 0.1 2018年06月29日 20:59 $Exp
 */
public class EncryptableEnumerablePropertySourceWrapper<T> extends EnumerablePropertySource<T> implements EncryptablePropertySource<T> {

  private final EnumerablePropertySource<T> delegate;
  private final EncryptablePropertyResolver resolver;

  public EncryptableEnumerablePropertySourceWrapper(EnumerablePropertySource<T> delegate, EncryptablePropertyResolver resolver) {
    super(delegate.getName(), delegate.getSource());
    Assert.notNull(delegate, "PropertySource delegate cannot be null");
    Assert.notNull(resolver, "EncryptablePropertyResolver cannot be null");
    this.delegate = delegate;
    this.resolver = resolver;
  }

  @Override
  public Object getProperty(String name) {
    return getProperty(resolver, delegate, name);
  }

  @Override
  public String[] getPropertyNames() {
    return delegate.getPropertyNames();
  }

  @Override
  public PropertySource<T> getDelegate() {
    return delegate;
  }
}
