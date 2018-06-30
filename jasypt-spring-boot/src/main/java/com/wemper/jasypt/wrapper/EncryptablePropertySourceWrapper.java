/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.wrapper;

import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.EncryptablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

/**
 * @author wemper
 * @version $Id: EncryptablePropertySourceWrapper.java,v 0.1 2018年06月29日 21:02 $Exp
 */
public class EncryptablePropertySourceWrapper<T> extends PropertySource<T> implements EncryptablePropertySource<T> {
    private final PropertySource<T> delegate;
    EncryptablePropertyResolver resolver;

    public EncryptablePropertySourceWrapper(PropertySource<T> delegate, EncryptablePropertyResolver resolver) {
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
    public PropertySource<T> getDelegate() {
        return delegate;
    }
}
