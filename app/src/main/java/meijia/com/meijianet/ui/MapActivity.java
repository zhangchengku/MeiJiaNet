package meijia.com.meijianet.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class MapActivity extends BaseActivity {

    private TextView tvTitle;
    private WebView mWebView;
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_map);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("地图");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//
//    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("url");
        Log.e(TAG, "initData: "+url );
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
