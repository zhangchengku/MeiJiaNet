package meijia.com.meijianet.ui;


import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.AliPayVO;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.bean.ShareBO;
import meijia.com.meijianet.bean.WechatPayVO;
import meijia.com.meijianet.dialog.ShareDialog;
import meijia.com.meijianet.dialog.SharePopupWindow;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.wxapi.WXUtils;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/5/3.
 */

public class AdvertisementWe extends BaseActivity {

    private TextView tvTitle;
    private WebView mWebView;
    private long id;
    private List<AliPayVO> newHouseInfos;
    private LinearLayout linear;
    private SharePopupWindow popWindow;
//    private Button startselling;

    @Override
    protected void setContent() {
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_webview);

    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getIntent().getStringExtra("istatle"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvertisementWe.this, ContentActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setNavigationHomeAsUp(true);
        linear = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);
//        startselling = (Button) findViewById(R.id.startselling);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(AdvertisementWe.this,true);
            linear.setPadding(0, BubbleUtils.getStatusBarHeight(AdvertisementWe.this), 0, 0);
            StatusBarUtils.setStatusBarColor(AdvertisementWe.this, getResources().getColor(R.color.white));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(AdvertisementWe.this,true);
            StatusBarUtils.setStatusBarColor(AdvertisementWe.this, getResources().getColor(R.color.color_black60));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
        popWindow = new SharePopupWindow(this);
        String url = getIntent().getStringExtra("url");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 不设置为true不显示图片
        webSettings.setBlockNetworkImage(false);
        // 下面两个设置是为了自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 网页加载完成关闭进度条
                if (newProgress == 100) {
                    PromptUtil.closeTransparentDialog();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        PromptUtil.showTransparentProgress(this, true);
        mWebView.loadUrl(url);
    }
    @Override
    protected void initClick() {
//        startselling.setOnClickListener(this);
    }
    //TransactionRecordActivity.class
    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.startselling:
//                startActivity(new Intent(WebViewActivity.this,PostHouseActivity.class));
//                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 在这里，拦截或者监听Android系统的返回键事件。
            // return将拦截。
            // 不做任何处理则默认交由Android系统处理。
            Intent intent = new Intent(AdvertisementWe.this, ContentActivity.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }
}
