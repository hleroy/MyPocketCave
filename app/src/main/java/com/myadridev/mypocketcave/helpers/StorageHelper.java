package com.myadridev.mypocketcave.helpers;

import java.util.Collections;
import java.util.List;

public class StorageHelper {

    public static int getNewId(List<Integer> ids) {
        Collections.sort(ids);
        int lastId = 1;
        for (int id : ids) {
            if (lastId == id) {
                lastId++;
            } else {
                break;
            }
        }
        return lastId;
    }
}
