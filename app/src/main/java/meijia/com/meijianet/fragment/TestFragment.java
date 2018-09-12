package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donkingliang.banner.CustomBanner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;

import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.base.TubiaoVo;
import meijia.com.meijianet.bean.BannerVo;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.MyEntrustActivity;
import meijia.com.meijianet.ui.PromotionsPopActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.ui.TransactionRecordActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.ui.WebViewActivity2;
import meijia.com.meijianet.ui.WebViewActivity4;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;

import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created by Administrator on 2018/5/16.
 */

public class TestFragment extends BaseFragment {

    private List<NewHouseInfo> datas = new ArrayList<>();
    private RecyclerView list;

    private TextView tvMore;
    private JCVideoPlayerStandard mJc;
    private RecyclerView re;
    private CommonAdapter<NewHouseInfo> adapter;
    private SmartRefreshLayout smartRefreshLayout;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private CustomBanner ivBanner;
    private List<BannerVo> newHouseInfos;
    private VideoVo videoVos;

    private List<TubiaoVo> tubiaoVos = new ArrayList<>();
    private RecyclerView recy;
    private CommonAdapter<TubiaoVo> recyadapter;
    private VideoVo2 videoVos2;
    private String rediredt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first_fragment2, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initView() {
        list = (RecyclerView) view.findViewById(R.id.recycle);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        getmjds();
        getDataByNet();
        setPullRefresher();
    }

