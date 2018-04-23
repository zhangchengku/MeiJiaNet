package meijia.com.meijianet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


public class ToastUtil {
    private static Toast toast;
    private static View view;

    private ToastUtil() {

    }

    @SuppressLint("ShowToast")
    private static void getToast(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        if (view == null) {
            view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
        }
        toast.setView(view);
    }

    public static void showShortToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence msg) {
        showToast(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
    }

    /**
     * 统一处理提示，访问服务器失败
     *
     * @param context
     */
//    public static void showServerError(Context context) {
//        showToast(context.getApplicationContext(), R.string.common_access_server_error, Toast.LENGTH_SHORT);
//    }

    /**
     * 统一处理提示，网络不可用
     *
     * @param context
     */
//    public static void showNetworkError(Context context) {
//        showToast(context.getApplicationContext(), R.string.common_no_network, Toast.LENGTH_SHORT);
//    }

    private static void showToast(Context context, CharSequence msg,
                                  int duration) {
        try {
            getToast(context);
            toast.setText(msg);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {

        }
    }

    private static void showToast(Context context, int resId, int duration) {
        try {
            if (resId == 0) {
                return;
            }
            getToast(context);
            toast.setText(resId);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {

        }
    }


    public static void showCenterShortToast(Context context,String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


}