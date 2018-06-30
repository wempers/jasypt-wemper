/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.resolver;

import com.wemper.jasypt.EncryptablePropertyDetector;
import com.wemper.jasypt.EncryptablePropertyResolver;
import com.wemper.jasypt.detector.DefaultPropertyDetector;
import com.wemper.jasypt.exception.DecryptionException;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.util.Assert;

/**
 * @author wemper
 * @version $Id: DefaultPropertyResolver.java,v 0.1 2018年06月29日 21:06 $Exp
 */
public class DefaultPropertyResolver implements EncryptablePropertyResolver

    {

    private StringEncryptor encryptor;
    private EncryptablePropertyDetector detector;

    public DefaultPropertyResolver(StringEncryptor encryptor) {
        this(encryptor, new DefaultPropertyDetector());
    }

    public DefaultPropertyResolver(StringEncryptor encryptor, EncryptablePropertyDetector detector) {
        Assert.notNull(encryptor, "String encryptor can't be null");
        Assert.notNull(detector, "Encryptable Property detector can't be null");
        this.encryptor = encryptor;
        this.detector = detector;
    }

    @Override
    public String resolvePropertyValue(String value) {
        String actualValue = value;
        if (detector.isEncrypted(value)) {
            try {
                actualValue = encryptor.decrypt(detector.unwrapEncryptedValue(value.trim()));
            } catch (EncryptionOperationNotPossibleException e) {
                throw new DecryptionException("Decryption of Properties failed,  make sure encryption/decryption " +
                        "passwords match", e);
            }
        }
        return actualValue;
    }
}
