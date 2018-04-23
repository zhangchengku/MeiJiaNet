package meijia.com.meijianet.util;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import meijia.com.meijianet.R;

public class PromptUtil {
    private static Dialog transparentProgressDialog;
    private static Dialog dialog;


    /**
     * @param context
     * @param title         标题
     * @param msg           提示信息
     * @param leftBt        左按钮文字
     * @param rightBt       右按钮文字
     * @param leftListener  左按钮监听
     * @param rightListener 右按钮监听
     */
    private static void showCommonDialog(Context context, String title, String msg, String leftBt
            , String rightBt, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        closeCommonDialog();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_dialog, null);
        AppCompatButton btCancel = (AppCompatButton) view.findViewById(R.id.tv_cancel_dialog);
        AppCompatButton btConfirm = (AppCompatButton) view.findViewById(R.id.tv_confirm_dialog);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        btCancel.setText(leftBt);
        btConfirm.setText(rightBt);
        if(leftListener != null) {
            btCancel.setVisibility(View.VISIBLE);
            btCancel.setOnClickListener(leftListener);
        } else {
            btCancel.setVisibility(View.INVISIBLE);
        }
        btConfirm.setOnClickListener(rightListener);
        dialog.setContentView(view);
        tvTitle.setText(title);
        tvInfo.setText(msg);
        dialog.show();
    }

    /**
     * @param context
     * @param title         提示标题
     * @param msg           提示信息资源id
     * @param leftBt        左按钮文字资源id
     * @param rightBt       右按钮文字资源id
     * @param rightListener 右按钮监听
     */
    public static void showCommonDialog(Context context, int title, int msg, int leftBt
            , int rightBt, View.OnClickListener rightListener) {
        showCommonDialog(
                context
                , context.getString(title)
                , context.getString(msg)
                , context.getString(leftBt)
                , context.getString(rightBt), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptUtil.closeCommonDialog();
                    }
                }, rightListener);
    }

    /**
     * @param context
     * @param msg           提示信息资源id
     * @param leftBt        左按钮文字资源id
     * @param rightBt       右按钮文字资源id
     * @param leftListener  左按钮监听
     * @param rightListener 右按钮监听
     */
//    public static void showCommonDialog(Context context, int msg, int leftBt
//            , int rightBt, View.OnClickListener leftListener, View.OnClickListener rightListener) {
//        showCommonDialog(
//                context
//                , context.getString(R.string.common_tip)
//                , context.getString(msg)
//                , context.getString(leftBt)
//                , context.getString(rightBt), leftListener, rightListener);
//    }

    /**
     * @param context
     * @param msg           提示信息资源id
     * @param leftBt        左按钮文字资源id
     * @param rightBt       右按钮文字资源id
     * @param rightListener 右按钮监听
     */
//    public static void showCommonDialog(Context context, int msg, int leftBt
//            , int rightBt, View.OnClickListener rightListener) {
//        showCommonDialog(
//                context
//                , context.getString(R.string.common_tip)
//                , context.getString(msg)
//                , context.getString(leftBt)
//                , context.getString(rightBt), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PromptUtil.closeCommonDialog();
//                    }
//                }, rightListener);
//    }

    /**
     * @param context
     * @param msg           提示信息资源id
     * @param rightListener 确定按钮监听
     */
    public static void showCommonDialog(Context context, String msg, View.OnClickListener rightListener) {
        showCommonDialog(
                context
                , "温馨提示"
                , msg
                , "取消"
                , "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptUtil.closeCommonDialog();
                    }
                }, rightListener);
    }

    /**
     * @param context
     * @param msg           提示信息
     * @param rightListener 确定按钮监听
     */
//    public static void showCommonDialog(Context context, String msg, View.OnClickListener rightListener) {
//        showCommonDialog(
//                context
//                , context.getString(R.string.common_tip)
//                , msg
//                , context.getString(R.string.common_cancel)
//                , context.getString(R.string.common_confirm), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PromptUtil.closeCommonDialog();
//                    }
//                }, rightListener);
//    }

    /**
     * 只是显示错误的提示信息
     *
     * @param context
     * @param msg     提示信息
     */
//    public static void showCommonDialog(Context context, String msg) {
//        showCommonDialog(
//                context
//                , context.getString(R.string.common_tip)
//                , msg
//                , ""
//                , context.getString(R.string.common_confirm), null, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PromptUtil.closeCommonDialog();
//                    }
//                });
//    }

