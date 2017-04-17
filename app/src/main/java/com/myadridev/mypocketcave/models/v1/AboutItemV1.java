package com.myadridev.mypocketcave.models.v1;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v1.AboutFieldsEnumV1;

@Deprecated
@JsonSerialize(as = AboutItemV1.class)
public class AboutItemV1 implements Comparable<AboutItemV1> {
    public AboutFieldsEnumV1 AboutFieldsEnum;
    public String Label = "";
    public String Value = "";

    public AboutItemV1(Context context, AboutFieldsEnumV1 aboutFieldsEnum, String value) {
        AboutFieldsEnum = aboutFieldsEnum;
        Label = context.getString(aboutFieldsEnum.StringResourceId);
        Value = value;
    }

    @Override
    public int compareTo(@NonNull AboutItemV1 otherItem) {
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
