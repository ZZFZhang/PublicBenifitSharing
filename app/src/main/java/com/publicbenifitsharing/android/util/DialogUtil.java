package com.publicbenifitsharing.android.util;

import android.content.Context;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.publicbenifitsharing.android.R;

public class DialogUtil {
    private static Dialog dialog=null;

    public interface CommonDialogCallback{
        void onLeftClick();
        void onRightClick();
    }

    public static void showQuitAccountDialog(Context context,CommonDialogCallback callback){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_quit_account_view,null);
        TextView cancel=(TextView) view.findViewById(R.id.cancel);
        TextView quit=(TextView) view.findViewById(R.id.quit);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callback.onLeftClick();
                dialog.dismiss();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRightClick();
                dialog.dismiss();
            }
        });
        dialog=new Dialog(context,R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window=dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        WindowManager windowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth=windowManager.getDefaultDisplay().getWidth();
        int windowHeight=windowManager.getDefaultDisplay().getHeight();
        layoutParams.width=(int)(windowWidth*0.8);
        layoutParams.height=(int)(windowHeight*0.25);
        window.setAttributes(layoutParams);
    }
}
