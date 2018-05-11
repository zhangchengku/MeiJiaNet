package meijia.com.meijianet.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CollectAdapter;
import meijia.com.meijianet.activity.IntentionAdapter;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.fragment.IntentionFragment;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.DisplayUtil;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.vo.intention.IntentionVo;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class MyIntentionActivity extends BaseActivity implements CollectAdapter.onMyItemClickListener {

    private List<IntentionVo> datas = new ArrayList<>();
    private TextView tvTitle;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView rvList;
    private RelativeLayout rlEmpty;
    private CollectAdapter3 mAdapter;
    private ImageView ivMenu;
    private LinearLayout linear;

    @Override
    protected void setContent() {
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_my_intention);

    }

    @Override
    protected void initView() {
        linear = (LinearLayout) findViewById(R.id.activity_my_intention);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("意向房源");
        ivMenu = (ImageView) findViewById(R.id.iv_toolbar_menu);
        ivMenu.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_ac_chezhu_empty);
        rvList = (RecyclerView) findViewById(R.id.rv_fm_intention_list);

    }

    @Override
    protected void initData() {
        linear.post(new Runnable() {
            @Override
            public void run() {
                linear.setPadding(0, BubbleUtils.getStatusBarHeight(MyIntentionActivity.this), 0, 0);
            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectAdapter3(this, datas);
        rvList.setAdapter(mAdapter);
        getDataByNet();
    }

    @Override
    protected void initClick() {
        mAdapter.setOnMyItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + MY_ITENTION)
                .addParams("type","-1")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<IntentionVo> intentionVos = JSON.parseArray(body, IntentionVo.class);
                        if (intentionVos!=null && intentionVos.size()>0){
                            datas.clear();
                            datas.addAll(intentionVos);
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
    public void onItemClick(int positon) {
        Intent intent = new Intent(MyIntentionActivity.this,WebViewActivity.class);
        LoginVo userInfo = SharePreUtil.getUserInfo(MyIntentionActivity.this);
        intent.putExtra("istatle", "房屋详情");
        if (!userInfo.getUuid().equals("")){
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(positon).getHouse().getId()+"&uuid="+userInfo.getUuid());
        }else {
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(positon).getHouse().getId()+"&uuid="+"");
        }
        startActivity(intent);

    }
}
