/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt;

/**
 * @author fygu
 * @version $Id: EncryptablePropertyResolver.java,v1.0 2018年06月28日 17:38 $Exp
 */
public interface EncryptablePropertyResolver {


  /**
   * Returns the unencrypted version of the value provided free on any prefixes/suffixes or any other metadata
   * surrounding the encrypted value. Or the actual same String if no encryption was detected.
   *
   * @param value the property value
   * @return either the same value if the value is not encrypted, or the decrypted version.
   */
  String resolvePropertyValue(String value);

}
