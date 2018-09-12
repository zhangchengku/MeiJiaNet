package meijia.com.meijianet.ui;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.AliPayVO;
import meijia.com.meijianet.bean.WechatPayVO;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.meijianet.wxapi.WXUtils;
import meijia.com.srdlibrary.commondialog.CommonDialog;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class PayActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvSure;
    private CommonDialog mDialog;
    private EditText etName,etPhone;

    private long mId;
    public static boolean isPay = false;
    private TextView sendss;
    private EditText cade;


    @Override
    protected void setContent() {
        setContentView(R.layout.activity_pay);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("预约看房");


        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        tvSure = (TextView) findViewById(R.id.tv_ac_pay_sure);
        etName = (EditText) findViewById(R.id.et_ac_pay_name);
        etPhone = (EditText) findViewById(R.id.et_ac_pay_phone);

        sendss = (TextView) findViewById(R.id.send_ss);
        cade = (EditText) findViewById(R.id.cade);

    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initClick() {
        tvSure.setOnClickListener(this);
        sendss.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_pay_sure:
                    getOrderNum(0);
                    break;
                case R.id.send_ss:
                    String phone1 = etPhone.getText().toString().trim();
                    if (phone1.equals("")){
                        ToastUtil.showShortToast(PayActivity.this,"手机号不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone1)){
                        ToastUtil.showShortToast(PayActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    getcode(phone1);
                    sendss.setEnabled(false);
                    sendss.setBackgroundColor(Color.parseColor("#999999"));
                    timer.start();

                    break;
                case R.id.tv_wx:
                    getOrderNum(0);
                    if (mDialog!=null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    break;
                case R.id.tv_ali:
                    getOrderNum(1);
                    if (mDialog!=null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
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
            sendss.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            sendss.setText("重新获取");
            sendss.setEnabled(true);
        }
    };
    private void getcode(String phone) {

        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("codetype","4");
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CODE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(PayActivity.this,"验证码发送成功");
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(PayActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void getOrderNum(int type) {
        if (!NetworkUtil.checkNet(PayActivity.this)){
            ToastUtil.showShortToast(PayActivity.this,"没有网了，请检查网络");
            return;
        }

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String cades = cade.getText().toString().trim();

        if (name.equals("") || phone.equals("")){
            ToastUtil.showShortToast(PayActivity.this,"请将信息填写完整");
            return;
        }
        if (!ToolUtil.isPhoneNumber(phone)){
            ToastUtil.showShortToast(PayActivity.this,"请输入正确的手机号码");
            return;
        }
        PromptUtil.showTransparentProgress(PayActivity.this,false);
        RequestParams params = new RequestParams();
        params.add("smscode",cades);
        params.add("houseId",getIntent().getStringExtra("houseId"));
        params.add("tel",phone);
        params.add("realName",name);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+PREDE_TERMINE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        WebViewActivity2.test_a.finish();//关闭上个界面
                        finish();//关闭本界面
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        PromptUtil.closeTransparentDialog();
                        ToastUtil.showShortToast(PayActivity.this,returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }







}
