package meijia.com.meijianet.ui;

import android.content.Intent;
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
import meijia.com.meijianet.bean.ChengJiaoVo;
import meijia.com.meijianet.activity.MyChengJiaoAdapter;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class KnockdownActivity extends BaseActivity implements MyChengJiaoAdapter.onPicClickListener {
    private TextView tvTitle;
    private RecyclerView rvList;
    private RelativeLayout rlEmpty;
    private LinearLayoutManager mManager;
    private MyChengJiaoAdapter mAdapter;
    private List<ChengJiaoVo> datas = new ArrayList<>();
    //item_rv_chengjiao  条目布局
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_knockdown);
        StatusBarUtils.setStatusBarColor(this,getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("最近成交");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        rvList = (RecyclerView) findViewById(R.id.rv_ac_knockdown_list);
    }

    @Override
    protected void initData() {
        mAdapter = new MyChengJiaoAdapter(KnockdownActivity.this,datas);
        mManager = new LinearLayoutManager(KnockdownActivity.this);
        rvList.setLayoutManager(mManager);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnPicClickListener(this);
        getMyChengJiao();
    }

    private void getMyChengJiao() {

        if (!NetworkUtil.checkNet(KnockdownActivity.this)){
            ToastUtil.showShortToast(KnockdownActivity.this,"没有网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(KnockdownActivity.this,false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL+RECENTLY_SELL)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<ChengJiaoVo> myEntrustVos = JSON.parseArray(body, ChengJiaoVo.class);
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
                        ToastUtil.showShortToast(KnockdownActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPicClick(int position) {
        Intent intent = new Intent(KnockdownActivity.this, HouseDetailActivity.class);
        intent.putExtra("id",datas.get(position).getId());
        startActivity(intent);
    }
}
