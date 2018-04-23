package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donkingliang.banner.CustomBanner;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.BannerVo;
import meijia.com.meijianet.ui.HouseDetailActivity;
import meijia.com.meijianet.activity.MyScollerLinearlayoutManager;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.activity.SearchMoreAdapter;
import meijia.com.meijianet.api.OnItemClickListener;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.ui.PostHouseActivity;
import meijia.com.meijianet.ui.SearchActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.ui.SellerNoticeActivity;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/5
 */
public class FirstFragment extends BaseFragment implements OnItemClickListener {
    private LinearLayout llParent;
    private TextView tvAddress;
    private TextView tvSearch;
    private TextView tvMore;

    private CustomBanner ivBanner;
    private int mWidth;
    private PopupWindow mPopupWindow;

//    private ImageView ivSell;

    private List<NewHouseInfo> datas = new ArrayList<>();
    private RecyclerView rvList;
    private SearchMoreAdapter mAdapter;
    private MyScollerLinearlayoutManager mManager;
    private JCVideoPlayerStandard mJc;
    private String videoUrl = "https://mjkf.oss-cn-beijing.aliyuncs.com/mjw-video/index.mp4" ;
    private List<BannerVo> newHouseInfos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first_fragment,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);
        llParent = (LinearLayout) view.findViewById(R.id.ll_parent);
//        ivSell = (ImageView) view.findViewById(R.id.iv_sell);
        tvMore = (TextView) view.findViewById(R.id.tv_fm_first_more);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvSearch = (TextView) view.findViewById(R.id.tv_fm_first_search);
        ivBanner = (CustomBanner) view.findViewById(R.id.iv_fm_banner);
//        ivBanner.setBackgroundColor(Color.BLUE);
        mJc = (JCVideoPlayerStandard) view.findViewById(R.id.videoplayer);
        mJc.backButton.setVisibility(View.GONE);
        mJc.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mJc.setUp("https://mjkf.oss-cn-beijing.aliyuncs.com/mjw-video/index.mp4",JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");
        Glide.with(getActivity())
                .load(R.drawable.img_video)
                .into(mJc.thumbImageView);


    }
    private int mHeight;
    @Override
    protected void initData() {
//
//        llParent.post(new Runnable() {
//            @Override
//            public void run() {
//                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity()), 0, 0);
//            }
//        });
        tvAddress.post(new Runnable() {
            @Override
            public void run() {
                mHeight = tvAddress.getHeight();
                mWidth = tvAddress.getWidth();
            }
        });

        mAdapter = new SearchMoreAdapter(getActivity(),datas);
        mManager = new MyScollerLinearlayoutManager(getActivity());
        mManager.setVerticalScrollFlag(false);
        rvList.setLayoutManager(mManager);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        getDataByNet();
        getUrlByNet();
        getBanner();
    }

    private void getBanner() {
        OkHttpUtils.post()
                .tag(this)
                .url("http://192.168.1.58:8080/api/carousel/getcarousels")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                      newHouseInfos = JSON.parseArray(body, BannerVo.class);
                        setBannerAdapter();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

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
            ivBanner.startTurning(4000);
            //设置轮播图的滚动速度
            ivBanner.setScrollDuration(300);
        }
    }


    @Override
    protected void initClick() {
        tvSearch.setOnClickListener(this);
        tvMore.setOnClickListener(this);

//        ivSell.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_fm_first_search:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    break;
                case R.id.tv_fm_first_more:
                    startActivity(new Intent(getActivity(), SearchMoreActivity.class));
                    break;
                case R.id.tv_address:
                    showPop(tvAddress,mWidth,mHeight);
                    break;
//                case R.id.iv_sell:
//                    if (!SharePreUtil.isFisrtSell(getActivity())){
//                        SharePreUtil.setFirstSell(getActivity(),true);
//                        startActivity(new Intent(getActivity(),SellerNoticeActivity.class));
//                    }else {
//                        startActivity(new Intent(getActivity(),PostHouseActivity.class));
//                    }
//                    break;
                default:
                    break;
            }
        }
    }

    private void showPop(TextView tvAddress,int width,int height) {

        TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
        mPopupWindow = new PopupWindow(view,
                width, height, true);
        mPopupWindow.setTouchable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.pop_bg));
        // 设置好参数之后再show
        mPopupWindow.showAsDropDown(tvAddress);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(view.getText().toString().trim());
                if (mPopupWindow!=null){
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(null,null,
                        getResources().getDrawable(R.mipmap.home_icon_more_normal),null);
            }
        });


        if (mPopupWindow!=null){
            if (mPopupWindow.isShowing()){
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(null,null,
                        getResources().getDrawable(R.mipmap.home_icon_more_pressed),null);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();

    }

    private void getUrlByNet() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + VIDEO)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("000", "onSuccess: "+body );
                        videoUrl = body;

                        mJc.setUp(body,JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(),returnTip);
                        mJc.setUp(videoUrl,JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");
                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }

    private void getDataByNet() {

        //检查网络
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(getActivity(),false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + NEW_HOUSE)
                .addParams("flag", "true")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        List<NewHouseInfo> newHouseInfos = JSON.parseArray(body, NewHouseInfo.class);
                        if (newHouseInfos!=null && newHouseInfos.size()>0){
                            for (int i = 0; i < newHouseInfos.size(); i++) {
                                if (i <3){
                                    datas.add(newHouseInfos.get(i));
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(),returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });


    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(),HouseDetailActivity.class);
        intent.putExtra("id",datas.get(position).getId());
        startActivity(intent);
    }
}

