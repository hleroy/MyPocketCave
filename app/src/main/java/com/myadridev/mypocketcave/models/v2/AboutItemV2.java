package com.myadridev.mypocketcave.models.v2;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.myadridev.mypocketcave.enums.v2.AboutFieldsEnumV2;

@JsonSerialize(as = AboutItemV2.class)
public class AboutItemV2 implements Comparable<AboutItemV2> {

    @JsonProperty("afe")
    public AboutFieldsEnumV2 AboutFieldsEnum;
    @JsonProperty("l")
    public String Label;
    @JsonProperty("v")
    public String Value;

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
