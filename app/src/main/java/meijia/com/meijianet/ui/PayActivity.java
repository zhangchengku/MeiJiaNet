package meijia.com.meijianet.ui;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import meijia.com.meijianet.R;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.commondialog.CommonDialog;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class PayActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvSure;
    private CommonDialog mDialog;
    private EditText etName,etPhone;

    private long mId;
    public static boolean isPay = false;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_pay);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("缴纳意向金");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        tvSure = (TextView) findViewById(R.id.tv_ac_pay_sure);
        etName = (EditText) findViewById(R.id.et_ac_pay_name);
        etPhone = (EditText) findViewById(R.id.et_ac_pay_phone);
    }

    @Override
    protected void initData() {
        mId = getIntent().getLongExtra("id",0);
        Log.e(TAG, "initData: id = "+mId );
    }

    @Override
    protected void initClick() {
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_pay_sure:
                    showMyDialog();
                    break;
                case R.id.tv_wx:
                    getOrderNum(0);
                    if (mDialog!=null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    break;
                case R.id.tv_ali:
                    getOrderNum(1);
                    if (mDialog!=null && mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getOrderNum(int type) {
        if (!NetworkUtil.checkNet(PayActivity.this)){
            ToastUtil.showShortToast(PayActivity.this,"没有网了，请检查网络");
            return;
        }
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (name.equals("") || phone.equals("")){
            ToastUtil.showShortToast(PayActivity.this,"请将信息填写完整");
            return;
        }
        if (!ToolUtil.isPhoneNumber(phone)){
            ToastUtil.showShortToast(PayActivity.this,"请输入正确的手机号码");
            return;
        }
        PromptUtil.showTransparentProgress(PayActivity.this,false);
        RequestParams params = new RequestParams();
        params.add("houseId",mId+"");
        params.add("tel",phone);
        params.add("realName",name);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+ORDER_PAY)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        try {
                            JSONObject object = new JSONObject(body);
                            String ordernum = object.getString("ordernum");
                            if (ordernum!=null && !ordernum.equals("")){
                                if (type == 0){
                                    wechatPay(ordernum);
                                }else if (type == 1){
                                    aliPay(ordernum);
                                }else {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        PromptUtil.closeTransparentDialog();
                        ToastUtil.showShortToast(PayActivity.this,returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    private void showMyDialog() {
        View view = LayoutInflater.from(PayActivity.this).inflate(R.layout.pay_layout, null);
        view.findViewById(R.id.tv_wx).setOnClickListener(this);
        view.findViewById(R.id.tv_ali).setOnClickListener(this);
        mDialog = new CommonDialog(PayActivity.this,view,0, Gravity.BOTTOM);
        mDialog.show();
    }

//    //微信支付
    private void wechatPay(String orderNumber) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + WX_PAY)
                .addParams("orderNum", orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(PayActivity.this, returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }


    //支付宝支付
    private void aliPay(String orderNumber) {
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL + ALI_PAY)
                .addParams("orderNum",orderNumber)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(PayActivity.this, returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

}
