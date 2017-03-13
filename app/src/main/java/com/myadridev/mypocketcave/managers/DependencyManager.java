package com.myadridev.mypocketcave.managers;

import com.myadridev.mypocketcave.listeners.OnDependencyChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyManager {

    private static Map<Class, Object> singletonMap;
    private static Map<Class, List<OnDependencyChangeListener>> onDependencyChangeListenerMap;
    private static boolean isInitDone = false;

    public static void init() {
        singletonMap = new HashMap<>();
        onDependencyChangeListenerMap = new HashMap<>();
        isInitDone = true;
    }

    public static void cleanUp() {
        if (onDependencyChangeListenerMap != null) {
            for (Class dependencyInterface : onDependencyChangeListenerMap.keySet()) {
                fireOnDependencyChange(dependencyInterface);
            }
            onDependencyChangeListenerMap = null;
        }
        singletonMap = null;
        isInitDone = false;
    }

    public static boolean needsRestart() {
        return !isInitDone;
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
        if (singletonMap.containsKey(dependencyInterface)) {
            fireOnDependencyChange(dependencyInterface);
        }
        singletonMap.put(dependencyInterface, dependencySingleton);
    }

    public static <I> I getSingleton(Class<I> dependencyInterface, OnDependencyChangeListener listener) {
        ensureIsInitDone();
        if (!singletonMap.containsKey(dependencyInterface)) {
            throw new IllegalArgumentException("Type " + dependencyInterface.getName() + " is not registered.");
        }
        I singleton = (I) singletonMap.get(dependencyInterface);
        if (listener != null) {
            registerDependencyChangeListener(dependencyInterface, listener);
        }
        return singleton;
    }

    private static <I> void registerDependencyChangeListener(Class<I> dependencyInterface, OnDependencyChangeListener listener) {
        if (!onDependencyChangeListenerMap.containsKey(dependencyInterface)) {
            onDependencyChangeListenerMap.put(dependencyInterface, new ArrayList<>());
        }
        onDependencyChangeListenerMap.get(dependencyInterface).add(listener);
    }

    private static <I> void fireOnDependencyChange(Class<I> dependencyInterface) {
        if (!onDependencyChangeListenerMap.containsKey(dependencyInterface)) {
            return;
        }
        for (OnDependencyChangeListener listener : onDependencyChangeListenerMap.get(dependencyInterface)) {
            listener.onDependencyChange();
        }
    }
}
