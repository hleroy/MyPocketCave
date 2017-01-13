package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottleUnplacedClickListener {

    void onBottleUnplaced(int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates);
}