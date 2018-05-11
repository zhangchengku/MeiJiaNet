package meijia.com.meijianet.ui;

import android.content.Context;
import android.support.v7.widget.Toolbar;
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
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class UpdatePswActivity extends BaseActivity implements TextView.OnEditorActionListener {

    private TextView tvTitle;
    private EditText etName;
    private ImageView ivDelete;
    private EditText etNewPsw;
    private ImageView ivNewPsw;
    private EditText etNewPswSure;
    private ImageView ivNewPswSure;
    private TextView tvComplete;
    private LinearLayout llParent;

    @Override
    protected void setContent() {
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_update_psw);

    }

    @Override
    protected void initView() {
        llParent = (LinearLayout) findViewById(R.id.activity_update_name);
        tvComplete = (TextView) findViewById(R.id.tv_ac_name_complete);
        etName = (EditText) findViewById(R.id.et_ac_name);
        ivDelete = (ImageView) findViewById(R.id.iv_ac_name_delete);
        etNewPsw = (EditText) findViewById(R.id.et_ac_psw_new);
        ivNewPsw = (ImageView) findViewById(R.id.iv_ac_name_delete_new);
        etNewPswSure = (EditText) findViewById(R.id.et_ac_psw_new_sure);
        ivNewPswSure = (ImageView) findViewById(R.id.iv_ac_name_delete_new_sure);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("修改密码");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

    @Override
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(UpdatePswActivity.this), 0, 0);
            }
        });
        ToolUtil.setInputListener(etName,ivDelete);
        ToolUtil.setInputListener(etNewPsw,ivNewPsw);
        ToolUtil.setInputListener(etNewPswSure,ivNewPswSure);
    }

    @Override
    protected void initClick() {
        tvComplete.setOnClickListener(this);
        etNewPswSure.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        updataPaw();
    }

    private void updataPaw() {
        if (!NetworkUtil.checkNet(UpdatePswActivity.this)){
            ToastUtil.showShortToast(UpdatePswActivity.this,"没有网了，请检查网络");
            return;
        }
        String oldPsw = etName.getText().toString().trim();
        String newPsw = etNewPsw.getText().toString().trim();
        String newPswSure = etNewPswSure.getText().toString().trim();
        if (oldPsw.equals("") || newPsw.equals("") || newPswSure.equals("")){
            ToastUtil.showShortToast(UpdatePswActivity.this,"请将信息填写完整");
            return;
        }
        if (!newPsw.equals(newPswSure)){
            ToastUtil.showShortToast(UpdatePswActivity.this,"请确保两次输入的密码一致");
            return;
        }
        PromptUtil.showTransparentProgress(UpdatePswActivity.this,false);
        RequestParams params = new RequestParams();
        params.add("phone", SharePreUtil.getUserInfo(UpdatePswActivity.this).getPhone());
        params.add("oldPassword",oldPsw);
        params.add("password",newPsw);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+UPDATA_PSW)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(UpdatePswActivity.this,"密码修改成功");
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(UpdatePswActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                        ToastUtil.showShortToast(UpdatePswActivity.this,"密码修改失败，请稍后重试");
                        finish();
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
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            // 对应逻辑操作
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            updataPaw();
            return true;
        }
        return false;
    }
}
