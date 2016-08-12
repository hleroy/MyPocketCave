package com.myadridev.mypocketcave.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.myadridev.mypocketcave.R;
import com.myadridev.mypocketcave.listeners.OnPatternClickListener;
import com.myadridev.mypocketcave.models.CoordinatesModel;

public class AddPatternViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;

    private AddPatternViewHolder(View itemView, ImageView imageView) {
        super(itemView);
        this.imageView = imageView;
    }

    public static AddPatternViewHolder newInstance(View parent) {
        ImageView imageView = (ImageView) parent.findViewById(R.id.add_pattern_image);
        return new AddPatternViewHolder(parent, imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setOnItemClickListener(final OnPatternClickListener listener, final CoordinatesModel coordinates) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPatternClick(coordinates);
            }
        });
    }
}
