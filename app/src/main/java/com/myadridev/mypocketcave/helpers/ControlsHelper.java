package com.myadridev.mypocketcave.helpers;

import android.widget.EditText;

public class ControlsHelper {

    public static int getIntValue(EditText textField) {
        String valueString = textField.getText().toString();
        return valueString.isEmpty() ? 0 : Integer.valueOf(valueString);
    }
}
