package com.myadridev.mypocketcave.listeners;

import com.myadridev.mypocketcave.adapters.viewHolders.BottleViewHolder;
import com.myadridev.mypocketcave.models.v1.BottleModel;

public interface OnBottleBindListener {

    void onBottleBind(BottleViewHolder holder, BottleModel bottle);
}
