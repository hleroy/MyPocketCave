package com.myadridev.mypocketcave.models.v1;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.AboutFieldsEnum;

@JsonSerialize(as = AboutItem.class)
public class AboutItem implements Comparable<AboutItem> {
    public AboutFieldsEnum AboutFieldsEnum;
    public String Label;
    public String Value;

    public AboutItem(Context context, AboutFieldsEnum aboutFieldsEnum, String value) {
        AboutFieldsEnum = aboutFieldsEnum;
        Label = context.getString(aboutFieldsEnum.StringResourceId);
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
