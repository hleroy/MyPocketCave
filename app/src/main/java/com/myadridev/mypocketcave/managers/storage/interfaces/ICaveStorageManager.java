package com.myadridev.mypocketcave.managers.storage.interfaces;

import com.myadridev.mypocketcave.models.CaveModel;

public interface ICaveStorageManager {

    CaveModel getCave(int caveId);

    void insertOrUpdateCave(CaveModel cave);

    void deleteCave(CaveModel cave);
}
