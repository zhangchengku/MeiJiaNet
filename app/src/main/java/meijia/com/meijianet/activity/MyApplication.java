package meijia.com.meijianet.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import meijia.com.meijianet.wxapi.WXUtils;
import okhttp3.OkHttpClient;


/**
 * Created by Administrator on 2017/9/2.
 */

public class MyApplication extends Application {
    private static final float CORRECT_RATE = 1.0f;
    public static ArrayList<Activity> activityList;
    private static MyApplication application;
    public static String ImagePath;

    @Override
    public void onCreate() {
        super.onCreate();
        activityList = new ArrayList<>();
        application = this;
        //OkhttpUtils配置
        ShareSDK.initSDK(this);
        //初始化美恰客服
        initMQ();
        ZXingLibrary.initDisplayOpinion(this);
        //初始化微信支付
        WXUtils.registerWX(this);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApp()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20*1000L, TimeUnit.MILLISECONDS)
                .readTimeout(20*1000L,TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggerInterceptor("OkHttpUtils",true))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
    /***
     * 注册美恰客服
     */
    private void initMQ() {
        //正式  0d00707be69e8db7e1122e81890c7fde
        //测试  6ca56a57b4bb55c85218f6dbac0b47a1

        MQConfig.init(this, "4fa38cc3e75af7bd2b99c9e986baac64", new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
                Log.e("log--", "init success");
            }

            @Override
            public void onFailure(int code, String message) {
                Log.e("log--", "int failure");
            }
        });
    }

    //获取栈顶活动
    public static Activity getTopActivity(){
        if (activityList.isEmpty()){
            return null;
        }else {
            return activityList.get(activityList.size()-1);
        }
    }

    public static MyApplication getApp(){
        return application;
    }
}
