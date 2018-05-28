package meijia.com.meijianet.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class UpdateNameActivity extends BaseActivity {

    private TextView tvTitle;
    private EditText etName;
    private ImageView ivDelete;
    private TextView tvComplete;
    private LinearLayout llParent;

    @Override
    protected void setContent() {

        setContentView(R.layout.activity_update_name);
    }

    @Override
    protected void initView() {
        llParent = (LinearLayout) findViewById(R.id.activity_update_name);

        tvComplete = (TextView) findViewById(R.id.tv_ac_name_complete);
        etName = (EditText) findViewById(R.id.et_ac_name);
        ivDelete = (ImageView) findViewById(R.id.iv_ac_name_delete);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("设置姓名");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }
    public boolean isEmoji(String string) {
        @SuppressLint("WrongConstant")
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(UpdateNameActivity.this,true);
            StatusBarUtils.setStatusBarColor(UpdateNameActivity.this, getResources().getColor(R.color.white));
            llParent.setPadding(0, BubbleUtils.getStatusBarHeight(UpdateNameActivity.this), 0, 0);
//            llParent.post(new Runnable() {
//                @Override
//                public void run() {
//                    StatusBarUtils.setStatusBarFontDark(UpdateNameActivity.this,true);
//                    StatusBarUtils.setStatusBarColor(UpdateNameActivity.this, getResources().getColor(R.color.white));
//                    llParent.setPadding(0, BubbleUtils.getStatusBarHeight(UpdateNameActivity.this), 0, 0);
//                }
//            });
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(UpdateNameActivity.this,true);
            StatusBarUtils.setStatusBarColor(UpdateNameActivity.this, getResources().getColor(R.color.color_black60));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }


        ToolUtil.setInputListener(etName,ivDelete);
    }

    @Override
    protected void initClick() {
        tvComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String trim = etName.getText().toString().trim();
        if(isEmoji(trim)==true){
            ToastUtil.showShortToast(UpdateNameActivity.this,"请不要输入表情");
            return;
        }
        if (trim.equals("")){
            ToastUtil.showShortToast(UpdateNameActivity.this,"修改的名称不能为空");
            return;
        }
        updateMsg("name",trim);
    }

    private void updateMsg(String params,String values){
        if (!NetworkUtil.checkNet(UpdateNameActivity.this)){
            ToastUtil.showShortToast(UpdateNameActivity.this,"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(UpdateNameActivity.this,false);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL+UPDATE_MSG)
                .addParams(params,values)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        LoginVo vo = SharePreUtil.getUserInfo(UpdateNameActivity.this);
                        vo.setName(values);
                        SharePreUtil.setUserInfo(UpdateNameActivity.this,vo);
                        Intent intent = new Intent();
                        intent.putExtra("name",values);
                        setResult(104,intent);
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(UpdateNameActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
