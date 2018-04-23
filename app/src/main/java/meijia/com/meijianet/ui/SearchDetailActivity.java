package meijia.com.meijianet.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.activity.RequestParams;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.activity.SearchMoreAdapter;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class SearchDetailActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, TextView.OnEditorActionListener {
    private String address = "";

    private EditText etSearch;
    private ImageView ivDelete;
    private ImageView ivBack;
    private RelativeLayout rlMianji;
    private RelativeLayout rlZongjia;
    private RelativeLayout rlMoren;
    private TextView tvMoren;
    private TextView tvMianji;
    private TextView tvZongjia;

    private SwipeToLoadLayout swipeToLoadLayout;
    private RelativeLayout rlEmpty;
    private RecyclerView rvList;
    private SearchMoreAdapter mAdapter;
    private List<NewHouseInfo> datas = new ArrayList<>();
    private int pageNo = 1;
    public static final int PAGE_SIZE = 10;
    private String mCity;
    private List<String> listByShare;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_search_detail);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        rlMianji = (RelativeLayout) findViewById(R.id.rl_ac_searchdetail_mianji);
        rlZongjia = (RelativeLayout) findViewById(R.id.rl_ac_searchdetail_zongjia);
        rlMoren = (RelativeLayout) findViewById(R.id.rl_ac_searchdetail_moren);
        tvMoren = (TextView) findViewById(R.id.tv_ac_searchdetail_moren);
        tvMianji = (TextView) findViewById(R.id.tv_ac_searchdetail_mianji);
        tvZongjia = (TextView) findViewById(R.id.tv_ac_searchdetail_zongjia);
        etSearch = (EditText) findViewById(R.id.et_ac_search);
        ivDelete = (ImageView) findViewById(R.id.iv_ac_search_delete);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ToolUtil.setInputListener(etSearch, ivDelete);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.refresh_layout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        rvList = (RecyclerView) findViewById(R.id.swipe_target);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_ac_chezhu_empty);
        rvList.setLayoutManager(new LinearLayoutManager(SearchDetailActivity.this));
        mAdapter = new SearchMoreAdapter(SearchDetailActivity.this, datas);
        rvList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        listByShare = SharePreUtil.getListByShare(SearchDetailActivity.this);
        mCity = getIntent().getStringExtra("city");
        if (mCity != null && !TextUtils.isEmpty(mCity)) {
            address = mCity;
            etSearch.setText(address);
            etSearch.setSelection(address.length());
        }


        autoRefresh();
    }

    @Override
    protected void initClick() {
        etSearch.setOnEditorActionListener(this);
        ivBack.setOnClickListener(this);
        rlMoren.setOnClickListener(this);
        rlZongjia.setOnClickListener(this);
        rlMianji.setOnClickListener(this);
    }

    private int mianjiStatus = 0;//0默认 1降序 2 升序
    private int zongjiaStatus = 0;

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.rl_ac_searchdetail_moren://默认
                    if (datas.size()<=0){
                        ToastUtil.showShortToast(SearchDetailActivity.this,"没有更多房源，换个小区试试吧");
                        return;
                    }
                    tvZongjia.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_sort_default), null);
                    tvMianji.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(R.mipmap.icon_sort_default), null);
                    mianjiStatus = 0;
                    zongjiaStatus = 0;
                    autoRefresh();
                    break;
                case R.id.rl_ac_searchdetail_mianji://面积
                    if (datas.size()<=0){
                        ToastUtil.showShortToast(SearchDetailActivity.this,"没有更多房源，换个小区试试吧");
                        return;
                    }
                    if (zongjiaStatus!=0){
                        zongjiaStatus = 0;
                        tvZongjia.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_default), null);
                    }
                    if (mianjiStatus == 0 || mianjiStatus == 2) {
                        mianjiStatus = 1;
                        tvMianji.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_high), null);
                    } else {
                        mianjiStatus = 2;
                        tvMianji.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_low), null);
                    }
                    autoRefresh();
                    break;
                case R.id.rl_ac_searchdetail_zongjia://总价
                    if (datas.size()<=0){
                        ToastUtil.showShortToast(SearchDetailActivity.this,"没有更多房源，换个小区试试吧");
                        return;
                    }
                    if (mianjiStatus!=0){
                        mianjiStatus = 0;
                        tvMianji.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_default), null);
                    }
                    if (zongjiaStatus == 0 || zongjiaStatus == 2) {
                        zongjiaStatus = 1;
                        tvZongjia.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_high), null);
                    } else {
                        zongjiaStatus = 2;
                        tvZongjia.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.mipmap.icon_sort_low), null);
                    }
                    autoRefresh();
                    break;
                default:
                    break;
            }
        }
    }

    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(this)) {
            ToastUtil.showShortToast(this, "没网啦，请检查网络");
            swipeToLoadLayout.setRefreshing(false);
            return;
        }
        final RequestParams params = new RequestParams();
        params.add("pageNo", 1);
        params.add("pageSize", PAGE_SIZE);
        params.add("name", mCity);
        if (mianjiStatus == 1) {
            params.add("aceareaAsc", "0");
        } else if (mianjiStatus == 2) {
            params.add("aceareaAsc", "1");
        }

        if (zongjiaStatus == 1) {
            params.add("priceAsc", "0");
        } else if (zongjiaStatus == 2) {
            params.add("priceAsc", "1");
        }

        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + SEARCH_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<NewHouseInfo> newHouseInfos = JSON.parseArray(body, NewHouseInfo.class);
                        if (newHouseInfos.size() > 0) {
                            datas.clear();
                            datas.addAll(newHouseInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo = 1;
                            rlEmpty.setVisibility(View.GONE);
                        } else {
                            swipeToLoadLayout.setRefreshing(false);
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(SearchDetailActivity.this, returnTip);
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
        if (!NetworkUtil.checkNet(this)) {
            ToastUtil.showShortToast(this, "没网了，请检查网络");
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        if (datas.isEmpty()) {
            swipeToLoadLayout.setLoadingMore(false);
            return;
        }
        RequestParams params = new RequestParams(this);
        params.add("pageNo", pageNo + 1);
        params.add("pageSize", PAGE_SIZE);
        params.add("name", mCity);
        if (mianjiStatus == 1) {
            params.add("aceareaAsc", "0");
        } else if (mianjiStatus == 2) {
            params.add("aceareaAsc", "1");
        }

        if (zongjiaStatus == 1) {
            params.add("priceAsc", "0");
        } else if (zongjiaStatus == 2) {
            params.add("priceAsc", "1");
        }
        OkHttpUtils
                .get()
                .tag(this)
                .url(BaseURL.BASE_URL + SEARCH_HOUSE)
                .params(params.getMap())
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        List<NewHouseInfo> newHouseInfos = JSON.parseArray(body, NewHouseInfo.class);

                        if (newHouseInfos.size() > 0) {
                            datas.addAll(newHouseInfos);
                            mAdapter.notifyDataSetChanged();
                            pageNo++;
                        } else {
                            ToastUtil.showShortToast(SearchDetailActivity.this, "没有更多了...");
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(SearchDetailActivity.this, returnTip);
                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
            // 对应逻辑操作
            String psw = etSearch.getText().toString().trim();
            if (TextUtils.isEmpty(psw)){
                ToastUtil.showShortToast(SearchDetailActivity.this,"小区名称不能为空");
                return false;
            }
            mCity = psw;
            //隐藏软键盘
            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()){
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            autoRefresh();
            boolean isTrue = false;
            int p = 100;
            if (listByShare!=null){
                if (listByShare.size()>=10){
                    for (int i = 0; i < listByShare.size(); i++) {
                        if (listByShare.get(i).equals(psw)){
                            isTrue = true;
                            p = i;
                        }
                    }
                    if (!isTrue){
                        listByShare.remove(0);
                        listByShare.add(psw);
                    }
                }else if (listByShare.size()<=0){
                    listByShare.add(psw);
                }else {
                    for (int i = 0; i < listByShare.size(); i++) {
                        if (listByShare.get(i).equals(psw)){
                            isTrue = true;
                            p = i;
                        }
                    }
                    if (!isTrue){
                        listByShare.add(psw);
                    }
                }
                SharePreUtil.putListToShare(SearchDetailActivity.this,listByShare);
            }
            return true;
        }
        return false;
    }
}
