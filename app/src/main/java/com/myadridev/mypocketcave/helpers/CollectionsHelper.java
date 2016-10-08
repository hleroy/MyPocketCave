package com.myadridev.mypocketcave.helpers;

import java.util.Map;

public class CollectionsHelper {

    public static <K, V> V getValueOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return defaultValue;
    }
}
