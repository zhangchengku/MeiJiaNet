package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
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

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
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
    private View viewLine;

    private RelativeLayout rlEmpty;
    private LinearLayout llContent;
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_post_house);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("发布房源");
        ivMenu = (ImageView) findViewById(R.id.iv_toolbar_menu);
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        etXiaoqu = (EditText) findViewById(R.id.et_ac_post_xiaoquname);
        etAddress = (EditText) findViewById(R.id.et_ac_post_address);
        etPrice = (EditText) findViewById(R.id.et_ac_post_price);
        etName = (EditText) findViewById(R.id.et_ac_post_name);
        etPhone = (EditText) findViewById(R.id.et_ac_post_phone);
        viewLine = findViewById(R.id.view_line);
        tvComplite = (TextView) findViewById(R.id.tv_ac_post_complete);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {
        tvComplite.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        etPhone.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_toolbar_menu:
                    showPop(viewLine);
                    break;
                case R.id.tv_xuzhi:
                    Intent intent = new Intent(this, SellerNoticeActivity.class);
                    intent.putExtra("isTrue",true);
                    startActivity(intent);
                    if (mPopupWindow!=null&&mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                    break;
                case R.id.tv_chengjiao:
                    startActivity(new Intent(this,KnockdownActivity.class));
                    if (mPopupWindow!=null&&mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                    break;
                case R.id.tv_liucheng:
                    startActivity(new Intent(this, ProcessActivity.class));
                    if (mPopupWindow!=null&&mPopupWindow.isShowing()){
                        mPopupWindow.dismiss();
                    }
                    break;
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
                    pullHouse(phone,address,name,price,xiaoQu);
                    break;

            }
        }
    }

    private void showPop(View imageView) {
        View view = LayoutInflater.from(PostHouseActivity.this)
                .inflate(R.layout.pop_menu,null);
        view.findViewById(R.id.tv_xuzhi).setOnClickListener(this);
        view.findViewById(R.id.tv_chengjiao).setOnClickListener(this);
        view.findViewById(R.id.tv_liucheng).setOnClickListener(this);
        mPopupWindow = new PopupWindow(view,ViewGroup.LayoutParams.WRAP_CONTENT
        ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.showAsDropDown(imageView, -DisplayUtil.dip2px(PostHouseActivity.this,50f),10);
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
            pullHouse(phone,address,name,price,xiaoQu);
            return true;
        }
        return false;
    }

    private void pullHouse(String phone, String address, String name, String price, String xiaoQu) {
        if (!NetworkUtil.checkNet(PostHouseActivity.this)){
            ToastUtil.showShortToast(PostHouseActivity.this,"没网了，请检查网络");
            return;
        }

        PromptUtil.showTransparentProgress(PostHouseActivity.this,false);

        RequestParams params = new RequestParams();
        params.add("name",xiaoQu);
        params.add("contactphone",phone);
        params.add("address",address);
        params.add("price",price);
        params.add("contactname",name);

        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+PULL_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        llContent.setVisibility(View.GONE);
                        rlEmpty.setVisibility(View.VISIBLE);
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
