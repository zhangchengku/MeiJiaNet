package meijia.com.meijianet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.MyEntrustAdapter;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.ContentActivity;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.MyEntrustActivity;
import meijia.com.meijianet.ui.PostHouseActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.ui.TransactionRecordActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.vo.myentrust.MyEntrustVo;

/**
 * Created by Administrator on 2018/4/20.
 * sellinghome
 */

public class SellingHomeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener{
    private Toolbar toolbar;
    private TextView tvTitle;
    private LinearLayout llParent;
    private ImageView ivMenu;
    private LinearLayout add;
    private RecyclerView rvList;
    private MyEntrustAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<MyEntrustVo> datas = new ArrayList<>();
    private RelativeLayout rlEmpty;
    private SwipeToLoadLayout swipeToLoadLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_seller_notice, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    protected void initView() {
        llParent=(LinearLayout)view.findViewById(R.id.activity_talk_list);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) view.findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("卖房委托");
        add = (LinearLayout) view.findViewById(R.id.add);
        rvList = (RecyclerView)view.findViewById(R.id.swipe_target);
        rlEmpty = (RelativeLayout)view.findViewById(R.id.rl_ac_chezhu_empty);
        swipeToLoadLayout = (SwipeToLoadLayout)view.findViewById(R.id.refresh_layout);
        swipeToLoadLayout.setOnRefreshListener(this);
        autoRefresh();
    }

    @Override
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity()), 0, 0);
            }
        });
        mAdapter = new MyEntrustAdapter(getActivity(),datas);
        mManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(mManager);
        rvList.setAdapter(mAdapter);

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
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("istatle", "卖家须知");
                    intent.putExtra("url", BaseURL.BASE_URL+"/api/salerNotice");
                    startActivity(intent);

                    break;
                default:
                    break;
            }
        }

    }


    private void getMyEntrust(){
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没有网了，请检查网络");
            swipeToLoadLayout.setRefreshing(false);
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
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
                        if(returnTip.equals("未登录")){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        swipeToLoadLayout.setRefreshing(false);
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }


    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
    @Override
    public void onRefresh() {
        getMyEntrust();
    }
    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);
    }
}
