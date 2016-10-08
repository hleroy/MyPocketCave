package com.myadridev.mypocketcave.managers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adrie on 08/10/2016.
 */

public class DependencyManager {

    private static Map<Class, Object> singletonMap;
    private static boolean isInitDone = false;

    public static void init() {
        singletonMap = new HashMap<>();
        isInitDone = true;
    }

    public static void cleanUp() {
        singletonMap = null;
        isInitDone = false;
    }

    private static void ensureIsInitDone() {
        if (!isInitDone) {
            throw new IllegalStateException("DependencyManager is not initialized");
        }
    }

    public static <I, S extends I> void registerSingleton(Class<I> dependencyInterface, S dependencySingleton) {
        ensureIsInitDone();
        registerSingleton(dependencyInterface, dependencySingleton, false);
    }

    public static <I, S extends I> void registerSingleton(Class<I> dependencyInterface, S dependencySingleton, boolean isOverridePossible) {
        ensureIsInitDone();
        if (!isOverridePossible && singletonMap.containsKey(dependencyInterface)) {
            throw new IllegalArgumentException("Type " + dependencyInterface.getName() + " is already registered.");
        }
        singletonMap.put(dependencyInterface, dependencySingleton);
    }

    public static <I> I getSingleton(Class<I> dependencyInterface) {
        ensureIsInitDone();
        if (!singletonMap.containsKey(dependencyInterface)) {
            throw new IllegalArgumentException("Type " + dependencyInterface.getName() + " is not registered.");
        }
        return (I) singletonMap.get(dependencyInterface);
    }
}
