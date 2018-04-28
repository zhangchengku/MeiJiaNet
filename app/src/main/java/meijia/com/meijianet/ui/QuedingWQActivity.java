package meijia.com.meijianet.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/4/26.
 */
public class QuedingWQActivity extends BaseActivity {

    private LinearLayout llContent;
    private ImageView ivFinish;
    private EditText onepassword;
    private EditText onepasswords;
    private TextView tvSure;
    private String style;
    private String userId;
    private String wxUserId;
    private String qqUserId;
    private String phone;

    @Override
    protected void setContent() {
        setContentView(R.layout.layout_quedingwq);
        StatusBarUtils.setActivityTranslucent(this);
    }

    @Override
    protected void initView() {
        llContent = (LinearLayout) findViewById(R.id.activity_rigest);
        ivFinish = (ImageView) findViewById(R.id.iv_ac_regist_finish);
        onepassword = (EditText) findViewById(R.id.et_ac_forget_psw);
        onepasswords = (EditText) findViewById(R.id.et_ac_forget_psws);
        tvSure = (TextView) findViewById(R.id.tv_ac_forget_complete);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        style = intent.getStringExtra("style");
        userId = intent.getStringExtra("UserId");
        phone = intent.getStringExtra("phone");

        if(style.equals("1")){
            wxUserId = userId;
            qqUserId = "";
        }
        if(style.equals("2")){
            wxUserId = "";
            qqUserId = userId;
        }
    }

    @Override
    protected void initClick() {
        ivFinish.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_ac_regist_finish:
                    finish();
                    break;
                case R.id.tv_ac_forget_complete:
                    String password = onepassword.getText().toString().trim();
                    String passwords = onepasswords.getText().toString().trim();
                    if (password.equals("") || passwords.equals("")){
                        ToastUtil.showShortToast(QuedingWQActivity.this,"密码不能为空");
                        return;
                    }
                    if (!password.equals(passwords) ){
                    ToastUtil.showShortToast(QuedingWQActivity.this,"请输入相同的密码");
                    return;
                }
                    rigest(passwords);
                    break;
                default:
                    break;
            }
        }
    }

    private void rigest(String passwords) {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("password",passwords);
        params.add("wxUserId",wxUserId);
        params.add("qqUserId",qqUserId);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CHECK_PHONE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        ToastUtil.showShortToast(QuedingWQActivity.this,"登陆成功");


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
}
