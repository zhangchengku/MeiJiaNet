package meijia.com.meijianet.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
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

public class YouxiangActivity extends BaseActivity {

    private TextView tvTitle;
    private EditText etName;
    private ImageView ivDelete;
    private TextView tvComplete;
    private LinearLayout llParent;

    @Override
    protected void setContent() {

        setContentView(R.layout.activity_update_youxiang);

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
        tvTitle.setText("邮箱");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setStatusBarFontDark(YouxiangActivity.this,true);
            StatusBarUtils.setStatusBarColor(YouxiangActivity.this, getResources().getColor(R.color.white));
            llParent.setPadding(0, BubbleUtils.getStatusBarHeight(YouxiangActivity.this), 0, 0);
//            llParent.post(new Runnable() {
//                @Override
//                public void run() {
//                    StatusBarUtils.setStatusBarFontDark(YouxiangActivity.this,true);
//                    StatusBarUtils.setStatusBarColor(YouxiangActivity.this, getResources().getColor(R.color.white));
//                    llParent.setPadding(0, BubbleUtils.getStatusBarHeight(YouxiangActivity.this), 0, 0);
//                }
//            });
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(YouxiangActivity.this,true);
            StatusBarUtils.setStatusBarColor(YouxiangActivity.this, getResources().getColor(R.color.color_black60));
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
        if (trim.equals("")){
            ToastUtil.showShortToast(YouxiangActivity.this,"修改的名称不能为空");
            return;
        }
        if( isEmail(trim)==true){
            updateMsg("email",trim);
        }else {
            ToastUtil.showShortToast(YouxiangActivity.this,"请输入正确邮箱");
        }

    }
    public static boolean isEmail(String email){
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
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
