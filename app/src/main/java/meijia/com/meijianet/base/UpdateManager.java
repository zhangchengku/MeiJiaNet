 package meijia.com.meijianet.base;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.content.FileProvider;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Toast;

        import com.alibaba.fastjson.JSON;
        import com.zhy.http.okhttp.OkHttpUtils;
        import com.zhy.http.okhttp.callback.Callback;

        import org.greenrobot.eventbus.EventBus;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;

        import meijia.com.meijianet.R;
        import meijia.com.meijianet.api.ResultCallBack;
        import meijia.com.meijianet.bean.BannerVo;
        import meijia.com.meijianet.bean.BaseVO;
        import meijia.com.meijianet.bean.LoginVo;
        import meijia.com.meijianet.ui.AppConstants;
        import meijia.com.meijianet.ui.SplashActivity;
        import meijia.com.meijianet.util.NetworkUtil;
        import meijia.com.meijianet.util.SharePreUtil;
        import okhttp3.Call;
        import okhttp3.Response;
        import static meijia.com.meijianet.api.URL.VERSION_CODE;

 /**
 * Created by Administrator on 2018/5/7.
 */

public class UpdateManager {
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
//    Map<String, String> versionMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Context mContext;
    /* 更新进度条 */
    private HorizontalProgressBarWithNumber mProgress;
    private Dialog mDownloadDialog;
    private int currentVersionCode;
    private int serverVersionCode;
    private int minVersionCode;
    private String currentVersionName;
    private String serverVersionName;
    private String minVersionName;
    private String updateMessage;
    private int updateSign = 0;
    private String source;//login,setting

    private AppVersionBO appVersionBO;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateManager(Context context, String source) {
        this.source = source;
        this.mContext = context;
    }

    class UpdateThread extends Thread {
        public void run() {
//            updateSign = isUpdate();
//            updateHandler.sendEmptyMessage(0);
            isUpdate();
        }
    }

