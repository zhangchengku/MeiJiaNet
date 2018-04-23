package meijia.com.meijianet.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.liushibuju.BaseAdapter;
import meijia.com.srdlibrary.liushibuju.TagLayout;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class AddressActivity extends BaseActivity {
    private TextView tvTitle;
    private TagLayout mTagLayout;
    private String citys[] = {"柯城", "衢江", "巨化", "其他区域"};
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_address);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("选择区域");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        mTagLayout = (TagLayout) findViewById(R.id.tag_layout);
    }

    @Override
    protected void initData() {
        mTagLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return citys.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                TextView view = (TextView) LayoutInflater.from(AddressActivity.this).inflate(R.layout.item_tag, parent, false);
                view.setText(citys[position]);
                return view;
            }
        });
        mTagLayout.setOnChildViewClickListener(new TagLayout.OnChildViewClickListener() {
            @Override
            public void onChildClick(View view, int postion) {
                mTagLayout.setItemSelecte(postion);
                updateMsg("location",citys[postion]);
            }
        });
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }

    private void updateMsg(String params,String values){
        if (!NetworkUtil.checkNet(AddressActivity.this)){
            ToastUtil.showShortToast(AddressActivity.this,"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(AddressActivity.this,false);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL+UPDATE_MSG)
                .addParams(params,values)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        LoginVo vo = SharePreUtil.getUserInfo(AddressActivity.this);
                        vo.setLocation(values);
                        SharePreUtil.setUserInfo(AddressActivity.this,vo);
                        Intent intent = new Intent();
                        intent.putExtra("city",values);
                        setResult(105,intent);
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(AddressActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
