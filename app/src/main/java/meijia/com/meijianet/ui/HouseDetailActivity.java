package meijia.com.meijianet.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.HouseDetailVo;
import meijia.com.meijianet.activity.MyScrollView;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.DisplayUtil;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.TimeUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.commondialog.CommonDialog;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class HouseDetailActivity extends BaseActivity implements MyScrollView.onMyScrollListener, ViewPager.OnPageChangeListener {

    private MyScrollView mScrollView;
    private RelativeLayout rlView;
    private TextView tvIndcator;
    private TextView tvMiaoshu;
    private TextView tvHuxingtu;
    private TextView tvYewu;
    private Banner ivBanner;
    private RelativeLayout rlCollect;
    private TextView tvCollect;
    private TextView tvPrice;
    private TextView tvTitle;
    private TextView tvMap;
    private TextView tvHuxing;
    private TextView tvZhuangxiu;
    private TextView tvType;
    private TextView tvQishui;
    private TextView tvPersonShui;
    private TextView tvZengzhishui;
    private TextView tvGongben;
    private TextView tvMoney;
    private TextView tvMianji;
    private TextView tvLouceng;
    private TextView tvChangxiang;
    private TextView tvAddress;
    private TextView tvPay;
    private TextView tvDay,tvHour,tvMinute,tvSecond;
    private TextView tvStatus;
    private TextView tvJisuanqi;
    private ImageView ivBack, ivShare;
    private LinearLayout llPay;
    private LinearLayout llTime;
    private CommonDialog mDialog;


    private float height = 0;
    private int mStatuBarHeight;
    private int rlHeight;
    private View statusView;
    private List<Integer> images = new ArrayList<>();
    private int count = 0;
    private long mId;
    private boolean isCollect = false;
    private String webUrl = "";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            int what = msg.what;
            if (what == 1){
                Log.e(TAG, "handleMessage: " );
                Bundle data = msg.getData();
                long millis = data.getLong("m");
                int ss = 1000;
                int mi = ss * 60;
                int hh = mi * 60;
                int dd = hh * 24;

                long day = millis / dd;
                long hour = (millis - day * dd) / hh;
                long minute = (millis - day * dd - hour * hh) / mi;
                long second = (millis - day * dd - hour * hh - minute * mi) / ss;
                long milliSecond = millis - day * dd - hour * hh - minute * mi - second * ss;
                tvDay.setText(day+"");
                tvHour.setText(""+hour);
                tvMinute.setText(""+minute);
                tvSecond.setText(""+second);

                millis -= 1000;
                Message message = mHandler.obtainMessage();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putLong("m",millis);
                message.setData(bundle);
                mHandler.sendMessageDelayed(message,1000);
            }

            super.handleMessage(msg);

        }
    };

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_house_detail);
        StatusBarUtils.setActivityTranslucent(this);
        ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            statusView = contentView.getChildAt(1);
            statusView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        } else {
            statusView = StatusBarUtils.createTranslucentStatusBarView(this, 0);
            contentView.addView(statusView);
        }
    }


    @Override
    protected void initView() {
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        llPay = (LinearLayout) findViewById(R.id.ll_shouchang_pay);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvJisuanqi = (TextView) findViewById(R.id.tv_ac_housedetail_jisuanqi);
        tvPay = (TextView) findViewById(R.id.tv_ac_housedetail_pay);
        tvDay = (TextView) findViewById(R.id.tv_ac_housedetail_day);
        tvMinute = (TextView) findViewById(R.id.tv_ac_housedetail_minute);
        tvHour = (TextView) findViewById(R.id.tv_ac_housedetail_hour);
        tvSecond = (TextView) findViewById(R.id.tv_ac_housedetail_second);
        tvQishui = (TextView) findViewById(R.id.tv_ac_housedetail_qishui);
        tvPersonShui = (TextView) findViewById(R.id.tv_ac_housedetail_personshui);
        tvZengzhishui = (TextView) findViewById(R.id.tv_ac_housedetail_zengzhishui);
        tvGongben = (TextView) findViewById(R.id.tv_ac_housedetail_gongbenfei);
        tvMoney = (TextView) findViewById(R.id.tv_ac_housedetail_daikuan);
        tvLouceng = (TextView) findViewById(R.id.tv_ac_housedetail_floor);
        tvChangxiang = (TextView) findViewById(R.id.tv_ac_housedetail_fangxiang);
        tvAddress = (TextView) findViewById(R.id.tv_ac_housedetail_address);
        tvMianji = (TextView) findViewById(R.id.tv_ac_housedetail_mianji);
        tvType = (TextView) findViewById(R.id.tv_ac_housedetail_type);
        tvZhuangxiu = (TextView) findViewById(R.id.tv_ac_housedetail_zhuangxiu);
        tvHuxing = (TextView) findViewById(R.id.tv_ac_housedetail_huxing);
        tvMap = (TextView) findViewById(R.id.tv_ac_housedetail_map);
        tvTitle = (TextView) findViewById(R.id.tv_ac_housedetail_title);
        tvPrice = (TextView) findViewById(R.id.tv_ac_housedetail_price);
        tvCollect = (TextView) findViewById(R.id.tv_ac_housedetail_shoucang);
        rlCollect = (RelativeLayout) findViewById(R.id.rl_ac_housedetail_shoucang);
        tvYewu = (TextView) findViewById(R.id.tv_ac_housedetail_yewuyaoqiu);
        tvHuxingtu = (TextView) findViewById(R.id.tv_ac_housedetail_huxingtu);
        tvMiaoshu = (TextView) findViewById(R.id.tv_ac_housedetail_miaoshu);
        tvIndcator = (TextView) findViewById(R.id.tv_indcator);
        mScrollView = (MyScrollView) findViewById(R.id.my_scroll_view);
        rlView = (RelativeLayout) findViewById(R.id.rl_layout);
        ivBanner = (Banner) findViewById(R.id.iv_banner);
        mScrollView.setOnMyScrollListener(this);
    }

    @Override
    protected void initData() {
        ivBanner.post(new Runnable() {
            @Override
            public void run() {
                rlHeight = rlView.getMeasuredHeight();
                height = ivBanner.getMeasuredHeight();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlView.getLayoutParams();
                mStatuBarHeight = DisplayUtil.getStatuBarHeight(HouseDetailActivity.this);
                layoutParams.topMargin = mStatuBarHeight;
                rlView.setLayoutParams(layoutParams);
            }
        });

        mId = getIntent().getLongExtra("id", 0);
        getDataByNet(mId);
        images.add(R.drawable.pic_home_bg);
        images.add(R.drawable.pic_flow2);
        images.add(R.drawable.home_sell);

        ivBanner.isAutoPlay(false);
        ivBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        ivBanner.setOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getDataByNet(long id) {
        if (!NetworkUtil.checkNet(HouseDetailActivity.this)) {
            ToastUtil.showShortToast(HouseDetailActivity.this, "没网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(HouseDetailActivity.this, false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + HOUSE_DETAIL)
                .addParams("id", id + "")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        HouseDetailVo houseDetailVo = JSON.parseObject(body, HouseDetailVo.class);
                        setData(houseDetailVo);
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(HouseDetailActivity.this, returnTip);
                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });

    }

    private void setData(HouseDetailVo vo) {
        if (vo.getBannerLi() != null && vo.getBannerLi().size() > 0) {
            tvIndcator.setVisibility(View.VISIBLE);
            count = vo.getBannerLi().size();
            tvIndcator.setText("1/" + vo.getBannerLi().size());
            ivBanner.setImages(vo.getBannerLi()).setImageLoader(new GlideImageLoader()).start();
        } else {
            tvIndcator.setVisibility(View.VISIBLE);
            count = 3;
            ivBanner.setImages(images).setImageLoader(new GlideImageLoader()).start();
            tvIndcator.setText("1/3");
        }
        tvMiaoshu.setText(vo.getHouseMeno());

        tvHuxingtu.setText(vo.getHouseLayout());

        tvYewu.setText(vo.getRequirements());

        tvPrice.setText("¥" + vo.getTotalprice() + "万");
        setTextBigSize(tvPrice, 0, tvPrice.getText().toString().trim().length() - 1, 20, "");
        tvTitle.setText(vo.getTitle());
        tvHuxing.setText("户型：" + vo.getRoom() + "室" + vo.getHall() + "厅" + vo.getToilet() + "卫");
        setTextBigSize(tvHuxing, 3, tvHuxing.getText().toString().trim().length(), 0, "#333333");
        int decoration = vo.getDecoration();
        String decora = "";
        /**
         * 1 毛坯, 2 简单装修 ,3 中档装修 ,4 精装修,5 豪华装修
         */
        switch (decoration) {
            case 1:
                decora = "毛坯";
                break;
            case 2:
                decora = "简单装修";
                break;
            case 3:
                decora = "中档装修";
                break;
            case 4:
                decora = "精装修";
                break;
            case 5:
                decora = "豪华装修";
                break;
        }
        tvZhuangxiu.setText("装修：" + decora);
        setTextBigSize(tvZhuangxiu, 3, tvZhuangxiu.getText().toString().trim().length(), 0, "#333333");
        tvType.setText("房屋类型：" + vo.getApplication());
        setTextBigSize(tvType, 5, tvType.getText().toString().trim().length(), 0, "#333333");
        tvMianji.setText("面积：" + vo.getArea() );
        setTextBigSize(tvMianji, 3, tvMianji.getText().toString().trim().length(), 0, "#333333");
        tvLouceng.setText("楼层：" + vo.getStorey() + "楼（共" + vo.getSumfloor() + "楼）");
        setTextBigSize(tvLouceng, 3, tvLouceng.getText().toString().trim().length(), 0, "#333333");
        tvChangxiang.setText("朝向：" + vo.getOrientation());
        setTextBigSize(tvChangxiang, 3, tvChangxiang.getText().toString().trim().length(), 0, "#333333");
        tvAddress.setText("地址：" + vo.getAddress());
        setTextBigSize(tvAddress, 3, tvAddress.getText().toString().trim().length(), 0, "#333333");
        tvQishui.setText("契税：" + vo.getAgreementtax() + "%(首套)");
        setTextBigSize(tvQishui, 3, tvQishui.getText().toString().trim().length(), 0, "#333333");
        tvPersonShui.setText("个人所得税：" + vo.getTax() + "%");
        setTextBigSize(tvPersonShui, 6, tvPersonShui.getText().toString().trim().length(), 0, "#333333");
        tvZengzhishui.setText("增值税：" + vo.getLandtax() + "%");
        setTextBigSize(tvZengzhishui, 4, tvZengzhishui.getText().toString().trim().length(), 0, "#333333");
        tvGongben.setText("工本费：" + vo.getFlatcost() + "元");
        setTextBigSize(tvGongben, 4, tvGongben.getText().toString().trim().length(), 0, "#333333");
        tvMoney.setText("银行最高贷款金额：" + vo.getMaxloan() + "万");
        setTextBigSize(tvMoney, 9, tvMoney.getText().toString().trim().length(), 0, "#333333");
        webUrl = vo.getMapurl();
        if (vo.getStatus() == 1){//已成交
            tvStatus.setVisibility(View.VISIBLE);
            llPay.setVisibility(View.GONE);
            llTime.setVisibility(View.INVISIBLE);
        }else {
            llPay.setVisibility(View.VISIBLE);
            tvStatus.setVisibility(View.INVISIBLE);

            String preDowntime = vo.getPreDowntime();
            long millis = TimeUtil.getTimeMillis(preDowntime, "yyyy-MM-dd HH:mm:ss");
            long currentTimeMillis = System.currentTimeMillis();
            long ms = millis - currentTimeMillis;
            if (ms<=0){
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("已结束");
                llPay.setVisibility(View.GONE);
                llTime.setVisibility(View.INVISIBLE);
                return;
            }
            int ss = 1000;
            int mi = ss * 60;
            int hh = mi * 60;
            int dd = hh * 24;

            long day = ms / dd;
            long hour = (ms - day * dd) / hh;
            long minute = (ms - day * dd - hour * hh) / mi;
            long second = (ms - day * dd - hour * hh - minute * mi) / ss;
            tvDay.setText(day+"");
            tvHour.setText(""+hour);
            tvMinute.setText(""+minute);
            tvSecond.setText(""+second);
            //发送消息
            Message message = mHandler.obtainMessage();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putLong("m",ms);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }

        if (vo.getCollectStatus() == 0){
            isCollect = false;
            tvCollect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.details_icon_collect_default), null
                    , null, null);
        }else if (vo.getCollectStatus() == 1){
            isCollect = true;
            tvCollect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.details_icon_collect_selected), null
                    , null, null);
        }
    }


    @Override
    protected void initClick() {
        rlCollect.setOnClickListener(this);
        tvMap.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        tvJisuanqi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.rl_ac_housedetail_shoucang:
                    if (isCollect) {
                        collectHouse("houseids",mId, DELETE_COLLECT, false);
                    } else {
                        collectHouse("id",mId, COLLECT_HOUSE, true);
                    }

                    break;
                case R.id.tv_ac_housedetail_jisuanqi:
                    startActivity(new Intent(this, RefundActivity.class));
                    break;
                case R.id.tv_ac_housedetail_map:
                    if (webUrl.equals("")){
                        ToastUtil.showShortToast(HouseDetailActivity.this,"没有找到地图");
                        return;
                    }
                    Intent intent = new Intent(HouseDetailActivity.this, MapActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.iv_share:
                    showBroadView();
                    break;
                case R.id.tv_ac_housedetail_pay:
                    if (SharePreUtil.getUserInfo(HouseDetailActivity.this).getUuid().equals("")){
                        ToastUtil.showShortToast(HouseDetailActivity.this,"您还没有登录");
                        return;
                    }
                    Intent payIntent = new Intent(HouseDetailActivity.this, PayActivity.class);
                    payIntent.putExtra("id",mId);
                    startActivity(payIntent);
                    break;
                case R.id.tv_wx:
                    requestRuntimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            setShare(0);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            PromptUtil.showCommonDialog(HouseDetailActivity.this, "请在设置中打开内存卡读写权限", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    });

                    break;
                case R.id.tv_qq:
                    requestRuntimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            setShare(2);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            PromptUtil.showCommonDialog(HouseDetailActivity.this, "请在设置中打开内存卡读写权限", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    });
                    break;
                case R.id.tv_wxc:
                    requestRuntimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            setShare(1);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            PromptUtil.showCommonDialog(HouseDetailActivity.this, "请在设置中打开内存卡读写权限", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void setShare(int type) {
        mDialog.dismiss();
    }

    private void collectHouse(String params,long id, String url, boolean isC) {
        if (!NetworkUtil.checkNet(HouseDetailActivity.this)) {
            ToastUtil.showShortToast(HouseDetailActivity.this, "没网了，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(HouseDetailActivity.this, false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + url)
                .addParams(params, id + "")
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        if (isC) {
                            isCollect = true;
                            ToastUtil.showShortToast(HouseDetailActivity.this, "收藏成功");
                            tvCollect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.details_icon_collect_selected), null
                                    , null, null);
                        } else {
                            isCollect = false;
                            ToastUtil.showShortToast(HouseDetailActivity.this, "删除收藏");
                            tvCollect.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.details_icon_collect_default), null
                                    , null, null);
                        }

                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(HouseDetailActivity.this, returnTip);

                        PromptUtil.closeTransparentDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }

    @Override
    public void onScrollChange(int l, int t, int oldl, int oldt) {
        float h = height - mStatuBarHeight - rlHeight;
        if (t <= 0) {
            rlView.setBackgroundColor(Color.argb(0, 255, 255, 255));
        } else if (t > 0 && t < h) {
            float alpha = t * 1.0f / h;
            rlView.setBackgroundColor(Color.argb(((int) (alpha * 255)), 255, 255, 255));
            statusView.setBackgroundColor(Color.argb(((int) (alpha * 255)), 120, 120, 120));
        } else {
            rlView.setBackgroundColor(Color.argb(255, 255, 255, 255));
            statusView.setBackgroundColor(Color.argb(255, 120, 120, 120));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvIndcator.setText((position + 1) + "/" + count);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }


    private void setTextBigSize(TextView textView, int from, int end, int size, String color) {
        String text = textView.getText().toString().trim();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (size > 0) {
            AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);
            builder.setSpan(sizeSpan, from, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (!color.equals("")) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
            builder.setSpan(colorSpan, from, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        textView.setText(builder);
    }

    private void showBroadView() {
        View view = LayoutInflater.from(HouseDetailActivity.this).inflate(R.layout.layout_share, null);
        view.findViewById(R.id.tv_wx).setOnClickListener(this);
        view.findViewById(R.id.tv_qq).setOnClickListener(this);
        view.findViewById(R.id.tv_wxc).setOnClickListener(this);
        view.findViewById(R.id.tv_share_cancle).setOnClickListener(this);
        mDialog = new CommonDialog(HouseDetailActivity.this,view, 0, Gravity.BOTTOM);
        mDialog.show();
    }

}
