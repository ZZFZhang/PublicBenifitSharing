package com.publicbenifitsharing.android.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputWindowUtil {
    public static void hideInputWindow(Context context, View view){
        InputMethodManager imm=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
