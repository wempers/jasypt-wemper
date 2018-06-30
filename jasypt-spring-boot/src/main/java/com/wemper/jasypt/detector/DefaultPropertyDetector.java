/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.detector;

import com.wemper.jasypt.EncryptablePropertyDetector;
import org.springframework.util.Assert;

/**
 * @author wemper
 * @version $Id: DefaultPropertyDetector.java,v 0.1 2018年06月29日 21:07 $Exp
 */
public class DefaultPropertyDetector implements EncryptablePropertyDetector {

    private String prefix = "ENC(";
    private String suffix = ")";

    public DefaultPropertyDetector() {
    }

    public DefaultPropertyDetector(String prefix, String suffix) {
        Assert.notNull(prefix, "Prefix can't be null");
        Assert.notNull(suffix, "Suffix can't be null");
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public boolean isEncrypted(String property) {
        if (property == null) {
            return false;
        }
        final String trimmedValue = property.trim();
        return (trimmedValue.startsWith(prefix) &&
                trimmedValue.endsWith(suffix));
    }

    @Override
    public String unwrapEncryptedValue(String property) {
        return property.substring(
                prefix.length(),
                (property.length() - suffix.length()));
    }
}
