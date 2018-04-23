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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.MyEntrustAdapter;
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
public class MyEntrustActivity extends BaseActivity implements MyEntrustAdapter.onPicClickListener {
    private RelativeLayout rlEmpty;
    private TextView tvTitle;
    private TextView tvSell;
    private RecyclerView rvList;
    private MyEntrustAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<MyEntrustVo> datas = new ArrayList<>();
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_my_entrust);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("我的委托");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        rvList = (RecyclerView)findViewById(R.id.rv_list);
        rlEmpty = (RelativeLayout)findViewById(R.id.rl_ac_chezhu_empty);
        tvSell = (TextView) findViewById(R.id.tv_sell);
    }

    @Override
    protected void initData() {
        mAdapter = new MyEntrustAdapter(MyEntrustActivity.this,datas);
        mManager = new LinearLayoutManager(MyEntrustActivity.this);
        rvList.setLayoutManager(mManager);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnPicClickListener(this);
    }

    @Override
    protected void initClick() {
        tvSell.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!SharePreUtil.isFisrtSell(MyEntrustActivity.this)){
            SharePreUtil.setFirstSell(MyEntrustActivity.this,true);
            startActivity(new Intent(MyEntrustActivity.this,SellerNoticeActivity.class));
        }else {
            startActivity(new Intent(MyEntrustActivity.this,PostHouseActivity.class));
        }
    }

    private void call(int position) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+datas.get(position).getEmployee().getPhone() ));
        if (ActivityCompat.checkSelfPermission(MyEntrustActivity.this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
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

    @Override
    public void onPicClick(int position) {
        requestRuntimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
            @Override
            public void onGranted() {
                call(position);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                PromptUtil.showCommonDialog(MyEntrustActivity.this, "请在设置中打开拨打电话权限", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent); //此为设置完成后返回到获取界面
                        PromptUtil.closeCommonDialog();
                    }
                });
            }
        });
    }
}
