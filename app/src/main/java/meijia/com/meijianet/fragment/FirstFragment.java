package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donkingliang.banner.CustomBanner;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.BannerVo;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.activity.MyScollerLinearlayoutManager;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.MyEntrustActivity;
import meijia.com.meijianet.ui.TransactionRecordActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.activity.SearchMoreAdapter;
import meijia.com.meijianet.api.OnItemClickListener;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.ui.PostHouseActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
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
//    private String videoUrl = "https://mjkf.oss-cn-beijing.aliyuncs.com/mjw-video/index.mp4" ;
    private List<BannerVo> newHouseInfos;
    private ImageView fistfragmentdkjsq;
    private ImageView fistfragmentjylc;
    private ImageView fistfragmentsfbz;
    private static final int FIRST = 0; //首页
    private static final int SECOND = 1;//我的
    private static final int THIRD = 2;//房小二
    private static final int FOUR = 3;//买房
    private static final int FIVE = 4;//卖房
    private FirstFragment mHomeFragment;
    private MyFragment mMeFragment;
    private HouserFragment houserFragment;
    private BuyHomeFragment buyHomeFragment;
    private SellingHomeFragment sellingHomeFragment;
    private ImageView fistfragmentesf;
    private ImageView fistfragmentmf;
    private ImageView fistfragmentkf;
    private ImageView fistfragmentzjcj;
    private VideoVo videoVos;


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
        tvMore = (TextView) view.findViewById(R.id.tv_fm_first_more);
        ivBanner = (CustomBanner) view.findViewById(R.id.iv_fm_banner);
        fistfragmentdkjsq = (ImageView) view.findViewById(R.id.fist_fragment_dkjsq);
        fistfragmentjylc = (ImageView) view.findViewById(R.id.fist_fragment_jylc);
        fistfragmentsfbz = (ImageView) view.findViewById(R.id.fist_fragment_sfbz);
        fistfragmentesf = (ImageView) view.findViewById(R.id.fist_fragment_esf);
        fistfragmentmf = (ImageView) view.findViewById(R.id.fist_fragment_mf);
        fistfragmentkf = (ImageView) view.findViewById(R.id.fist_fragment_kf);
        fistfragmentzjcj = (ImageView) view.findViewById(R.id.fist_fragment_zjcj);

        mJc = (JCVideoPlayerStandard) view.findViewById(R.id.videoplayer);
        mJc.backButton.setVisibility(View.GONE);
        mJc.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        mJc.setUp("https://mjkf.oss-cn-beijing.aliyuncs.com/mjw-video/index.mp4",JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");

    }
    private int mHeight;
    @Override
    protected void initData() {
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
                .url(BaseURL.BASE_URL + SHOUYELUNBOTU)
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
        ivBanner.setOnPageClickListener(new CustomBanner.OnPageClickListener<BannerVo>() {
            @Override
            public void onPageClick(int position, BannerVo str) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("istatle", "百度");
                intent.putExtra("url",str.getAddress());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void initClick() {
        fistfragmentesf.setOnClickListener(this);
        fistfragmentmf.setOnClickListener(this);
        fistfragmentkf.setOnClickListener(this);
        fistfragmentdkjsq.setOnClickListener(this);
        fistfragmentjylc.setOnClickListener(this);
        fistfragmentsfbz.setOnClickListener(this);
        fistfragmentzjcj.setOnClickListener(this);
        tvMore.setOnClickListener(this);

//        ivSell.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {

                case R.id.tv_fm_first_more:
                    startActivity(new Intent(getActivity(), SearchMoreActivity.class));
                    break;

//                case R.id.iv_sell:
//                    if (!SharePreUtil.isFisrtSell(getActivity())){
//                        SharePreUtil.setFirstSell(getActivity(),true);
//                        startActivity(new Intent(getActivity(),SellerNoticeActivity.class));
//                    }else {
//                        startActivity(new Intent(getActivity(),PostHouseActivity.class));
//                    }
//                    break;
                case R.id.fist_fragment_dkjsq:
                    Intent intent3 = new Intent(getActivity(),WebViewActivity.class);
                    intent3.putExtra("istatle", "贷款计算器");
                    intent3.putExtra("url",BaseURL.BASE_URL+"/api/loanCalculator");
                    startActivity(intent3);

                    break;
                case R.id.fist_fragment_jylc:
                    Intent intent1 = new Intent(getActivity(),WebViewActivity.class);
                    intent1.putExtra("istatle", "交易流程");
                    intent1.putExtra("url",BaseURL.BASE_URL+"/api/house/process");
                    startActivity(intent1);
                    break;
                case R.id.fist_fragment_sfbz:

                    Intent intent2 = new Intent(getActivity(),HouserActivity.class);
                    intent2.putExtra("istatle", "收费标准");
                    intent2.putExtra("url",BaseURL.BASE_URL+"/api/house/standard");
                    startActivity(intent2);
                    break;
                case R.id.fist_fragment_esf:
                    startActivity(new Intent(getActivity(), SearchMoreActivity.class));
                    break;
                case R.id.fist_fragment_mf:
                    if (SharePreUtil.getUserInfo(getActivity()).getName().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(),MyEntrustActivity.class));
                    break;
                case R.id.fist_fragment_kf:
                    startActivity(new Intent(getActivity(), HouserActivity.class));
                    break;
                case R.id.fist_fragment_zjcj:
                    startActivity(new Intent(getActivity(), TransactionRecordActivity.class));
                    break;

                default:
                    break;
            }
        }
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

    private void getUrlByNet() {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + VIDEO)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.e("000", "onSuccess: "+body );
                         videoVos = JSON.parseObject(body, VideoVo.class);
                            if(videoVos.getUrl()!=null){
                                mJc.setUp(videoVos.getUrl(),JCVideoPlayer.SCREEN_LAYOUT_NORMAL,"");
                            }
                        if(videoVos.getPic()!=null){
                            Glide.with(getActivity())
                                    .load(videoVos.getPic())
                                    .into(mJc.thumbImageView);
                        }else {
                            Glide.with(getActivity())
                                    .load(R.drawable.img_video)
                                    .into(mJc.thumbImageView);
                        }

                    }
                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(getActivity(),returnTip);

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
                                if (i <=10){
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
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
        intent.putExtra("istatle", "房屋详情");
        intent.putExtra("houseId",String.valueOf(datas.get(position).getId()) );
        if (!userInfo.getUuid().equals("")){
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(position).getId()+"&uuid="+userInfo.getUuid());
        }else {
            intent.putExtra("url",BaseURL.BASE_URL+"/api/house/houseDetail?id="+datas.get(position).getId()+"&uuid="+"");
        }
        startActivity(intent);
    }
}

