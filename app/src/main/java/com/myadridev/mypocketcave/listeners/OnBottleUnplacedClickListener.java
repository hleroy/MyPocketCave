package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottleUnplacedClickListener {

    void onBottleUnplaced(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId);
}