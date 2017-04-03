package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.v1.CoordinatesModel;

public interface OnPlaceClickListener {

    void onPlaceClick(CoordinatesModel patternCoordinates, CoordinatesModel coordinates);
}