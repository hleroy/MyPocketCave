package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.adapters.viewHolders.CaveViewHolder;
import com.myadridev.mypocketcave.models.v1.CaveLightModel;

public interface OnCaveBindListener {

    void onCaveBind(CaveViewHolder holder, CaveLightModel cave);
}
