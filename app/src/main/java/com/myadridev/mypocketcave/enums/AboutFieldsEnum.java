package com.myadridev.mypocketcave.enums;


import com.myadridev.mypocketcave.R;

public enum AboutFieldsEnum {
    VERSION(0, R.string.about_version),
    CONTACT(1, R.string.about_contact),
    SOURCES(2, R.string.about_sources),
    LICENSE(3, R.string.about_license);

    private int index;
    private int stringResource;

    AboutFieldsEnum(int _index, int _stringResource) {
        index = _index;
        stringResource = _stringResource;
    }

    public int getStringResource() {
        return stringResource;
    }

    public int compare(AboutFieldsEnum otherAboutFieldsEnum) {
        if (otherAboutFieldsEnum.index > index) {
            return -1;
        } else if (otherAboutFieldsEnum.index < index) {
            return 1;
        } else {
            return 0;
        }
    }
}
