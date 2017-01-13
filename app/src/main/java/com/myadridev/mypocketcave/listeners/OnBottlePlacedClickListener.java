package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottlePlacedClickListener {

    void onBottlePlaced(int bottleId, int quantity, CoordinatesModel patternCoordinates, CoordinatesModel coordinates);
}