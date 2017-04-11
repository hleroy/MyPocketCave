package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

public interface OnBottleUnplacedClickListener {

    void onBottleUnplaced(int bottleId, int quantity, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates);
}