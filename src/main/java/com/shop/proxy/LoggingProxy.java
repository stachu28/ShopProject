package com.shop.proxy;

import lombok.extern.java.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Dynamic proxy: logs how long every call to any interface took.
 */
@Log
public final class LoggingProxy {

    private record LoggingHandler(Object target) implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long start = System.nanoTime();
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            } finally {
                long ms = (System.nanoTime() - start) / 1_000_000;
                log.fine(() -> "%s took %d ms".formatted(method.getName(), ms));
            }
        }
    }

    private LoggingProxy() {
    }

    public static <T> T withLogging(T target, Class<T> contract) {
        Object proxy = Proxy.newProxyInstance(
                contract.getClassLoader(),
                new Class<?>[]{contract},
                new LoggingHandler(target));
        return contract.cast(proxy);
    }
}