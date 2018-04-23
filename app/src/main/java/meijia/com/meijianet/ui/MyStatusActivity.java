package meijia.com.meijianet.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import meijia.com.meijianet.R;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class MyStatusActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvStatus;
    private TextView tvCreat;
    private TextView tvLogin;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_my_status);
        StatusBarUtils.setStatusBarColor(this,getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("我的状态");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        tvCreat = (TextView) findViewById(R.id.tv_ac_status_creattime);
        tvLogin = (TextView) findViewById(R.id.tv_ac_status_logintime);
        tvStatus = (TextView) findViewById(R.id.tv_ac_status);
    }

    @Override
    protected void initData() {
        getStatusByNet();
    }


    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }

    private void getStatusByNet() {
        if (!NetworkUtil.checkNet(MyStatusActivity.this)){
            ToastUtil.showShortToast(MyStatusActivity.this,"没有网了，请检查网络");
            return;
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+MY_STATUS)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            JSONObject object = new JSONObject(body);
                            String rigestTime = object.getString("createTime");
                            String loginTime = object.getString("loginDate");
                            int status = object.getInt("state");
                            tvCreat.setText("注册时间："+rigestTime);
                            tvLogin.setText("登录时间："+loginTime);
                            String text = "";
                            if (status == 0){
                                text = "待审核";
                            }else if (status == 1){
                                text = "通过";
                            }else if (status == -1){
                                text = "未通过";
                            }
                            tvStatus.setText("审核状态："+text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }
}
