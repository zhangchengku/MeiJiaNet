package meijia.com.meijianet.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class RigestActivity extends BaseActivity {
    private LinearLayout llContent;
    private ImageView ivFinish;
    private TextView tvDetail;
    private EditText etPhone;
    private EditText etYanzheng;
    private EditText etPsw;
    private TextView tvSure;
    private TextView tvSend;
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_rigest);
        StatusBarUtils.setActivityTranslucent(this);
    }

    @Override
    protected void initView() {
        llContent = (LinearLayout) findViewById(R.id.activity_rigest);
        ivFinish = (ImageView) findViewById(R.id.iv_ac_regist_finish);
        tvDetail = (TextView) findViewById(R.id.tv_ac_rigest_detail);
        etPhone = (EditText) findViewById(R.id.et_ac_regist_phone);
        etYanzheng = (EditText) findViewById(R.id.et_ac_regist_yanzheng);
        etPsw = (EditText) findViewById(R.id.et_ac_regist_psw);
        tvSure = (TextView) findViewById(R.id.tv_ac_rigest_login);
        tvSend = (TextView) findViewById(R.id.tv_ac_rigest_send);
    }

    @Override
    protected void initData() {
        setTextColorAndClick();
        llContent.post(new Runnable() {
            @Override
            public void run() {
                llContent.setPadding(0, BubbleUtils.getStatusBarHeight(RigestActivity.this), 0, 0);
            }
        });
    }

    @Override
    protected void initClick() {
        ivFinish.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        tvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ac_regist_finish:
                finish();
                break;
            case R.id.tv_ac_rigest_send:
                String phone1 = etPhone.getText().toString().trim();
                if (phone1.equals("")){
                    ToastUtil.showShortToast(RigestActivity.this,"手机号不能为空");
                    return;
                }
                if (!ToolUtil.isPhoneNumber(phone1)){
                    ToastUtil.showShortToast(RigestActivity.this,"请输入正确的手机格式");
                    return;
                }
                getSmsCode(phone1);
                timer.start();
                break;
            case R.id.tv_ac_rigest_login:
                String phone = etPhone.getText().toString().trim();
                String code = etYanzheng.getText().toString().trim();
                String psw = etPsw.getText().toString().trim();
                if (phone.equals("") || psw.equals("") || code.equals("")){
                    ToastUtil.showShortToast(RigestActivity.this,"手机号、密码或验证码不能为空");
                    return;
                }
                if (!ToolUtil.isPhoneNumber(phone)){
                    ToastUtil.showShortToast(RigestActivity.this,"请输入正确的手机格式");
                    return;
                }
                rigest(phone,code,psw);
                break;
            default:
                break;
        }
    }
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每隔countDownInterval秒会回调一次onTick()方法
            tvSend.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            tvSend.setText("重新获取");
            tvSend.setEnabled(true);
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
        params.add("codetype","0");
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CODE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(RigestActivity.this,"验证码发送成功");
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(RigestActivity.this,returnTip);
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
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + REGIST)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        if (timer != null) {
                            timer.cancel();
                        }
                        ToastUtil.showShortToast(RigestActivity.this,"注册成功");
                        Intent intent = new Intent();
                        intent.putExtra("phone",phone);
                        setResult(1,intent);
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(RigestActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void setTextColorAndClick() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s1 = tvDetail.getText().toString().trim();
        SpannableString sp1 = new SpannableString(s1);
        sp1.setSpan(new Clickable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast(RigestActivity.this,"000000");
            }
        }),10,s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan fcs1 = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_black60));
        sp1.setSpan(fcs1,10,s1.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(sp1);
        tvDetail.setText(builder);
        tvDetail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);    //去除超链接的下划线
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
