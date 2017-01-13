package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottleDrunkClickListener {

    void onBottleDrunk(int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates);
}