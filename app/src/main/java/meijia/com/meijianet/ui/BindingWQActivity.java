package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/4/20.
 */

public class BindingWQActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private LinearLayout llContent;
    private ImageView ivFinish;
    private EditText etPhone;
    private EditText etYanzheng;
    private EditText etPsw;
    private TextView tvSure;
    private TextView tvCode;
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
        etPsw = (EditText) findViewById(R.id.et_ac_forget_psw);
        tvSure = (TextView) findViewById(R.id.tv_ac_forget_complete);
        tvCode = (TextView) findViewById(R.id.tv_ac_forget_send);
    }

    @Override
    protected void initData() {
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
        etPsw.setOnEditorActionListener(this);
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
                    timer.start();
                    break;
                case R.id.iv_ac_regist_finish:
                    finish();
                    break;
                case R.id.tv_ac_forget_complete:
                    String phone = etPhone.getText().toString().trim();
                    String code = etYanzheng.getText().toString().trim();
                    String psw = etPsw.getText().toString().trim();
                    if (phone.equals("") || psw.equals("") || code.equals("")){
                        ToastUtil.showShortToast(BindingWQActivity.this,"手机号、密码或验证码不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone)){
                        ToastUtil.showShortToast(BindingWQActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    rigest(phone,code,psw);
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
        params.add("codetype","1");
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

    private void rigest(String phone, String code, String psw) {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("password",psw);
        params.add("smscode",code);
        params.add("codetype","1");
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + FORGEST_PSW)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        if (timer != null) {
                            timer.cancel();
                        }
                        ToastUtil.showShortToast(BindingWQActivity.this,"修改成功");
                        Intent intent = new Intent();
                        intent.putExtra("phone",phone);
                        setResult(2,intent);
                        finish();
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


        //判断是否是“DOWN”键
        if(actionId == EditorInfo.IME_ACTION_DONE){
            // 对应逻辑操作
            String psw = etPsw.getText().toString().trim();
            String code = etYanzheng.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(psw) || code.equals("") || phone.equals("")){
                ToastUtil.showShortToast(BindingWQActivity.this,"手机号、验证码或新密码不能为空");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            rigest(phone,code,psw);
            return true;
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

