/*
 * wemper Inc.  wemper soft.
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt;

/**
 * @author fygu
 * @version $Id: EncryptablePropertyDetector.java,v1.0 2018年06月28日 17:36 $Exp
 */
public interface EncryptablePropertyDetector {

  /**
   * 返回属性是否被加密。通常基于前缀和后缀。
   *
   * @param property the property value to check whether is encrypted or not.
   * @return true if the property is encrypted.
   */
  boolean isEncrypted(String property);

  /**
   * 返回属性的一部分，该属性实际上是加密值，没有任何额外的元数据，如前缀和后缀。
   *
   * @param property the property value to extract the encrypted value.
   * @return the encrypted portion of the property value.
   */
  String unwrapEncryptedValue(String property);

}
