package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

public interface OnPlaceClickListener {

    void onPlaceClick(CoordinatesModelV2 patternCoordinates, CoordinatesModelV2 coordinates);
}