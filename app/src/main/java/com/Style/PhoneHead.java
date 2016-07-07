package com.Style;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by user on 2016/5/20.
 */
public class PhoneHead {
             public static void  HeadStyle(Activity context){
                 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     Window window = context.getWindow();
                     window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                             | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                     window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                             | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                             | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                     window.setStatusBarColor(Color.TRANSPARENT);
                     window.setNavigationBarColor(Color.TRANSPARENT);
                 }
             }
}
