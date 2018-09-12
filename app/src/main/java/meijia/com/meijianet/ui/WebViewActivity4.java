package meijia.com.meijianet.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.api.PermissionListener;
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
public class WebViewActivity4  extends BaseActivity {

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
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        linear = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(WebViewActivity4.this,true);
            StatusBarUtils.setStatusBarColor(WebViewActivity4.this, getResources().getColor(R.color.white));
            linear.setPadding(0, BubbleUtils.getStatusBarHeight(WebViewActivity4.this), 0, 0);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(WebViewActivity4.this,true);
            StatusBarUtils.setStatusBarColor(WebViewActivity4.this, getResources().getColor(R.color.color_black60));
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
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTitle.setText(title);
            }
        });
        PromptUtil.showTransparentProgress(this, true);
        Log.d("H5", "initData: "+url);
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new WebViewActivity4.JsInterface(),"Android");

    }
    public class JsInterface {
        @JavascriptInterface
        public void scan(String msg) {
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    Log.d(TAG, "agreesdfads: ");
                    if(msg==null){

                    }else {
                        Intent intent = new Intent(WebViewActivity4.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                @Override
                public void onDenied(List<String> deniedPermission) {
                    PromptUtil.showCommonDialog(WebViewActivity4.this, "请在设置中打开内存卡读写权限", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    LoginVo userInfo = SharePreUtil.getUserInfo(WebViewActivity4.this);
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String str2=result.substring(0,24);
                    if(str2.equals("/api/touchGold/sweepCode")){
                        Log.d("二维码扫描", "onActivityResult: "+BaseURL.BASE_URL+result+"&uuid="+userInfo.getUuid());
                        Intent intent5 = new Intent(WebViewActivity4.this,WebViewActivity3.class);
                        intent5.putExtra("istatle", "摸金大赛");
                        intent5.putExtra("url", BaseURL.BASE_URL+result+"&uuid="+userInfo.getUuid());
                        startActivity(intent5);
                    }else {
                        Toast.makeText(this,"无法识别该二维码，请确认它是我们的活动二维码哦",Toast.LENGTH_SHORT).show();
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Log.d("二维码扫描失败", "onActivityResult: ");
                }
            }
        }
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if (mWebView.canGoBack())
            {
                mWebView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }else
            {
                Intent i=new Intent();
                setResult(4,i);
                finish();
                return true;
            }

        }
        return false;
    }
    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }
}


