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
    private String phone;
    private String three;

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
        llContent.post(new Runnable() {
            @Override
            public void run() {
                llContent.setPadding(0, BubbleUtils.getStatusBarHeight(QuedingWQActivity.this), 0, 0);
            }
        });
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
        if (style.equals("1")) {//微信
            three = "wxUserId";
        }
        if (style.equals("2")) {//QQ
            three = "qqUserId";
        }
        //检查网络
        if (!NetworkUtil.checkNet(this)) {
            ToastUtil.showShortToast(this, "没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this, false);
        RequestParams params = new RequestParams(this);
        params.add("phone", phone);
        params.add("password", passwords);
        params.add(three, userId);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + BING_NOT_REGIST)
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

                    }
                });
    }
    private void getlogding() {
        if (style .equals("1") ) {//微信
            three = "wxUserId";
        }
        if (style .equals("2")) {//QQ
            three = "qqUserId";
        }
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add(three,userId);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN_QW)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("", "onSuccess: body =登陆成功 " );
                        Intent show=new Intent(QuedingWQActivity.this,ContentActivity.class);
                        ToastUtil.showShortToast(QuedingWQActivity.this,"登录成功");
                        LoginVo vo = JSON.parseObject(body, LoginVo.class);
                        SharePreUtil.setUserInfo(QuedingWQActivity.this,vo);
                        EventBus.getDefault().post("login");
                        show.putExtra("grxx",1);
                        startActivity(show);
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        Log.e("asdfasdfa", "onSuccess: body =登陆成 " );
                        ToastUtil.showShortToast(QuedingWQActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        Log.e("asdfasdfa", "onSuccess: body =登陆 "+id );
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
