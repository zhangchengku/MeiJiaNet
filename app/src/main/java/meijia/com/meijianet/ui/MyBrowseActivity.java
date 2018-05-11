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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CollectAdapter;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.bean.MyBrowseVo;
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

public class MyBrowseActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, CollectAdapter.onMyItemClickListener {
    private TextView tvTitle;
    private ImageView ivMenu;


    private SwipeToLoadLayout swipeToLoadLayout;
    private RelativeLayout rlEmpty;
    private RecyclerView rvList;
    private CollectAdapter mAdapter;
    private List<MyCollectInfo> datas = new ArrayList<>();
    private int pageNo = 1;
    public static final int PAGE_SIZE = 10;
    private ArrayList<String> ids = new ArrayList<>();//删除的收藏id集合
    private ArrayList<Integer> positions = new ArrayList<>();
    private LinearLayout llParent;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_my_browse);
        StatusBarUtils.setStatusBarFontDark(this,true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
    }

    @Override
    protected void initView() {
        llParent=(LinearLayout)findViewById(R.id.activity_my_collect);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("浏览记录");
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
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(MyBrowseActivity.this), 0, 0);
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.refresh_layout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        rvList = (RecyclerView) findViewById(R.id.swipe_target);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_ac_chezhu_empty);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CollectAdapter(this, datas);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnMyItemClickListener(this);
        autoRefresh();
    }

    @Override
    protected void initClick() {
        ivMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {

            }
        }

    }

    private void collectHouse(String id, boolean isEmpty) {
        Log.e(TAG, "collectHouse: id = "+id );
        if (!NetworkUtil.checkNet(MyBrowseActivity.this)) {
            ToastUtil.showShortToast(MyBrowseActivity.this, "没网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(MyBrowseActivity.this, false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + DELETE_BROWSE)
                .addParams("houseids", id)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        ToastUtil.showShortToast(MyBrowseActivity.this, "删除收藏");
                        mAdapter.setCheck();
                        autoRefresh();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(MyBrowseActivity.this, "收藏清除失败，请稍后重试");
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
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
                .url(BaseURL.BASE_URL + MY_BROWSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        datas.clear();
                        List<MyBrowseVo> myBrowseVos = JSON.parseArray(body, MyBrowseVo.class);
                        if (myBrowseVos.size()>0){
                            for (int i = 0; i < myBrowseVos.size(); i++) {
                                MyBrowseVo myBrowseVo = myBrowseVos.get(i);
                                MyCollectInfo house = myBrowseVo.getHouse();
                                datas.add(house);
                            }
//                            datas.addAll(myCollectInfos);
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
                .url(BaseURL.BASE_URL+MY_BROWSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        List<MyBrowseVo> myBrowseVos = JSON.parseArray(body, MyBrowseVo.class);
                        if (myBrowseVos.size()>0){
                            for (int i = 0; i < myBrowseVos.size(); i++) {
                                MyBrowseVo myBrowseVo = myBrowseVos.get(i);
                                MyCollectInfo house = myBrowseVo.getHouse();
                                datas.add(house);
                            }
//                            datas.addAll(myCollectInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo ++;
                        }else {
                            ToastUtil.showShortToast(MyBrowseActivity.this, "没有更多了...");
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {


                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    @Override
    public void onItemClick(int positon) {
        Intent intent = new Intent(MyBrowseActivity.this,WebViewActivity.class);
        LoginVo userInfo = SharePreUtil.getUserInfo(MyBrowseActivity.this);
        intent.putExtra("istatle", "房屋详情");
        if (!userInfo.getUuid().equals("")){
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(positon).getId()+"&uuid="+userInfo.getUuid());
        }else {
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(positon).getId()+"&uuid="+"");
        }
        startActivity(intent);
    }
}
