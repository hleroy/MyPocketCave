package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.CaveModel;

/**
 * Created by adrien on 01/10/2016.
 */

public interface ICaveStorageManager {

    CaveModel getCave(int caveId);

    void insertCave(CaveModel cave);

    void updateCave(CaveModel cave);

    void deleteCave(CaveModel cave);
}
