package com.publicbenifitsharing.android.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    public static void toastCenter(Context context,String content){
        Toast toast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
