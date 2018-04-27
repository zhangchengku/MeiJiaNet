package meijia.com.meijianet.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CollectAdapter;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.activity.MyCollectInfo;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.StringUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class MyCollectActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, CollectAdapter.onMyItemClickListener {
    private TextView tvTitle;


    private SwipeToLoadLayout swipeToLoadLayout;
    private RelativeLayout rlEmpty;
    private RecyclerView rvList;
    private CollectAdapter2 mAdapter;
    private List<MyCollectInfo> datas = new ArrayList<>();
    private int pageNo = 1;
    public static final int PAGE_SIZE = 10;
    private ArrayList<String> ids = new ArrayList<>();//删除的收藏id集合
    private ArrayList<Integer> positions = new ArrayList<>();
    private ImageView ivMenu;
    private LinearLayout llParent;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_my_collect);
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected void initView() {
        llParent=(LinearLayout)findViewById(R.id.activity_my_collect);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("我的收藏");
        ivMenu = (ImageView) findViewById(R.id.iv_toolbar_menu);
        ivMenu.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

    @Override
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(MyCollectActivity.this), 0, 0);
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.refresh_layout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        rvList = (RecyclerView) findViewById(R.id.swipe_target);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_ac_chezhu_empty);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectAdapter2(this, datas);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnMyItemClickListener(this);
        autoRefresh();
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {

            }
        }

    }
    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            swipeToLoadLayout.setRefreshing(false);
            return;
        }
        final RequestParams params = new RequestParams(this);
        params.add("pageNo", 1);
        params.add("pageSize", PAGE_SIZE);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + MY_COLLECT)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        datas.clear();
                        List<MyCollectInfo> myCollectInfos = JSON.parseArray(body, MyCollectInfo.class);
                        if (myCollectInfos.size()>0){
                            datas.addAll(myCollectInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo = 1;
                            rlEmpty.setVisibility(View.GONE);
                        }else {
                            swipeToLoadLayout.setRefreshing(false);
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }



                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setRefreshing(false);
                    }
                });


    }


    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        }, 200);
    }

    private void refresh() {
        getDataByNet();
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore();
            }
        }, 200);
    }

    private void loadMore() {
        more();
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void more() {
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网了，请检查网络");
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        if (datas.isEmpty()) {
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        RequestParams params = new RequestParams(this);
        params.add("pageNo", pageNo+1);
        params.add("pageSize", PAGE_SIZE);
        OkHttpUtils
                .post()
                .tag(this)
                .url(BaseURL.BASE_URL+MY_COLLECT)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        List<MyCollectInfo> myCollectInfos = JSON.parseArray(body, MyCollectInfo.class);
                        if (myCollectInfos.size()>0){
                            datas.addAll(myCollectInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo ++;
                        }else {
                            ToastUtil.showShortToast(MyCollectActivity.this, "没有更多了...");
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        if (returnTip.equals("未登录")){
                            appoutAll();
                            ToastUtil.showShortToast(MyCollectActivity.this,"登录过期或未登录，请登录");
                        }else {
                            ToastUtil.showShortToast(MyCollectActivity.this,returnTip);
                        }

                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    @Override
    public void onItemClick(int positon) {
        Intent intent = new Intent(MyCollectActivity.this,HouseDetailActivity.class);
        intent.putExtra("id",datas.get(positon).getId());
        startActivity(intent);
    }



}
