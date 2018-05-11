package meijia.com.meijianet.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.MyEntrustAdapter;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.vo.myentrust.MyEntrustVo;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * 我的委托
 */
public class MyEntrustActivity extends BaseActivity {
    private RelativeLayout rlEmpty;
    private TextView tvTitle;

    private RecyclerView rvList;
    private MyEntrustAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<MyEntrustVo> datas = new ArrayList<>();
    private LinearLayout linear;
    private LinearLayout add;

    @Override
    protected void setContent() {
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_my_entrust);
    }

    @Override
    protected void initView() {
        linear = (LinearLayout) findViewById(R.id.activity_my_entrust);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("我的委托");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        rvList = (RecyclerView)findViewById(R.id.rv_list);
        add = (LinearLayout) findViewById(R.id.add);
        rlEmpty = (RelativeLayout)findViewById(R.id.rl_ac_chezhu_empty);

    }

    @Override
    protected void initData() {
        linear.post(new Runnable() {
            @Override
            public void run() {
                linear.setPadding(0, BubbleUtils.getStatusBarHeight(MyEntrustActivity.this), 0, 0);
            }
        });
        mAdapter = new MyEntrustAdapter(MyEntrustActivity.this,datas);
        mManager = new LinearLayoutManager(MyEntrustActivity.this);
        rvList.setLayoutManager(mManager);
        rvList.setAdapter(mAdapter);
//        mAdapter.setOnPicClickListener(this);
    }

    @Override
    protected void initClick() {
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
        switch (v.getId()) {
            case R.id.add:
                Intent intent = new Intent(MyEntrustActivity.this,WebViewActivity.class);
                intent.putExtra("istatle", "卖家须知");
                intent.putExtra("url", BaseURL.BASE_URL+"/api/salerNotice");
                startActivity(intent);

                break;
            default:
                break;
        }
    }

        }

    @Override
    protected void onResume() {
        super.onResume();
        getMyEntrust();
    }

    private void getMyEntrust(){
        if (!NetworkUtil.checkNet(MyEntrustActivity.this)){
            ToastUtil.showShortToast(MyEntrustActivity.this,"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(MyEntrustActivity.this,false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+MY_ENTRUST)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<MyEntrustVo> myEntrustVos = JSON.parseArray(body, MyEntrustVo.class);
                        if (myEntrustVos!=null && myEntrustVos.size()>0){
                            datas.clear();
                            datas.addAll(myEntrustVos);
                            mAdapter.notifyDataSetChanged();
                            rlEmpty.setVisibility(View.GONE);
                        }else {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }


}
