package com.myadridev.mypocketcave.models.v2;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;

public class AboutItemV2 implements Comparable<AboutItemV2> {

    @SerializedName("afe")
    public AboutFieldsEnumV2 AboutFieldsEnum;
    @SerializedName("l")
    public String Label = "";
    @SerializedName("v")
    public String Value = "";

    public AboutItemV2(Context context, AboutFieldsEnumV2 aboutFieldsEnum, String value) {
        AboutFieldsEnum = aboutFieldsEnum;
        Label = context.getString(aboutFieldsEnum.StringResourceId);
        Value = value;
    }

    @Override
    public int compareTo(@NonNull AboutItemV2 otherItem) {
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
