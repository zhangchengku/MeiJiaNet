package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.WechatPayVO;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.DisplayUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

import static android.R.attr.id;

public class PostHouseActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private TextView tvTitle;
    private ImageView ivMenu;
    private TextView tvComplite;
    private EditText etXiaoqu;
    private EditText etAddress;
    private EditText etPrice;
    private EditText etName;
    private EditText etPhone;
    private PopupWindow mPopupWindow;
    private TextView sendss;
    private EditText cade;


    private LinearLayout linear;


    @Override
    protected void setContent() {
        setContentView(R.layout.activity_post_house);
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected void initView() {

                linear = (LinearLayout) findViewById(R.id.activity_post_house);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("发布房源");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        etXiaoqu = (EditText) findViewById(R.id.et_ac_post_xiaoquname);
        etAddress = (EditText) findViewById(R.id.et_ac_post_address);
        etPrice = (EditText) findViewById(R.id.et_ac_post_price);
        etName = (EditText) findViewById(R.id.et_ac_post_name);
        etPhone = (EditText) findViewById(R.id.et_ac_post_phone);

        tvComplite = (TextView) findViewById(R.id.tv_ac_post_complete);
        sendss = (TextView) findViewById(R.id.send_ss);
        cade = (EditText) findViewById(R.id.cade);

    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linear.post(new Runnable() {
                @Override
                public void run() {
                    linear.setPadding(0, BubbleUtils.getStatusBarHeight(PostHouseActivity.this), 0, 0);
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

    }

    @Override
    protected void initClick() {
        tvComplite.setOnClickListener(this);
        etPhone.setOnEditorActionListener(this);
        sendss.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_post_complete:
                    String phone = etPhone.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String name = etName.getText().toString().trim();
                    String price = etPrice.getText().toString().trim();
                    String xiaoQu = etXiaoqu.getText().toString().trim();
                    String yzm = cade.getText().toString().trim();

                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(price) ||TextUtils.isEmpty(xiaoQu)){
                        ToastUtil.showShortToast(PostHouseActivity.this,"请将信息填写完整");
                        return;
                    }
                    getid(phone,address,name,price,xiaoQu,yzm);
                    break;
                case R.id.send_ss:
                    String phone1 = etPhone.getText().toString().trim();
                    if (phone1.equals("")){
                        ToastUtil.showShortToast(PostHouseActivity.this,"手机号不能为空");
                        return;
                    }
                    if (!ToolUtil.isPhoneNumber(phone1)){
                        ToastUtil.showShortToast(PostHouseActivity.this,"请输入正确的手机格式");
                        return;
                    }
                    getcode(phone1);
                    sendss.setEnabled(false);
                    sendss.setBackgroundColor(Color.parseColor("#999999"));
                    timer.start();

                    break;
            }
        }
    }

    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //每隔countDownInterval秒会回调一次onTick()方法
            sendss.setText(millisUntilFinished / 1000 + " s");
        }

        @Override
        public void onFinish() {
            sendss.setText("重新获取");
            sendss.setEnabled(true);
        }
    };
    private void getcode(String phone) {

        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        RequestParams params = new RequestParams(this);
        params.add("phone",phone);
        params.add("codetype","5");
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CODE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(PostHouseActivity.this,"验证码发送成功");
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(PostHouseActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
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
        if(actionId == EditorInfo.IME_ACTION_DONE){
            // 对应逻辑操作
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String price = etPrice.getText().toString().trim();
            String xiaoQu = etXiaoqu.getText().toString().trim();
            String yzm = cade.getText().toString().trim();
            if (!phone.equals("") ){
                if (!ToolUtil.isPhoneNumber(phone)){
                    ToastUtil.showShortToast(PostHouseActivity.this,"请输入正确的手机号码");
                    return false;
                }
            }
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(price) ||TextUtils.isEmpty(xiaoQu)){
                ToastUtil.showShortToast(PostHouseActivity.this,"请将信息填写完整");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            getid(phone,address,name,price,xiaoQu,yzm);
            return true;
        }
        return false;
    }

    private void getid(String phone, String address, String name, String price, String xiaoQu,String yzm) {
        if (!NetworkUtil.checkNet(PostHouseActivity.this)){
            ToastUtil.showShortToast(PostHouseActivity.this,"没网了，请检查网络");
            return;
        }

        PromptUtil.showTransparentProgress(PostHouseActivity.this,false);

        RequestParams params = new RequestParams();
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+EXCLUSIVE_TWO)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        GetIdVO vo = JSON.parseObject(body, GetIdVO.class);
                        pullHouse(phone,address,name,price,xiaoQu,String.valueOf(vo.getId()),yzm);
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        PromptUtil.closeTransparentDialog();
                        ToastUtil.showShortToast(PostHouseActivity.this,returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void pullHouse(String phone, String address, String name, String price, String xiaoQu,String id,String yzm) {
        if (!NetworkUtil.checkNet(PostHouseActivity.this)){
            ToastUtil.showShortToast(PostHouseActivity.this,"没网了，请检查网络");
            return;
        }

        PromptUtil.showTransparentProgress(PostHouseActivity.this,false);

        RequestParams params = new RequestParams();
        params.add("name",xiaoQu);
        params.add("address",address);
        params.add("price",price);
        params.add("employeeId",id);
        params.add("contactname",name);
        params.add("contactphone",phone);
        params.add("smscode",yzm);

        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+PULL_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        startActivity(new Intent(PostHouseActivity.this,ExclusiveActivity.class));
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        PromptUtil.closeTransparentDialog();
                        ToastUtil.showShortToast(PostHouseActivity.this,returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });


    }
}
