package com.myadridev.mypocketcave.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myadridev.mypocketcave.R;

public class SeekbarRange extends RelativeLayout {

    private boolean isInit;

    private TextView selectedRangeTextView;
    private View totalRangeView;
    private View selectedRangeView;
    private ImageView selectedMinView;
    private ImageView selectedMaxView;

    private int minValue;
    private int maxValue;
    private int selectedMinValue;
    private int selectedMaxValue;
    private Drawable cursorIconSrc;
    private int rangeBackgroundColor;
    private int selectedRangeColor;

    private int pixelStep;
    private int totalRangeViewWidth;
    private int cursorWidth;

    private int minCurrentX;
    private int minLeftX;
    private int minRightX;
    private int maxCurrentX;
    private int maxLeftX;
    private int maxRightX;

    public SeekbarRange(Context context) {
        super(context);
        init(context, null);
    }

    public SeekbarRange(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SeekbarRange(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflateView(context, attrs);
    }

    private void inflateView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.seekbar_range, this);
        selectedRangeTextView = (TextView) findViewById(R.id.selected_range_text);
        totalRangeView = findViewById(R.id.total_range);
        selectedRangeView = findViewById(R.id.selected_range);
        selectedMinView = (ImageView) findViewById(R.id.selected_min);
        selectedMaxView = (ImageView) findViewById(R.id.selected_max);
        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            minValue = 0;
            maxValue = 100;
            selectedMinValue = minValue;
            selectedMaxValue = maxValue;
            cursorIconSrc = ContextCompat.getDrawable(context, R.drawable.price_on);
            rangeBackgroundColor = ContextCompat.getColor(context, R.color.colorAccent);
            selectedRangeColor = ContextCompat.getColor(context, R.color.colorPrimary);
        } else {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekbarRange);
            try {
                minValue = a.getInt(R.styleable.SeekbarRange_minValue, 0);
                maxValue = a.getInt(R.styleable.SeekbarRange_maxValue, 100);
                selectedMinValue = a.getInteger(R.styleable.SeekbarRange_selectedMinValue, minValue);
                selectedMaxValue = a.getInteger(R.styleable.SeekbarRange_selectedMaxValue, maxValue);
                cursorIconSrc = a.getDrawable(R.styleable.SeekbarRange_cursorIconSrc);
                if (cursorIconSrc == null) {
                    cursorIconSrc = ContextCompat.getDrawable(context, R.drawable.price_on);
                }
                rangeBackgroundColor = a.getColor(R.styleable.SeekbarRange_rangeBackgroundColor, ContextCompat.getColor(context, R.color.colorAccent));
                selectedRangeColor = a.getColor(R.styleable.SeekbarRange_selectedRangeColor, ContextCompat.getColor(context, R.color.colorPrimary));
            } finally {
                a.recycle();
            }
        }

        setRangeText();
        totalRangeView.setBackgroundColor(rangeBackgroundColor);
        selectedRangeView.setBackgroundColor(selectedRangeColor);
        selectedMinView.setImageDrawable(cursorIconSrc);
        selectedMinView.setOnTouchListener(onMinViewTouch());
        selectedMaxView.setImageDrawable(cursorIconSrc);
        selectedMaxView.setOnTouchListener(onMaxViewTouch());
        totalRange = maxValue - minValue;
        isInit = true;
    }

    private int totalRange;

    private OnTouchListener onMinViewTouch() {
        return (View v, MotionEvent event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getRawX();

                    if (x <= minLeftX) {
                        setSelectedMinValue(selectedMinValue - 1);
                    } else if (x >= minRightX) {
                        setSelectedMinValue(selectedMinValue + 1);
                    }
                    break;
            }

            return true;
        };
    }

    private OnTouchListener onMaxViewTouch() {
        return (View v, MotionEvent event) -> {
            if (selectedMaxValue == maxValue && selectedMaxValue == selectedMinValue) {
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getRawX();

                    if (x <= maxLeftX) {
                        setSelectedMaxValue(selectedMaxValue - 1);
                    } else if (x >= maxRightX) {
                        setSelectedMaxValue(selectedMaxValue + 1);
                    }
                    break;
            }

            return true;
        };
    }

    @SuppressWarnings("unused")
    public int getSelectedMinValue() {
        return selectedMinValue;
    }

    @SuppressWarnings("unused")
    public void setSelectedMinValue(int selectedMinValue) {
        int oldSelectedMinValue = this.selectedMinValue;
        this.selectedMinValue = Math.max(minValue, Math.min(selectedMinValue, selectedMaxValue));
        if (this.selectedMinValue == oldSelectedMinValue) return;

        setRangeText();
        requestNewLayout();
    }

    private void setRangeText() {
        selectedRangeTextView.setText(String.valueOf(selectedMinValue) + " - " + String.valueOf(selectedMaxValue));
    }

    @SuppressWarnings("unused")
    public int getSelectedMaxValue() {
        return selectedMaxValue;
    }

    @SuppressWarnings("unused")
    public void setSelectedMaxValue(int selectedMaxValue) {
        int oldSelectedMaxValue = this.selectedMaxValue;
        this.selectedMaxValue = Math.min(maxValue, Math.max(selectedMinValue, selectedMaxValue));
        if (this.selectedMaxValue == oldSelectedMaxValue) return;

        setRangeText();
        requestNewLayout();
    }

    private void requestNewLayout() {
        invalidate();
        requestLayout();
    }

    private void updateCursorPosition(ImageView imageView, int value) {
        LayoutParams imageLayoutParams = (LayoutParams) imageView.getLayoutParams();
        imageLayoutParams.setMarginStart(getMarginForPosition(value));
        imageView.setLayoutParams(imageLayoutParams);
    }

    private int getMarginForPosition(int value) {
        return (totalRangeViewWidth * value / totalRange) - (cursorWidth / 2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!isInit) {
            totalRangeViewWidth = totalRangeView.getWidth();
            pixelStep = totalRangeViewWidth / totalRange;

            updateCursorsPosition();
        }

        if (isInit) {
            isInit = false;

            cursorWidth = selectedMinView.getWidth();

            LayoutParams minValueLayoutParams = (LayoutParams) selectedRangeTextView.getLayoutParams();
            minValueLayoutParams.setMarginEnd(minValueLayoutParams.getMarginEnd() + (cursorWidth / 2));
            selectedRangeTextView.setLayoutParams(minValueLayoutParams);

            LayoutParams totalRangeLayoutParams = (LayoutParams) totalRangeView.getLayoutParams();
            totalRangeLayoutParams.setMarginEnd(totalRangeLayoutParams.getMarginEnd() + (cursorWidth / 2));
            totalRangeView.setLayoutParams(totalRangeLayoutParams);
        }
    }

    private void updateCursorsPosition() {
        updateCursorPosition(selectedMaxView, selectedMaxValue);
        updateCursorPosition(selectedMinView, selectedMinValue);

        minCurrentX = selectedMinView.getLeft();
        maxCurrentX = selectedMaxView.getLeft();

        minLeftX = Math.max(totalRangeView.getLeft() + (cursorWidth / 2), minCurrentX - pixelStep);
        maxRightX = Math.min(totalRangeView.getRight() - (cursorWidth / 2), maxCurrentX + pixelStep);

        minRightX = Math.min(maxCurrentX, minCurrentX + pixelStep);
        maxLeftX = Math.max(minCurrentX, maxCurrentX - pixelStep);
    }
}
