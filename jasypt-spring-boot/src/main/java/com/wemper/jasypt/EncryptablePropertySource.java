/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt;

import org.springframework.core.env.PropertySource;

/**
 *
 *
 * @author fygu
 * @version $Id: EncryptablePropertySource.java,v1.0 2018年06月28日 17:40 $Exp
 */
public interface EncryptablePropertySource<T> {

  PropertySource<T> getDelegate();

  default Object getProperty(EncryptablePropertyResolver resolver, PropertySource<T> source, String name) {
    Object value = source.getProperty(name);
    if (value instanceof String) {
      String stringValue = String.valueOf(value);
      return resolver.resolvePropertyValue(stringValue);
    }
    return value;
  }

}
