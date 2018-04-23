package meijia.com.meijianet.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class YouxiangActivity extends BaseActivity {

    private TextView tvTitle;
    private EditText etName;
    private ImageView ivDelete;
    private TextView tvComplete;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_update_youxiang);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        tvComplete = (TextView) findViewById(R.id.tv_ac_name_complete);
        etName = (EditText) findViewById(R.id.et_ac_name);
        ivDelete = (ImageView) findViewById(R.id.iv_ac_name_delete);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("邮箱");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

    @Override
    protected void initData() {
        ToolUtil.setInputListener(etName,ivDelete);
    }

    @Override
    protected void initClick() {
        tvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String trim = etName.getText().toString().trim();
        if (trim.equals("")){
            ToastUtil.showShortToast(YouxiangActivity.this,"修改的名称不能为空");
            return;
        }

        updateMsg("email",trim);
    }

    private void updateMsg(String params,String values){
        if (!NetworkUtil.checkNet(YouxiangActivity.this)){
            ToastUtil.showShortToast(YouxiangActivity.this,"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(YouxiangActivity.this,false);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL+UPDATE_MSG)
                .addParams(params,values)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        LoginVo vo = SharePreUtil.getUserInfo(YouxiangActivity.this);
                        vo.setEmail(values);
                        SharePreUtil.setUserInfo(YouxiangActivity.this,vo);
                        Intent intent = new Intent();
                        intent.putExtra("email",values);
                        setResult(107,intent);
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(YouxiangActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
