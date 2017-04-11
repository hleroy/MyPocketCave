package com.myadridev.mypocketcave.enums.v2;

import com.myadridev.mypocketcave.R;

public enum AboutFieldsEnumV2 {
    v(0, R.string.about_version),
    c(1, R.string.about_contact),
    s(2, R.string.about_sources),
    l(3, R.string.about_license);

    public final int Id;
    public final int StringResourceId;

    AboutFieldsEnumV2(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public static AboutFieldsEnumV2 getById(int id) {
        for (AboutFieldsEnumV2 about : values()) {
            if (id == about.Id)
                return about;
        }
        return null;
    }

    public int compare(AboutFieldsEnumV2 otherAboutFieldsEnum) {
        if (otherAboutFieldsEnum.Id > Id) {
            return -1;
        } else if (otherAboutFieldsEnum.Id < Id) {
            return 1;
        } else {
            return 0;
        }
    }
}
