package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.v2.CoordinatesModelV2;

import java.util.List;
import java.util.Map;

public interface OnValueChangedListener {

    void onValueChanged(Map<CoordinatesModelV2, List<CoordinatesModelV2>> coordinatesToUpdate);
}
