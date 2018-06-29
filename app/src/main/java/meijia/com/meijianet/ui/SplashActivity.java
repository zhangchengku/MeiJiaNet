package meijia.com.meijianet.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import meijia.com.meijianet.R;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.BaseVO;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.fragment.VideoVo;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;
import okhttp3.Call;
import okhttp3.Response;


/**
 * @desc 启动屏
 * Created by devilwwj on 16/1/23.
 */
public class SplashActivity extends BaseActivity {
    /***
     * 默认广告停留时间为3秒
     */
    private static int COUNTDOWN_TIME = 3;
    private ImageView guideImage;
    private ImageView ivsplash;
    private TextView countdowntext;
    private Timer timer;

    @Override
    protected void setContent() {
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.act_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);

    }

    private void getadvertisement() {
        if (!NetworkUtil.checkNet(SplashActivity.this)){
            return;
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+ADVERTISEMENT)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        AdverVo adverVo = JSON.parseObject(body, AdverVo.class);
                        if(adverVo.getStartStatus()==1){
                           String pic =  adverVo.getAdvertisement();
                            String url =  adverVo.getRedirectUrl();
                            setAD_Img(pic,url);
                        }else {
                            Intent intent = new Intent(SplashActivity.this, ContentActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(SplashActivity.this,returnTip);

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    @Override
    protected void initView() {
        guideImage=(ImageView)findViewById(R.id.guideImage);
        ivsplash=(ImageView)findViewById(R.id.iv_splash);
        countdowntext=(TextView)findViewById(R.id.countdown_text);


    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.setStatusBarFontDark(SplashActivity.this,true);
            StatusBarUtils.setStatusBarColor(SplashActivity.this, getResources().getColor(R.color.white));
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
    }

    @Override
    protected void initClick() {

    }


    private void enterHomeActivity() {
        LoginVo userInfo = SharePreUtil.getUserInfo(this);
        if (!userInfo.getUuid().equals("")){
            autoLogin(userInfo.getUuid());
        }
        getadvertisement();

    }
    private void autoLogin(String uuid) {
        if (!NetworkUtil.checkNet(SplashActivity.this)){
            return;
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+AUTO_LOGIN)
                .addParams("uuid",uuid)
                .build()
                .execute(new Callback() {

                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String result = response.body().string();
                        BaseVO baseVO = JSON.parseObject(result, BaseVO.class);
                        if (baseVO.getCode().equals("fail") ) {
                            LoginVo vo = new LoginVo();
                            vo.setName("");
                            vo.setHeader("");
                            vo.setUuid("");
                            SharePreUtil.setUserInfo(SplashActivity.this,vo);
                            EventBus.getDefault().post("logout");
                        }else {
                            LoginVo vo = JSON.parseObject(baseVO.getData(), LoginVo.class);
                            vo.setUuid(uuid);
                            SharePreUtil.setUserInfo(SplashActivity.this,vo);

                        }
                        return result;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LoginVo vo = new LoginVo();
                        vo.setName("");
                        vo.setHeader("");
                        vo.setUuid("");
                        SharePreUtil.setUserInfo(SplashActivity.this,vo);
                        EventBus.getDefault().post("logout");
                    }
                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }
    private void setAD_Img(String imgUrl, final String imgAddress) {

        Picasso.with(SplashActivity.this).load(imgUrl)
                .into(guideImage);
        guideImage.setVisibility(View.VISIBLE);
        ivsplash.setVisibility(View.GONE);
        countdowntext.setVisibility(View.VISIBLE);
        countdowntext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, ContentActivity.class);
                startActivity(intent);
                finish();
                if (timer != null)
                    timer.cancel();
            }
        });
        guideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgAddress.length() < 1) return;
//                StateMessage.isGDMessahe = true;
                Intent intent = new Intent(SplashActivity.this, WebViewActivity4.class);
                intent.putExtra("url","http://"+imgAddress);
                Log.d("asdfasdfasd", "          "+"http://"+imgAddress);
                startActivity(intent);
                if (timer != null)
                    timer.cancel();
            }
        });
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            COUNTDOWN_TIME--;
            if (COUNTDOWN_TIME <= 0) {
                timer.cancel();
                Intent intent = new Intent(SplashActivity.this, ContentActivity.class);
                startActivity(intent);
                finish();
            }
            countdowntext.setText(COUNTDOWN_TIME + " 跳过");
        }
    };
    @Override
    public void onClick(View v) {

    }
}