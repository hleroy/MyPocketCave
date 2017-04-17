package com.myadridev.mypocketcave.enums.v1;


import com.myadridev.mypocketcave.R;

@Deprecated
public enum AboutFieldsEnumV1 {
    VERSION(0, R.string.about_version),
    CONTACT(1, R.string.about_contact),
    SOURCES(2, R.string.about_sources),
    LICENSE(3, R.string.about_license);

    public final int Id;
    public final int StringResourceId;

    AboutFieldsEnumV1(int id, int stringResourceId) {
        Id = id;
        StringResourceId = stringResourceId;
    }

    public int compare(AboutFieldsEnumV1 otherAboutFieldsEnum) {
        if (otherAboutFieldsEnum.Id > Id) {
            return -1;
        } else if (otherAboutFieldsEnum.Id < Id) {
            return 1;
        } else {
            return 0;
        }
    }
}
