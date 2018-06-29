package meijia.com.meijianet.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
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
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.wxapi.WXUtils;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/5/3.
 */

public class WebViewActivity extends BaseActivity {

    private TextView tvTitle;
    private WebView mWebView;
    private List<AliPayVO> newHouseInfos;

    private SharePopupWindow popWindow;
    private LinearLayout linear;
    private String url_more;
    private ImageView ivfinish;
//    private Button startselling;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_webview2);

    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        ivfinish = (ImageView) findViewById(R.id.iv_finish);

        tvTitle.setText(getIntent().getStringExtra("istatle"));

        linear = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(WebViewActivity.this,true);
            linear.setPadding(0, BubbleUtils.getStatusBarHeight(WebViewActivity.this), 0, 0);
            StatusBarUtils.setStatusBarColor(WebViewActivity.this, getResources().getColor(R.color.white));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(WebViewActivity.this,true);
            StatusBarUtils.setStatusBarColor(WebViewActivity.this, getResources().getColor(R.color.color_black60));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
        //支持javascript

     // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
     // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
      //扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        popWindow = new SharePopupWindow(this);

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
        LoginVo userInfo = SharePreUtil.getUserInfo(this);
        if (!userInfo.getUuid().equals("")){
            url_more =BaseURL.BASE_URL+"/api/house/houseDetail?id="+getIntent().getStringExtra("houseId")+"&uuid="+userInfo.getUuid();
        }else {
            url_more =BaseURL.BASE_URL+"/api/house/houseDetail?id="+getIntent().getStringExtra("houseId")+"&uuid="+"";
        }
        Log.d(TAG, "onRessadfasdume: "+url_more);
        mWebView.loadUrl(url_more);
        mWebView.addJavascriptInterface(new JsInterface(),"Android");
    }

    @Override
    protected void initData() {

    }

    public class JsInterface {
        //        @JavascriptInterface
//        public void pay(int type, String ordernumber) {
//            if (type == 0) {//微信支付
//                wechatPay(ordernumber);
//            } else {//支付宝支付
//                aliPay(ordernumber);
//            }
//        }
        @JavascriptInterface
        public void share(String shareUrl,String title,String desc,String imgUrl) {
            Log.d(TAG, "share: "+BaseURL.BASE_URL +shareUrl);
            popWindow.setShareMessage(title, imgUrl, desc, BaseURL.BASE_URL +shareUrl);
            mHandler.sendEmptyMessage(2);
        }
        @JavascriptInterface
        public void ask(String  zixun) {
            Log.d(TAG, "zixun: 咨询点击了");
            if (SharePreUtil.getUserInfo(WebViewActivity.this).getUuid().equals("")){
                startActivity(new Intent(WebViewActivity.this, LoginActivity.class));
                return;
            }
            LoginVo userInfo = SharePreUtil.getUserInfo(WebViewActivity.this);
            Intent intent = new MQIntentBuilder(WebViewActivity.this)
                    .setCustomizedId("de"+userInfo.getPhone()+"@dev.com") // 相同的 id 会被识别为同一个顾客
                    .build();
            startActivity(intent);
        }
        @JavascriptInterface
        public void tip(String msf) {
            Log.d(TAG, "zixsdfasdfasdfun: 咨询点击了"+msf);
            if(msf==null){
                Log.d(TAG, "zixsdfasdfa询点击了为空");
            }else if(Integer.valueOf(msf)==1){
                Log.d(TAG, "zixsdfasdfa询点击了1");
                Intent intent = new Intent(WebViewActivity.this,WebViewActivity2.class);
                intent.putExtra("istatle", "买家须知");
                intent.putExtra("houseId", getIntent().getStringExtra("houseId"));
                intent.putExtra("url", BaseURL.BASE_URL+"/api/buyerNotice");
                startActivity(intent);
            }else if(Integer.valueOf(msf)==0){
                Log.d(TAG, "zixsdfasdfa询点击了0");
                startActivity(new Intent(WebViewActivity.this, LoginActivity.class));
            }



        }

    }

    private void wechatPay(String orderNumber) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + WX_PAY)
                .addParams("orderNum", orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        WechatPayVO vo = JSON.parseObject(body, WechatPayVO.class);
                        WXUtils.payWX(vo);

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(WebViewActivity.this, returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }


    //支付宝支付
    private void aliPay(String orderNumber) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + ALI_PAY)
                .addParams("orderNum",orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        AliPayVO vo = JSON.parseObject(body, AliPayVO.class);
                        payAliPay(vo.getAlipay());
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(WebViewActivity.this, returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void payAliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(WebViewActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = 0x11;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * 支付宝返回结果处理
     * <p>
     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                popWindow.showAtLocation(linear, Gravity.BOTTOM, 0, 0);

            }else if(msg.what==0x11){
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(WebViewActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                    goBackMain();
                } else {
                    Toast.makeText(WebViewActivity.this, "支付失败！", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
    @Override
    protected void initClick() {
//        startselling.setOnClickListener(this);
        ivfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack())
                {
                    mWebView.goBack(); //goBack()表示返回WebView的上一页面

                }else
                {
                    finish();
                }
            }
        });
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            if (mWebView.canGoBack())
            {
                mWebView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }else
            {
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
