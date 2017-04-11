package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

public interface OnBottlePlacedClickListener {

    void onBottlePlaced(int bottleId, int quantity, CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates);
}