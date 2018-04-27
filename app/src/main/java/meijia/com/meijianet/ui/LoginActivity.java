package meijia.com.meijianet.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mob.tools.utils.UIHandler;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.MD5;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.commondialog.CommonDialog;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

import static android.R.attr.action;

public class LoginActivity extends BaseActivity implements TextView.OnEditorActionListener , PlatformActionListener ,Handler.Callback {
    private LinearLayout llContent;
    private LinearLayout ivFinish;
    private ImageView ivQQ;
    private ImageView ivWX;
    private EditText etPhone;
    private EditText etPsw;
    private TextView tvLogin;
    private TextView tvRegist;
    private TextView tvFogortPsw;
    private CommonDialog commonDialog;
    private int wqId = 0;
    private ProgressDialog progressDialog;
    private int MSG_ACTION_CCALLBACK=0;
    private  int style;
    private String userId;
    private String userName;
    private String userIcon;
    private String userGender;
    private String wxUserId;
    private String qqUserId;
    private String three;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_login);
        StatusBarUtils.setActivityTranslucent(this);
    }

    @Override
    protected void initView() {
        llContent = (LinearLayout) findViewById(R.id.activity_login);
        ivFinish = (LinearLayout) findViewById(R.id.iv_ac_login_finish);
        ivQQ = (ImageView) findViewById(R.id.iv_ac_login_qq);
        ivWX = (ImageView) findViewById(R.id.iv_ac_login_wx);
        etPhone = (EditText) findViewById(R.id.et_ac_login_phone);
        etPsw = (EditText) findViewById(R.id.et_ac_login_psw);
        tvLogin = (TextView) findViewById(R.id.tv_ac_login_login);
        tvRegist = (TextView) findViewById(R.id.tv_ac_login_regest);
        tvFogortPsw = (TextView) findViewById(R.id.tv_ac_login_fogortpsw);
    }

    @Override
    protected void initData() {
        llContent.post(new Runnable() {
            @Override
            public void run() {
                llContent.setPadding(0, BubbleUtils.getStatusBarHeight(LoginActivity.this), 0, 0);
            }
        });
    }

    @Override
    protected void initClick() {
        ivFinish.setOnClickListener(this);
        ivWX.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        tvFogortPsw.setOnClickListener(this);
        etPsw.setOnEditorActionListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_ac_login_finish:
                    finish();
                    break;
                case R.id.tv_ac_login_login:
                    String phone = etPhone.getText().toString().trim();
                    String psw = etPsw.getText().toString().trim();
                    if (phone.equals("") || psw.equals("")){
                        ToastUtil.showShortToast(LoginActivity.this,"手机号或密码不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone)){
                        ToastUtil.showShortToast(LoginActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    login(phone,psw);
                    break;
                case R.id.tv_ac_login_fogortpsw:
                    Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                    startActivityForResult(intent,100);
                    break;
                case R.id.tv_ac_login_regest:
                    Intent rigestIntent = new Intent(LoginActivity.this, RigestActivity.class);
                    startActivityForResult(rigestIntent,100);
                    break;
                case R.id.iv_ac_login_qq:
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(this);
                    qq.SSOSetting(false);
                    style=2;
                    authorize(qq, 2);
                    break;
                case R.id.iv_ac_login_wx:
                    if(isWeixinAvilible(this)) {
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(this);
                        wechat.SSOSetting(false);
                        style=1;
                        authorize(wechat, 1);
                    }else{
                        Toast.makeText(this, "您还没有安装微信，请先安装微信客户端",Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }
        }
    }
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
    private void authorize(Platform plat, int type) {
        switch (type) {
            case 1:
                showProgressDialog("正在打开微信");
                break;
            case 2:
                showProgressDialog("正在打开QQ");
                break;
            case 3:
                showProgressDialog("正在打开微博");
                break;
        }
        if (plat.isValid()) { //如果授权就删除授权资料
            plat.removeAccount();
        }
        plat.showUser(null);//授权并获取用户信息
    }
    private void showProgressDialog(String s) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(s);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.d("asdfasdfasdfsd", "handleMessage:授权成功 ");
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.d("asdfasdfasdfsd", "handleMessage:失败 "+throwable.getMessage().toString());
        progressDialog.dismiss();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.d("asdfasdfasdfsd", "handleMessage:取消 ");
        progressDialog.dismiss();
    }
    @Override
    public boolean handleMessage(Message message) {
        if (progressDialog != null)
            progressDialog.dismiss();
        switch (message.arg1) {
            case 1: { // 成功
                Platform platform = (Platform) message.obj;
                userId = platform.getDb().getUserId();//获取用户账号
                userName = platform.getDb().getUserName();//获取用户名字
                userIcon = platform.getDb().getUserIcon();//获取用户头像
                userGender = platform.getDb().getUserGender();
                checkWXQQ(userId);
            }
            break;

        }
        return false;
    }
    private void checkWXQQ(String UserId) {
        if (style == 1) {//微信
            three = "wxUserId";
        }
        if (style == 2) {//QQ
            three = "qqUserId";
        }
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add(three,UserId);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN_QW)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {//已注册
                            startActivity(new Intent(LoginActivity.this,QuedingWQActivity.class));
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void login(String phone, String psw) {
        Log.e("asdfasdfa", "onSuccess: body =登陆成功 " );
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }

        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("password",psw);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("asdfasdfa", "onSuccess: body =登陆成功 " );
                        ToastUtil.showShortToast(LoginActivity.this,"登录成功");
                        LoginVo vo = JSON.parseObject(body, LoginVo.class);
                        SharePreUtil.setUserInfo(LoginActivity.this,vo);
                        EventBus.getDefault().post("login");
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        Log.e("asdfasdfa", "onSuccess: body =登陆成 " );
                        ToastUtil.showShortToast(LoginActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        Log.e("asdfasdfa", "onSuccess: body =登陆 "+id );
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            if (requestCode == 100){
                switch (resultCode) {
                    case 1:
                        etPhone.setText(data.getStringExtra("phone"));
                        etPhone.setSelection(data.getStringExtra("phone").length());
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //判断是否是“DOWN”键
        if(actionId == EditorInfo.IME_ACTION_DONE){
            // 对应逻辑操作
            String psw = etPsw.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(psw) || phone.equals("")){
                ToastUtil.showShortToast(LoginActivity.this,"手机号或密码不能为空");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            login(phone,psw);
            return true;
        }
        return false;
    }

    private void checkPhone(String phone, String code, String psw, String openid, String name, String imageUrl) {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("smscode",code);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CHECK_PHONE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {//已注册
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }


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
                        ToastUtil.showShortToast(LoginActivity.this,"验证码发送成功");
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(LoginActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }



}
