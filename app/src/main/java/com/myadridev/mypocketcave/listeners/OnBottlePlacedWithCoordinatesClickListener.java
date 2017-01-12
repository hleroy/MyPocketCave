package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

public interface OnBottlePlacedWithCoordinatesClickListener {

    void onBottlePlaced(CoordinatesModel patternCoordinates, CoordinatesModel coordinates, int bottleId);
}