    private void getmjds() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + MJDS)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("000", "onSuccess: " + body);
                        videoVos2 = JSON.parseObject(body, VideoVo2.class);
                        if (videoVos2.getStatus() == 1) {
//                            mjds.setVisibility(View.VISIBLE);
                            if (videoVos2.getUrl() == null || videoVos2.getRedirectUrl() == null) {
                                return;
                            }
                            rediredt = videoVos2.getRedirectUrl();
                            Intent intent = new Intent(getActivity(), PromotionsPopActivity.class);
                            intent.putExtra("image", videoVos2.getUrl());
                            intent.putExtra("newlogin", String.valueOf(videoVos2.getNeedLogin()));
                            intent.putExtra("rediredt", rediredt);
                            startActivity(intent);
                        } else {
//                            mjds.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void getDataByNet() {
        //检查网络
        if (!NetworkUtil.checkNet(getActivity())) {
            ToastUtil.showShortToast(getActivity(), "没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(), false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + NEW_HOUSE)
                .addParams("flag", "true")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        datas = JSON.parseArray(body, NewHouseInfo.class);
                        setAdapter();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });


    }

    private void setPullRefresher() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getDataByNet();
                smartRefreshLayout.finishRefresh(1000);
                refreshlayout.finishRefresh(2000);
            }
        });

    }

    private void setAdapter() {
        adapter = new CommonAdapter<NewHouseInfo>(getActivity(),
                R.layout.item_rv_fangyuan, datas) {
            @Override
            protected void convert(ViewHolder holder, NewHouseInfo houseInfo, int position) {
                holder.setText(R.id.tv_item_fangyuan_title, houseInfo.getTitle());
                holder.setText(R.id.tv_item_fangyuan_price, subZeroAndDot(houseInfo.getTotalprice()));
                holder.setText(R.id.tv_item_fangyuan_msg, houseInfo.getRoom() + "室" + houseInfo.getHall() +
                        "厅" + houseInfo.getToilet() + "卫 | " + subZeroAndDot(houseInfo.getAcreage()) + "㎡|第" + houseInfo.getMstorey() + "层/共" + houseInfo.getSumfloor() + "层");
                holder.setText(R.id.tv_item_fangyuan_address, houseInfo.getMem_address());
                holder.setText(R.id.tv2, houseInfo.getBrowse_count() + "");
                holder.setText(R.id.tv1, houseInfo.getCollect_count() + "");
                Glide.with(mContext)
                        .load(houseInfo.getPiclogo())
                        .placeholder(R.mipmap.icon_fang_defout)
                        .error(R.mipmap.icon_fang_defout)
                        .into(((ImageView) holder.getView(R.id.iv_item_fangyuan)));
                String application = houseInfo.getApplication();
                String type = "";
                switch (application) {
                    case "1":
                        type = "单体别墅";
                        break;
                    case "2":
                        type = "排屋";
                        break;
                    case "3":
                        type = "多层";
                        break;
                    case "4":
                        type = "复式";
                        break;
                    case "5":
                        type = "小高楼";
                        break;
                    case "6":
                        type = "写字楼";
                        break;
                    case "7":
                        type = "店面";
                        break;
                }
                holder.setText(R.id.tv_type, type);

            }
        };
        list.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("istatle", "房屋详情");
                intent.putExtra("houseId", String.valueOf(datas.get(position - 1).getId()));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.lunbo_layout, null);
        ivBanner = (CustomBanner) headView.findViewById(R.id.iv_fm_banner);
        tvMore = (TextView) headView.findViewById(R.id.tv_fm_first_more);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchMoreActivity.class));
            }
        });
        recy = (RecyclerView) headView.findViewById(R.id.re);
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mJc = (JCVideoPlayerStandard) headView.findViewById(R.id.videoplayer);
        mJc.backButton.setVisibility(View.GONE);
        mJc.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mHeaderAndFooterWrapper.addHeaderView(headView);
        list.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        getBanner();
    }

    private void getBanner() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + SHOUYELUNBOTU)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        newHouseInfos = JSON.parseArray(body, BannerVo.class);
                        setBannerAdapter();
                        gettubiao();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void gettubiao() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + TUBIAO)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {

                        tubiaoVos = JSON.parseArray(body, TubiaoVo.class);
                        Log.e("000", "onSuccess: " + tubiaoVos.size());
                        settubiaoadapter();
                        getUrlByNet();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void settubiaoadapter() {
        recyadapter = new CommonAdapter<TubiaoVo>(getActivity(),
                R.layout.item_tubiao, tubiaoVos) {
            @Override
            protected void convert(ViewHolder holder, TubiaoVo tubiaoVo, int position) {
                holder.setText(R.id.te, tubiaoVo.getName() + "");
                Glide.with(mContext)
                        .load(tubiaoVo.getPhoto())
                        .into(((ImageView) holder.getView(R.id.im)));
            }
        };
        recy.setAdapter(recyadapter);
        recyadapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (tubiaoVos.get(position).getNeedLogin() == 1) {
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    if (tubiaoVos.get(position).getCode().equals("1")) {
                        startActivity(new Intent(getActivity(), SearchMoreActivity.class));
                    } else if (tubiaoVos.get(position).getCode().equals("2")) {
                        startActivity(new Intent(getActivity(), MyEntrustActivity.class));
                    } else if (tubiaoVos.get(position).getCode().equals("3")) {
                        startActivity(new Intent(getActivity(), HouserActivity.class));
                    } else if (tubiaoVos.get(position).getCode().equals("4")) {
                        startActivity(new Intent(getActivity(), TransactionRecordActivity.class));
                    } else {
                        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
                        Intent intent2 = new Intent(getActivity(), WebViewActivity2.class);
                        intent2.putExtra("istatle", tubiaoVos.get(position).getName());
                        intent2.putExtra("url", BaseURL.BASE_URL + tubiaoVos.get(position).getUrl() +"?uuid="+ userInfo.getUuid());
                        startActivity(intent2);
                    }
                } else {
                    if (tubiaoVos.get(position).getCode().equals("5")) {
                        Intent intent3 = new Intent(getActivity(), WebViewActivity2.class);
                        intent3.putExtra("istatle", "贷款计算器");
                        intent3.putExtra("url", BaseURL.BASE_URL + "/api/loanCalculator");
                        startActivity(intent3);
                    } else if (tubiaoVos.get(position).getCode().equals("6")) {
                        Intent intent1 = new Intent(getActivity(), WebViewActivity2.class);
                        intent1.putExtra("istatle", "交易流程");
                        intent1.putExtra("url", BaseURL.BASE_URL + "/api/house/process");
                        startActivity(intent1);
                    } else if (tubiaoVos.get(position).getCode().equals("7")) {
                        Intent intent2 = new Intent(getActivity(), WebViewActivity2.class);
                        intent2.putExtra("istatle", "收费标准");
                        intent2.putExtra("url", BaseURL.BASE_URL + "/api/house/standard");
                        Log.d("收费标准", "onItemClick: ");
                        startActivity(intent2);
                    }
                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void getUrlByNet() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + VIDEO)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("000", "onSuccess: " + body);
                        videoVos = JSON.parseObject(body, VideoVo.class);
                        if (videoVos.getUrl() != null) {
                            mJc.setUp(videoVos.getUrl(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                        }
                        if (videoVos.getPic() != null) {
                            Glide.with(getActivity())
                                    .load(videoVos.getPic())
                                    .into(mJc.thumbImageView);
                        } else {
                            Glide.with(getActivity())
                                    .load(R.drawable.img_video)
                                    .into(mJc.thumbImageView);
                        }
//                        getDataByNet();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(), returnTip);

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void setBannerAdapter() {
        ivBanner.setPages(new CustomBanner.ViewCreator<BannerVo>() {
            @Override
            public View createView(Context context, int i) {
                ImageView imageView = new ImageView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int i, BannerVo bannerVo) {
                Glide.with(context).load(bannerVo.getPictureroot())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(R.drawable.baidian)
                        .into((ImageView) view);
            }
        }, newHouseInfos);
        if (newHouseInfos.size() == 1) {   //只有一张轮播图   不滚动
            ivBanner.stopTurning();
        } else {
            ivBanner.startTurning(5000);
            //设置轮播图的滚动速度
            ivBanner.setScrollDuration(300);
        }
        ivBanner.setOnPageClickListener(new CustomBanner.OnPageClickListener<BannerVo>() {
            @Override
            public void onPageClick(int position, BannerVo str) {
                if(str.getAddress().equals("")){
                    return;
                }
                if(str.getAddress().substring(0,7).equals("houseid")){
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("istatle", "房屋详情");
                    intent.putExtra("houseId", str.getAddress().substring(8,str.getAddress().length()));
                    startActivity(intent);
                }else {
                    if (str.getNeedLogin() == 1) {
                        if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            return;
                        }
                        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
                        Intent intent = new Intent(getActivity(), WebViewActivity4.class);
                        intent.putExtra("url", str.getAddress() + "?uuid="+ userInfo.getUuid());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), WebViewActivity4.class);
                        intent.putExtra("url", str.getAddress());
                        startActivity(intent);
                    }

                }

            }
        });
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    @Override
    protected void initClick() {


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause()
            JCVideoPlayer.releaseAllVideos();
        } else {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


}
