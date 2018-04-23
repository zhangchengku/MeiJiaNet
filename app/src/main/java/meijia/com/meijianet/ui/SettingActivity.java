package meijia.com.meijianet.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class SettingActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvUploadPwd,tvMyStatus,tvLiucheng,tvLoginOut;
    private boolean isQQAuthorize;
    private boolean isWxAuthorize;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_setting);
        StatusBarUtils.setStatusBarColor(this,getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("设置");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        tvMsg = (TextView) findViewById(R.id.tv_ac_set_msg);
        tvUploadPwd = (TextView) findViewById(R.id.tv_ac_set_uploadpwd);
        tvMyStatus = (TextView) findViewById(R.id.tv_ac_set_mystatus);
        tvLiucheng = (TextView) findViewById(R.id.tv_ac_set_liucheng);
        tvLoginOut = (TextView) findViewById(R.id.tv_ac_set_loginout);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {
        tvMsg.setOnClickListener(this);
        tvUploadPwd.setOnClickListener(this);
        tvMyStatus.setOnClickListener(this);
        tvLiucheng.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_set_msg://个人资料
                    startActivity(new Intent(this, PersonCenterActivity.class));
                    break;
                case R.id.tv_ac_set_uploadpwd://修改密码
                    startActivity(new Intent(this, UpdatePswActivity.class));
                    break;
                case R.id.tv_ac_set_mystatus://我的状态
                    startActivity(new Intent(this, MyStatusActivity.class));
                    break;
                case R.id.tv_ac_set_liucheng://交易流程
                    startActivity(new Intent(this, ProcessActivity.class));
                    break;
                case R.id.tv_ac_set_loginout://退出
                    appout();
                    break;

            }
        }
    }

    private void appout() {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN_OUT)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
//                        ToastUtil.showShortToast(SettingActivity.this,"退出成功");
                        LoginVo vo = new LoginVo();
                        vo.setName("");
                        vo.setHeader("");
                        vo.setUuid("");
                        SharePreUtil.setUserInfo(SettingActivity.this,vo);
                        EventBus.getDefault().post("logout");
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(SettingActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
