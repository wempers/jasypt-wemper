package com.wemper.jasypt;

import com.wemper.jasypt.aop.EncryptableMutablePropertySourcesInterceptor;
import com.wemper.jasypt.aop.EncryptablePropertySourceMethodInterceptor;
import com.wemper.jasypt.wrapper.EncryptableEnumerablePropertySourceWrapper;
import com.wemper.jasypt.wrapper.EncryptableMapPropertySourceWrapper;
import com.wemper.jasypt.wrapper.EncryptablePropertySourceWrapper;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.env.*;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class EncryptablePropertySourceConverter {

    public static void convertPropertySources(InterceptionMode interceptionMode, EncryptablePropertyResolver propertyResolver, MutablePropertySources propSources) {
        StreamSupport.stream(propSources.spliterator(), false)
                .filter(ps -> !(ps instanceof EncryptablePropertySource))
                .map(ps -> makeEncryptable(interceptionMode, propertyResolver, ps))
                .collect(toList())
                .forEach(ps -> propSources.replace(ps.getName(), ps));
    }

    @SuppressWarnings("unchecked")
    public static <T> PropertySource<T> makeEncryptable(InterceptionMode interceptionMode, EncryptablePropertyResolver propertyResolver, PropertySource<T> propertySource) {
        if (propertySource instanceof EncryptablePropertySource) {
            return propertySource;
        }
        PropertySource<T> encryptablePropertySource = convertPropertySource(interceptionMode, propertyResolver, propertySource);
//        log.info("Converting PropertySource {} [{}] to {}", propertySource.getName(), propertySource.getClass().getName(),
//                AopUtils.isAopProxy(encryptablePropertySource) ? "AOP Proxy" : encryptablePropertySource.getClass().getSimpleName());
        return encryptablePropertySource;
    }

    private static <T> PropertySource<T> convertPropertySource(InterceptionMode interceptionMode, EncryptablePropertyResolver propertyResolver, PropertySource<T> propertySource) {
        return interceptionMode == InterceptionMode.PROXY
                ? proxyPropertySource(propertySource, propertyResolver) : instantiatePropertySource(propertySource, propertyResolver);
    }

    public static MutablePropertySources proxyPropertySources(InterceptionMode interceptionMode, EncryptablePropertyResolver propertyResolver, MutablePropertySources propertySources) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(MutablePropertySources.class);
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addInterface(PropertySources.class);
        proxyFactory.setTarget(propertySources);
        proxyFactory.addAdvice(new EncryptableMutablePropertySourcesInterceptor(interceptionMode, propertyResolver));
        return (MutablePropertySources) proxyFactory.getProxy();
    }

    @SuppressWarnings("unchecked")
    public static <T> PropertySource<T> proxyPropertySource(PropertySource<T> propertySource, EncryptablePropertyResolver resolver) {
        //Silly Chris Beams for making CommandLinePropertySource getProperty and containsProperty methods final. Those methods
        //can't be proxied with CGLib because of it. So fallback to wrapper for Command Line Arguments only.
        if (CommandLinePropertySource.class.isAssignableFrom(propertySource.getClass())) {
            return instantiatePropertySource(propertySource, resolver);
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetClass(propertySource.getClass());
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addInterface(EncryptablePropertySource.class);
        proxyFactory.setTarget(propertySource);
        proxyFactory.addAdvice(new EncryptablePropertySourceMethodInterceptor<>(propertySource, resolver));
        return (PropertySource<T>) proxyFactory.getProxy();
    }

    @SuppressWarnings("unchecked")
    public static <T> PropertySource<T> instantiatePropertySource(PropertySource<T> propertySource, EncryptablePropertyResolver resolver) {
        PropertySource<T> encryptablePropertySource;
        if (needsProxyAnyway(propertySource)) {
            encryptablePropertySource = proxyPropertySource(propertySource, resolver);
        } else if (propertySource instanceof MapPropertySource) {
            encryptablePropertySource = (PropertySource<T>) new EncryptableMapPropertySourceWrapper((MapPropertySource) propertySource, resolver);
        } else if (propertySource instanceof EnumerablePropertySource) {
            encryptablePropertySource = new EncryptableEnumerablePropertySourceWrapper<>((EnumerablePropertySource) propertySource, resolver);
        } else {
            encryptablePropertySource = new EncryptablePropertySourceWrapper<>(propertySource, resolver);
        }
        return encryptablePropertySource;
    }

    @SuppressWarnings("unchecked")
    private static boolean needsProxyAnyway(PropertySource<?> ps) {
        return needsProxyAnyway((Class<? extends PropertySource<?>>) ps.getClass());
    }

    private static boolean needsProxyAnyway(Class<? extends PropertySource<?>> psClass) {
        return needsProxyAnyway(psClass.getName());
    }

    /**
     *  Some Spring Boot code actually casts property sources to this specific type so must be proxied.
     */
    private static boolean needsProxyAnyway(String className) {
        return Stream.of(
                "org.springframework.boot.context.config.ConfigFileApplicationListener$ConfigurationPropertySources",
                "org.springframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource"
        ).anyMatch(className::equals);
    }
}