    private Handler updateHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (updateSign == 1) {//可选择更新
                showUpdateDialog();
            } else if (updateSign == 2) {//必须更新
                showMustUpdateDialog();
            } else {//无需更新
                if (source.equals("setting")) {
                    showNoUpdateDialog();
                }
            }

        }
    };

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        new UpdateThread().start();
        Log.e("版本更新，", "dddd");
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
//    private int isUpdate() {
//        // 获取当前软件版本
//        int sign = 0;
//        getVersionCode(mContext);
//        String path = LocalConfiguration.UPDATEVERSION; //更新的URl
//        try {
//        /*	HttpPost httpPost = new HttpPost(url);
//            DefaultHttpClient httpclient=new DefaultHttpClient();
//			HttpConnectionParams.setConnectionTimeout(httpPost.getParams(), 5000);
//			HttpConnectionParams.setSoTimeout(httpPost.getParams(), 5000); //设置请求超时
//			HttpResponse httpResponse =httpclient.execute(httpPost);*/
//
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setDoInput(true); //允许输入流，即允许下载
//            conn.setDoOutput(true); //允许输出流，即允许上传
//            conn.setRequestMethod("POST"); //使用get请求
//            // Post 请求不能使用缓存
//            conn.setUseCaches(false);
////            if (conn.getResponseCode() != 200) return sign;
//            InputStream is = conn.getInputStream();
//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            VersionXMLHandler handler = new VersionXMLHandler();
//            parser.parse(is, handler);
//            versionMap = handler.getVersionMap();
//            if (null != versionMap) {
//                serverVersionCode = Integer.valueOf(versionMap.get("versionCode"));
//                serverVersionName = versionMap.get("versionName");
//                updateMessage = versionMap.get("updateMessage");
//                minVersionCode = Integer.valueOf(versionMap.get("minVersionCode"));
//                minVersionName = versionMap.get("minVersionName");
//                // 版本判断
//                if (serverVersionCode > currentVersionCode) {
//                    sign = 1;
//                }
//                if (minVersionCode > currentVersionCode) {
//                    sign = 2;
//                }
//            }
//
//
//        } catch (UnknownHostException e) {
//            Message message = new Message();
//            Bundle data = new Bundle();
//            data.putString("message", "连接更新服务器出错！");
//            message.setData(data);
//            messageHandler.sendMessage(message);
//        } catch (ConnectTimeoutException e) {
//            Message message = new Message();
//            Bundle data = new Bundle();
//            data.putString("message", "连接更新服务器超时！");
//            message.setData(data);
//            messageHandler.sendMessage(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sign;
//    }

    /**
     * 检测是否有版本更新
     */
    private void isUpdate() {
        // 获取当前软件版本
        getVersionCode(mContext);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + VERSION_CODE)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        appVersionBO = JSON.parseObject(body, AppVersionBO.class);
                        serverVersionCode = Integer.valueOf(appVersionBO.getAndroid().getVersionCode());
                        serverVersionName = appVersionBO.getAndroid().getVersionName();
                        updateMessage = appVersionBO.getAndroid().getUpdateMessage();
                        minVersionCode = Integer.valueOf(appVersionBO.getAndroid().getMinVersionCode());
                        minVersionName = appVersionBO.getAndroid().getMinVersionName();
                        // 版本判断
                        if (serverVersionCode > currentVersionCode) {//可选择更新
                            showUpdateDialog();
                            return;
                        }
                        if (minVersionCode > currentVersionCode) {//必须更新
                            showMustUpdateDialog();
                            return;
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });


    }


    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast toast = Toast.makeText(mContext, msg.getData().getString("message"), Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private void getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packInfo.versionCode;
            currentVersionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示软件更新对话框
     */
    private void showUpdateDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        StringBuilder message = new StringBuilder();
        message.append("当前版本:");
        message.append(currentVersionName);
        message.append(" 最新版本:");
        message.append(serverVersionName);
        message.append("\n新特性:\n");
        message.append(updateMessage);
        builder.setMessage(message.toString());
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("更新",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
        // 稍后更新
        builder.setNegativeButton("下次再说",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件更新对话框
     */
    private void showMustUpdateDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        StringBuilder message = new StringBuilder();
        message.append("当前版本过低，必须升级才能继续使用！");
        builder.setMessage(message.toString());
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("更新",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 显示下载对话框
                        showDownloadDialog();
                    }
                });
    /*	// 稍后更新
        builder.setNegativeButton("退出",
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity)mContext).finish();
					}
				});*/
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 无需更新对话框
     */
    private void showNoUpdateDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("版本检测");
        StringBuilder message = new StringBuilder();
        message.append("当前版本:");
        message.append(currentVersionName);
        message.append("，已是最新，无需更新！");
        builder.setMessage(message.toString());
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (HorizontalProgressBarWithNumber) v.findViewById(R.id.update_progress);
        builder.setView(v);
        builder.setCancelable(false);
        // 取消更新
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // 设置取消状态
                        cancelUpdate = true;
                /*if(updateSign==2){
                    ((Activity)mContext).finish();
				}*/
                    }
                });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     *
     * @author shichenwei
     * @date 2012-4-26
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
                    mSavePath = sdDir.toString() + "/meijianet";
                } else {
                    mSavePath = AppConstants.DOWNLOAD_PATH; //手机内存位置
                }
                URL url = new URL(appVersionBO.getAndroid().getUrl());
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();
                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, AppConstants.appFileName);
                //修改权限，使系统的intent可以去安装
                String[] command = {"chmod", "777", mSavePath + "/" + AppConstants.appFileName};
                String[] command2 = {"chmod", "777", mSavePath};
                ProcessBuilder builder = new ProcessBuilder(command);
                ProcessBuilder builder2 = new ProcessBuilder(command2);
                try {
                    builder.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numread <= 0) {
                        //下载完成后修改已下载文件的读写权限
                        builder2.start();
                        mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                } while (!cancelUpdate);// 点击取消就停止下载.
                fos.close();
                is.close();
//				}
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }

    /**
     * 安装 apk 文件
     */
    private void installApk() {
        Log.d("开始下载", "installApk: ");
        File apkfile = new File(mSavePath, AppConstants.appFileName);
        if (!apkfile.exists()) {
            Log.d("开始下载", "installApk: 1111111");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, "meijia.com.meijianet.fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            Log.d("开始下载", "installApk: 222222222222");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("开始下载", "installApk:33333333333 ");
        }
        if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            mContext.startActivity(intent);
            Log.d("开始下载", "installApk: 44444444444");
        }
    }
}
