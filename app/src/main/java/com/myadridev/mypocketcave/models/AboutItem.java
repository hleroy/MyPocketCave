package com.myadridev.mypocketcave.models;

import android.content.Context;
import android.support.annotation.NonNull;

import com.myadridev.mypocketcave.enums.AboutFieldsEnum;

public class AboutItem implements Comparable<AboutItem> {
    public AboutFieldsEnum AboutFieldsEnum;
    public String Label;
    public String Value;

    public AboutItem(Context _context, AboutFieldsEnum aboutFieldsEnum, String value) {
        AboutFieldsEnum = aboutFieldsEnum;
        Label = _context.getString(aboutFieldsEnum.getStringResource());
        Value = value;
    }

    @Override
    public int compareTo(@NonNull AboutItem otherItem) {
        int compare = AboutFieldsEnum.compare(otherItem.AboutFieldsEnum);
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
