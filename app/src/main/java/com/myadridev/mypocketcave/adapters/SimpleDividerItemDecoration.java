package com.myadridev.mypocketcave.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.myadridev.mypocketcave.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDividerTop;
    private final Drawable mDividerBottom;
    private final Drawable mDividerLeft;
    private final Drawable mDividerRight;

    public SimpleDividerItemDecoration(Context context) {
        mDividerTop = ContextCompat.getDrawable(context, R.drawable.line_divider);
        mDividerBottom = ContextCompat.getDrawable(context, R.drawable.line_divider);
        mDividerLeft = ContextCompat.getDrawable(context, R.drawable.line_divider);
        mDividerRight = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mDividerTop.getIntrinsicHeight();
        }

        if (parent.getChildAdapterPosition(view) == parent.getChildCount() - 1) {
            outRect.bottom = mDividerBottom.getIntrinsicHeight();
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (i == 0) {
                int topLineBottom = child.getTop() - params.topMargin;
                int topLineTop = topLineBottom - mDividerTop.getIntrinsicHeight();
                mDividerTop.setBounds(left, topLineTop, right, topLineBottom);
                mDividerTop.draw(c);
            }

            int bottomtLineTop = child.getBottom() + params.bottomMargin;
            int bottomLineBottom = bottomtLineTop + mDividerBottom.getIntrinsicHeight();
            mDividerBottom.setBounds(left, bottomtLineTop, right, bottomLineBottom);
            mDividerBottom.draw(c);

            int leftLineLeft = child.getLeft() + params.leftMargin;
            int leftLineRight = leftLineLeft + mDividerLeft.getIntrinsicWidth();
            mDividerLeft.setBounds(leftLineLeft, top, leftLineRight, bottom);
            mDividerLeft.draw(c);

            int rightLineRight = child.getRight() - params.rightMargin;
            int rightLineLeft = rightLineRight - mDividerRight.getIntrinsicWidth();
            mDividerRight.setBounds(rightLineLeft, top, rightLineRight, bottom);
            mDividerRight.draw(c);
        }
    }
}