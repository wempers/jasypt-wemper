/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.exception;

/**
 * @author fygu
 * @version $Id: DecryptionException.java,v1.0 2018年06月28日 18:02 $Exp
 */
public class DecryptionException extends RuntimeException {
    public DecryptionException(String message) {
        super(message);
    }

    public DecryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}