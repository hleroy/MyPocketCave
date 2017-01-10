package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottleDrunkClickListener {

    void onBottleDrunk(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId);
}