package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.NewHouseInfo;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.MyBrowseActivity;
import meijia.com.meijianet.ui.MyCollectActivity;
import meijia.com.meijianet.ui.MyEntrustActivity;
import meijia.com.meijianet.ui.MyIntentionActivity;
import meijia.com.meijianet.ui.PersonCenterActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.ui.WebViewActivity2;
import meijia.com.meijianet.ui.textone;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;

import static meijia.com.meijianet.api.URL.PHONE_CODE;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/5
 */
public class MyFragment extends TakePhotoFragment implements View.OnClickListener {


    private View view;
    private TextView tvBrowse;
    private TextView tvCollect;
    private TextView ivSet;
    private RoundedImageView ivIcon;
    private TextView tvNameOrStatus;
    private TextView tvMyEntrust;
    private TextView tvMyIntention;
    private TextView tvLvshi;

    private TextView tvBiaozhun;
    private TextView tvDaikuan;
    private TextView fmmyphone;
    private TextView fmmynickname;
    private LinearLayout rlBanner;
    private TextView tvjyliucheng;
    private ScrollView linear;
    private ImageView callphone;
    private TextView text;
    private TextView phonecode;
    private String phone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        initView();
        initData();
        initClick();
        return view;
    }

    private void initView() {
        linear = (ScrollView)view.findViewById(R.id.linear);
        tvBrowse = (TextView) view.findViewById(R.id.tv1);
        tvCollect = (TextView) view.findViewById(R.id.tv_fm_my_collect);
        rlBanner = (LinearLayout) view.findViewById(R.id.rl_banner);
        ivSet = (TextView) view.findViewById(R.id.iv_fm_my_set);
        ivSet.setVisibility(View.GONE);
        ivIcon = (RoundedImageView) view.findViewById(R.id.riv_fm_my_icon);
        tvNameOrStatus = (TextView) view.findViewById(R.id.tv_fm_my_nickname);
        tvNameOrStatus.setVisibility(View.VISIBLE);
        tvMyEntrust = (TextView) view.findViewById(R.id.tv_fm_my_weituo);
        tvMyIntention = (TextView) view.findViewById(R.id.tv_fm_my_yixiang);
        tvLvshi = (TextView) view.findViewById(R.id.tv_lvshi);
        tvjyliucheng = (TextView) view.findViewById(R.id.tv_jyliucheng);
        tvBiaozhun = (TextView) view.findViewById(R.id.tv_biaozhun);
        tvDaikuan = (TextView) view.findViewById(R.id.tv_daikuan);
        fmmyphone = (TextView) view.findViewById(R.id.fm_my_phone);
        fmmyphone.setVisibility(View.GONE);
        fmmynickname = (TextView) view.findViewById(R.id.fm_my_nickname);
        fmmynickname.setVisibility(View.GONE);
        callphone = (ImageView) view.findViewById(R.id.call_phone);
        text = (TextView) view.findViewById(R.id.text);
        phonecode = (TextView) view.findViewById(R.id.phone_code);

    }

    @Override
    public void onResume() {
        super.onResume();
        getphone();
        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
        if (!userInfo.getUuid().equals("")) {
            ivSet.setVisibility(View.VISIBLE);
            tvNameOrStatus.setVisibility(View.GONE);
            fmmyphone.setVisibility(View.VISIBLE);
            fmmynickname.setVisibility(View.VISIBLE);
            fmmyphone.setText(userInfo.getPhone().equals("") ? "" : ToolUtil.getMosaicPhone(userInfo.getPhone()));
            fmmynickname.setText(userInfo.getName().equals("") ? "" : userInfo.getName());
            if (!userInfo.getHeader().equals("")) {
                Glide.with(getActivity()).load(userInfo.getHeader())
                        .into(ivIcon);
            }else {
                Glide.with(getActivity()).load(R.mipmap.icon_defoult).into(ivIcon);
            }
        } else {
            tvNameOrStatus.setVisibility(View.VISIBLE);
            ivSet.setVisibility(View.GONE);
            fmmyphone.setVisibility(View.GONE);
            fmmynickname.setVisibility(View.GONE);
            tvNameOrStatus.setText("登录/注册");

        }
    }

    private void getphone() {

        //检查网络
        if (!NetworkUtil.checkNet(getActivity())){
            ToastUtil.showShortToast(getActivity(),"没网啦，请检查网络");
            return;
        }
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + PHONE_CODE)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        Log.d("bodybodybodybodybody", "onSuccess: "+body);
                        phone =body;
                        phonecode.setText("客服热线："+phone);
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

    protected void initData() {

        EventBus.getDefault().register(this);

        rlBanner.post(new Runnable() {
            @Override
            public void run() {
                rlBanner.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity())+10, 0, 0);
            }
        });
    }


    protected void initClick() {
        tvNameOrStatus.setOnClickListener(this);
        callphone.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
        tvMyEntrust.setOnClickListener(this);
        tvMyIntention.setOnClickListener(this);
        tvCollect.setOnClickListener(this);
        tvBrowse.setOnClickListener(this);
        tvBiaozhun.setOnClickListener(this);
        tvLvshi.setOnClickListener(this);
        tvjyliucheng.setOnClickListener(this);
        tvDaikuan.setOnClickListener(this);
        text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_fm_my_set://编辑资料设置
                    startActivity(new Intent(getActivity(), PersonCenterActivity.class));
                    break;
                case R.id.text://编辑资料设置
                    startActivity(new Intent(getActivity(), textone.class));
                    break;
                case R.id.riv_fm_my_icon://设置头像
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(), PersonCenterActivity.class));
                    break;
                case R.id.tv_fm_my_nickname://设置头像
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    break;
                case R.id.tv_fm_my_weituo://我的委托
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyEntrustActivity.class));
                    break;
                case R.id.tv_fm_my_yixiang://意向房源
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyIntentionActivity.class));
                    break;
                case R.id.tv_fm_my_collect://我的收藏
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyCollectActivity.class));
                    break;
                case R.id.tv1://浏览足迹
                    if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyBrowseActivity.class));
                    break;
                case R.id.tv_lvshi://专属律师/
                    Intent intent5 = new Intent(getActivity(),WebViewActivity2.class);
                    intent5.putExtra("istatle", "专属律师");
                    intent5.putExtra("url", BaseURL.BASE_URL+"/api/house/lawyer");
                    Log.d("asdfasdfasdf", "onClick: "+BaseURL.BASE_URL+"/api/house/lawyer");
                    startActivity(intent5);
                    break;
                case R.id.tv_jyliucheng://交易流程
                    Intent intent1 = new Intent(getActivity(),WebViewActivity2.class);
                    intent1.putExtra("istatle", "交易流程");
                    intent1.putExtra("url", BaseURL.BASE_URL+"/api/house/process");
                    startActivity(intent1);
                    break;
                case R.id.tv_biaozhun://收费标准
                    Intent intent2 = new Intent(getActivity(),WebViewActivity2.class);
                    intent2.putExtra("istatle", "收费标准");
                    intent2.putExtra("url",BaseURL.BASE_URL+"/api/house/standard");
                    startActivity(intent2);
                    break;
                case R.id.tv_daikuan://贷款计算器
                    Intent intent3 = new Intent(getActivity(),WebViewActivity2.class);
                    intent3.putExtra("istatle", "贷款计算器");
                    intent3.putExtra("url",BaseURL.BASE_URL+"/api/loanCalculator");
                    startActivity(intent3);
                    break;
                case R.id.call_phone://打电话
                    Log.d("bodybodybodybodybody", "onSuccessadf: "+phone);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        Glide.with(this).load(images.get(0).getCompressPath()).into(ivIcon);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setLoadStatus(String login) {
        if (login.equals("login")) {
            LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
            tvNameOrStatus.setVisibility(View.GONE);
            ivSet.setVisibility(View.VISIBLE);
            fmmyphone.setVisibility(View.VISIBLE);
            fmmynickname.setVisibility(View.VISIBLE);
            fmmyphone.setText(userInfo.getPhone().equals("") ? "" : ToolUtil.getMosaicPhone(userInfo.getPhone()));
            fmmynickname.setText(userInfo.getName().equals("") ? "" : userInfo.getName());
            if (!userInfo.getHeader().equals("")) {
                Glide.with(getActivity()).load(userInfo.getHeader())
                        .into(ivIcon);
            }else {
                Glide.with(getActivity()).load(R.mipmap.icon_defoult).into(ivIcon);
            }
        } else if (login.equals("logout")) {
            Glide.with(getActivity()).load(R.mipmap.icon_defoult).into(ivIcon);
            tvNameOrStatus.setVisibility(View.VISIBLE);
            ivSet.setVisibility(View.GONE);
            fmmyphone.setVisibility(View.GONE);
            fmmynickname.setVisibility(View.GONE);
            tvNameOrStatus.setText("登录/注册");
        } else {
            Glide.with(getActivity()).load(login).into(ivIcon);
        }
    }



    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
