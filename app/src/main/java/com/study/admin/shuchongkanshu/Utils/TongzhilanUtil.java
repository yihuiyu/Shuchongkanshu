package com.study.admin.shuchongkanshu.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by admin on 2017/6/30.
 */

public class TongzhilanUtil {
    /**
     * 设置透明状态栏与导航栏 这个给首页和login.
     *
     * @param navi true不设置导航栏|false设置导航栏
     */
    public static void setStatusBar(Activity activity, boolean navi) {
        //api>21,全透明状态栏和导航栏;api>19,半透明状态栏和导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            if (navi) {
                window.getDecorView().setSystemUiVisibility(View
                        .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//导航栏不会被隐藏但activity布局会扩展到导航栏所在位置
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity.getWindow());
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT); // 通知栏所需颜色
        }
    }

    /**
     * 大部分activity 的方法
     *
     * @param activity
     * @param colorResId
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, activity.getWindow());
                SystemBarTintManager tintManager = new SystemBarTintManager(activity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(colorResId); // 通知栏所需颜色
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Window win) {// 通知栏变色
//      Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}