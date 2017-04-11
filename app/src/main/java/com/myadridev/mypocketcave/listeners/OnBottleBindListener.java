package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.models.v2.BottleModelV2;

public interface OnBottleBindListener {

    void onBottleBind(BottleViewHolder holder, BottleModelV2 bottle);
}
