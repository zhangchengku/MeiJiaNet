package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(price) ||TextUtils.isEmpty(xiaoQu)){
                        ToastUtil.showShortToast(PostHouseActivity.this,"请将信息填写完整");
                        return;
                    }
                    getid(phone,address,name,price,xiaoQu);
                    break;

            }
        }
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
            getid(phone,address,name,price,xiaoQu);
            return true;
        }
        return false;
    }

    private void getid(String phone, String address, String name, String price, String xiaoQu) {
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
                        pullHouse(phone,address,name,price,xiaoQu,String.valueOf(vo.getId()));
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

    private void pullHouse(String phone, String address, String name, String price, String xiaoQu,String id) {
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
