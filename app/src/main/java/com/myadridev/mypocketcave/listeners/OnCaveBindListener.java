package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.adapters.viewHolders.CaveViewHolder;
import com.myadridev.mypocketcave.models.v2.CaveLightModelV2;

public interface OnCaveBindListener {

    void onCaveBind(CaveViewHolder holder, CaveLightModelV2 cave);
}
