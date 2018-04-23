package meijia.com.meijianet.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;


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