//    public static void showDialog(Context context, String content) {
//            if (context != null) {
//                final Dialog mDialog = new Dialog(context);
//                mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                mDialog.setCancelable(true);
//                LayoutInflater inflater = LayoutInflater.from(context);
//                View contentView = inflater.inflate(R.layout.view_custom_dialog, null);
//                TextView titleTxtv = (TextView) contentView.findViewById(R.id.tv_title);
//                TextView info = (TextView) contentView.findViewById(R.id.tv_info);
//                AppCompatButton cancel = (AppCompatButton) contentView.findViewById(R.id.tv_cancel_dialog);
//                AppCompatButton confirm = (AppCompatButton) contentView.findViewById(R.id.tv_confirm_dialog);
//                cancel.setVisibility(View.GONE);
//                titleTxtv.setText(context.getString(R.string.common_tip));
//                info.setText(content);
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mDialog.dismiss();
//                    }
//                });
//                mDialog.setContentView(contentView);
//                mDialog.show();
//            }
//    }

    /***
     * 新的全屏透明的progress
     *
     * @param context
     * @param cancelAble
     */
    public static void showTransparentProgress(Context context, boolean cancelAble) {
        try {
            closeTransparentDialog();
            transparentProgressDialog = new Dialog(context, R.style.Transparent);
            transparentProgressDialog.setContentView(R.layout.view_transparent_dialog);
            transparentProgressDialog.setCancelable(cancelAble);
            transparentProgressDialog.show();
        }catch (Exception e){

        }

    }

    public static void closeTransparentDialog() {
        try {
            if (transparentProgressDialog != null && transparentProgressDialog.isShowing()) {
                transparentProgressDialog.dismiss();
                transparentProgressDialog = null;
            }
        }catch (Exception e){}

    }

    public static void closeCommonDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }catch (Exception e){}

    }


    /**
     * 显示异常提示
     * @param context
     * @param msg
     */
    public static void showErrorDialog(Context context, String msg) {
        new AlertDialog.Builder(context)//
                .setIcon(R.mipmap.ic_launcher)//
                .setTitle(R.string.app_name)//
                .setMessage(msg)//
                .setNegativeButton("ok", null)//
                .show();
    }

    private static final boolean isShow = true;

    /**
     * 测试:true
     * 在正式投入市场：false
     *
     * @param context
     * @param msg
     */
    public static void showToastTest(Context context, String msg) {
        if (isShow) {
            ToastUtil.showLongToast(context, msg);
        }
    }

    /**
     * 测试:true
     * 在正式投入市场：false
     *
     * @param tag
     * @param msg
     */
    public static void showLogTest(String tag, String msg) {
        if (isShow) {
            Log.i(tag, msg);
        }
    }

    /**
     * 网络访问成功后返回值通用错误验证
     * @param retValue
     * @return true返回值正常，false返回值异常
     */
//    public static boolean retValueVerify(Context context, String retValue) {
//        boolean result = false;
//        if (!TextUtils.isEmpty(retValue)) {
//            BaseVO baseVO = JSON.parseObject(retValue, BaseVO.class);
//            if (baseVO != null) {
//                if (baseVO.getCode() == 0) {
//                    result = true;
//                } else {
//                    ToastUtil.showShortToast(context, baseVO.getMessage());//逻辑上的错误，接口提供错误提示
//                }
//            } else {
//                ToastUtil.showShortToast(context, "数据解析失败");//数据解析失败
//            }
//        } else {
//            ToastUtil.showShortToast(context, "返回数据为空");//返回数据为空
//        }
//        return result;
//    }

    /**
     * 显示版本更新dialog
     * @param context
     * @param version
     */
//    public static void showVersionDialog(final Context context, Version version){
//        closeCommonDialog();
//        showVersionDialog(context,version,false);
//    }
    /**
     * 显示版本更新dialog
     * @param context
     * @param version
     * @param ignoreUserCancel 考虑用户已点击取消的情况 true：忽略  false：不忽略
     */
//    public static void showVersionDialog(final Context context, final Version version, boolean ignoreUserCancel) {
//        closeCommonDialog();
//        final File apkFile = new File(Environment.getExternalStorageDirectory() + "/download/Etrip.apk");
//        final Version localVersion = SharePreUtil.getVersion(context);
//        if (version == null)
//            return;
//        dialog = new Dialog(context, R.style.Transparent);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View view = LayoutInflater.from(context).inflate(R.layout.view_upload_dialog, null);
//        ImageView btCancel = (ImageView) view.findViewById(R.id.tv_cancel_dialog);
//        AppCompatButton btConfirm = (AppCompatButton) view.findViewById(R.id.tv_confirm_dialog);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
//        tvInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
////        btCancel.setText(R.string.common_cancel);
//        if (SharePreUtil.isDownLoadApk(context).equals(version.getEditionNo()) && apkFile.exists()) {
//            btConfirm.setText("安装");
//            tvInfo.setText(version.getEditionContent() + "(已下载完成)");
//        } else {
//            btConfirm.setText("立即升级");
//            tvInfo.setText(version.getEditionContent());
//        }
//        if (version.getUpdateType() == Version.UPDATE_FORCE) {
//            dialog.setCancelable(false);
//            btCancel.setVisibility(View.INVISIBLE);
//        }
//        btCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Global.updateCanceled = true;
//                PromptUtil.closeCommonDialog();
//            }
//        });
//        btConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (SharePreUtil.isDownLoadApk(context).equals(version.getEditionNo()) && apkFile.exists()) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    //判断是否是AndroidN以及更高的版本
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
//                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//                    } else {
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                    }
//                    context.startActivity(intent);
//                } else {
//                    downloadApk(context, version.getDownLoadPath(), version.getEditionContent(), context.getString(R.string.app_name));
//                    PromptUtil.closeCommonDialog();
//                    InstallReceiver myReceiver = new InstallReceiver();
//                    String ACTION = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
//                    IntentFilter filter = new IntentFilter();
//                    filter.addAction(ACTION);
//                    filter.setPriority(Integer.MAX_VALUE);
//                    MyApplication.getAppContext().registerReceiver(myReceiver, filter);
//                }
//
//            }
//        });
//        dialog.setContentView(view);
//        tvTitle.setText(version.getEditionTitle());
//        dialog.show();
//    }

}
