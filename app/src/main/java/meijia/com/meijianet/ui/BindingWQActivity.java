package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.BaseVO;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BindingWQActivity extends BaseActivity  {


    private LinearLayout llContent;
    private ImageView ivFinish;
    private EditText etPhone;
    private EditText etYanzheng;
    private TextView tvSure;
    private TextView tvCode;
    private String style;
    private String userId;
    private String wxUserId;
    private String qqUserId;
    private String three;
    private String userIcon;
    private String userName;

    @Override
    protected void setContent() {
        setContentView(R.layout.layout_wq);
        StatusBarUtils.setActivityTranslucent(this);
    }

    @Override
    protected void initView() {
        llContent = (LinearLayout) findViewById(R.id.activity_rigest);
        ivFinish = (ImageView) findViewById(R.id.iv_ac_regist_finish);
        etPhone = (EditText) findViewById(R.id.et_ac_forget_phone);
        etYanzheng = (EditText) findViewById(R.id.et_ac_forget_yanzheng);
        tvSure = (TextView) findViewById(R.id.tv_ac_forget_complete);
        tvCode = (TextView) findViewById(R.id.tv_ac_forget_send);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        style = intent.getStringExtra("style");
        userId = intent.getStringExtra("userId");
        userIcon = intent.getStringExtra("wxHeader");
        userName = intent.getStringExtra("wxNickName");
        llContent.post(new Runnable() {
            @Override
            public void run() {
                llContent.setPadding(0, BubbleUtils.getStatusBarHeight(BindingWQActivity.this), 0, 0);
            }
        });
    }
    @Override
    protected void initClick() {
        ivFinish.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_forget_send:
                    String phone1 = etPhone.getText().toString().trim();
                    if (phone1.equals("")){
                        ToastUtil.showShortToast(BindingWQActivity.this,"手机号不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone1)){
                        ToastUtil.showShortToast(BindingWQActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    getSmsCode(phone1);
                    tvCode.setEnabled(false);
                    timer.start();
                    break;
                case R.id.iv_ac_regist_finish:
                    finish();
                    break;
                case R.id.tv_ac_forget_complete:
                    String phone = etPhone.getText().toString().trim();
                    String code = etYanzheng.getText().toString().trim();
                    if (phone.equals("") || code.equals("")){
                        ToastUtil.showShortToast(BindingWQActivity.this,"手机号、密码或验证码不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone)){
                        ToastUtil.showShortToast(BindingWQActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    rigest(phone,code);
                    break;
                default:
                    break;
            }
        }
    }
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每隔countDownInterval秒会回调一次onTick()方法
            tvCode.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            tvCode.setText("重新获取");
            tvCode.setEnabled(true);
        }
    };

    private void getSmsCode(String phone) {

        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("codetype","3");
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CODE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(BindingWQActivity.this,"验证码发送成功");
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(BindingWQActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void rigest(String phone, String code) {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("smscode",code);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CHECK_PHONE)
                .params(params.getMap())
                .build()
                .execute(new Callback() {

                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        if (timer != null) {
                            timer.cancel();
                        }
                        String result = response.body().string();
                        BaseVO baseVO = JSON.parseObject(result, BaseVO.class);
                        if (baseVO.getStatus()==1 ) {//已经注册
                         getlongininformtion(phone);
                        }else {//未注册
                            Intent intent=new Intent(BindingWQActivity.this,QuedingWQActivity.class);
                            intent.putExtra("wxHeader",userIcon);
                            intent.putExtra("wxNickName",userName);
                            intent.putExtra("style",style);
                            intent.putExtra("userId",userId);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                            finish();
                        }
                        return result;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
    }

    private void getlongininformtion(String phone) {
        if (style.equals("1")) {//微信
            three = "wxUserId";
        }
        if (style.equals("2")) {//QQ
            three = "qqUserId";
        }
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add(three,userId);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + BIND_REGIST)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        getlogding();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void getlogding() {

        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        if (style.equals("1")) {//微信
            params.add("wxUserId",userId);
            params.add("wxHeader",userIcon);
            params.add("wxNickName",userName);
        }
        if (style.equals("2")) {//QQ
            params.add("qqUserId",userId);
            params.add("qqHeader",userIcon);
            params.add("qqNickName",userName);
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN_QW)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(BindingWQActivity.this,"登录成功");
                        LoginVo vo = JSON.parseObject(body, LoginVo.class);
                        SharePreUtil.setUserInfo(BindingWQActivity.this,vo);
                        EventBus.getDefault().post("login");
                        goBackMain();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(BindingWQActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

