/*
 * wemper.org
 * Copyright (c) 2017-2018. All Rights Reserved.
 */
package com.wemper.jasypt.util;

import java.util.function.Supplier;

/**
 * @author fygu
 * @version $Id: Singleton.java,v1.0 2018年06月28日 17:47 $Exp
 */
public final class Singleton<R> implements Supplier<R> {

  private boolean initialized = false;
  private volatile Supplier<R> instanceSupplier;

  public Singleton(final Supplier<R> original) {
    instanceSupplier = () -> {
      synchronized (original) {
        if (!initialized) {
          final R singletonInstance = original.get();
          instanceSupplier = () -> singletonInstance;
          initialized = true;
        }
        return instanceSupplier.get();
      }
    };
  }

  @Override
  public R get() {
    return instanceSupplier.get();
  }
}