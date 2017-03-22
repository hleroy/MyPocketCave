package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.models.CoordinatesModel;

import java.util.List;
import java.util.Map;

public interface OnValueChangedListener {

    void onValueChanged(Map<CoordinatesModel, List<CoordinatesModel>> coordinatesToUpdate);
}
