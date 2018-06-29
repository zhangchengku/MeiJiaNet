package meijia.com.meijianet.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.AliPayVO;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.dialog.SharePopupWindow;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/5/21.
 */

public class WebViewActivity3 extends BaseActivity {

    private TextView tvTitle;
    private WebView mWebView;
    private long id;
    private List<AliPayVO> newHouseInfos;
    private LinearLayout linear;
    private SharePopupWindow popWindow;
    private int REQUEST_CODE=6;
//    private Button startselling;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_webview);

    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getIntent().getStringExtra("istatle"));
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        linear = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);
//        startselling = (Button) findViewById(R.id.startselling);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(WebViewActivity3.this,true);
            StatusBarUtils.setStatusBarColor(WebViewActivity3.this, getResources().getColor(R.color.white));
            linear.setPadding(0, BubbleUtils.getStatusBarHeight(WebViewActivity3.this), 0, 0);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(WebViewActivity3.this,true);
            StatusBarUtils.setStatusBarColor(WebViewActivity3.this, getResources().getColor(R.color.color_black60));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

        popWindow = new SharePopupWindow(this);
        String url = getIntent().getStringExtra("url");
        Log.e(TAG, "initData: "+url);
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
        mWebView.addJavascriptInterface(new WebViewActivity3.JsInterface(),"Android");
    }

    public class JsInterface {

        @JavascriptInterface
        public void buyagree(String buyagree) {

        }
    }






    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }
}



