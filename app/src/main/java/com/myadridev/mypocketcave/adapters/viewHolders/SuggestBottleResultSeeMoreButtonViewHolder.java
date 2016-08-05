package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.myadridev.mypocketcave.R;

public class SuggestBottleResultSeeMoreButtonViewHolder extends RecyclerView.ViewHolder {
    public final Button button;

    private SuggestBottleResultSeeMoreButtonViewHolder(View itemView, Button button) {
        super(itemView);
        this.button = button;
    }

    public static SuggestBottleResultSeeMoreButtonViewHolder newInstance(View parent) {
        Button button = (Button) parent.findViewById(R.id.bottle_result_see_more_button);
        return new SuggestBottleResultSeeMoreButtonViewHolder(parent, button);
    }
}
