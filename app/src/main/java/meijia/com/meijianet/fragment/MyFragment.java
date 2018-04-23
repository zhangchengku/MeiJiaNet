package meijia.com.meijianet.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.dialog.BottomMenuDialog;
import meijia.com.meijianet.ui.LawyerActivity;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.MyBrowseActivity;
import meijia.com.meijianet.ui.MyCollectActivity;
import meijia.com.meijianet.ui.MyEntrustActivity;
import meijia.com.meijianet.ui.MyIntentionActivity;
import meijia.com.meijianet.ui.PersonCenterActivity;
import meijia.com.meijianet.ui.ProveActivity;
import meijia.com.meijianet.ui.RefundActivity;
import meijia.com.meijianet.ui.SettingActivity;
import meijia.com.meijianet.ui.StandardActivity;
import meijia.com.meijianet.util.BubbleUtils;
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
public class MyFragment extends TakePhotoFragment implements View.OnClickListener {
    private View view;
    private RelativeLayout rlBanner;
    private ImageView ivSet;
    private RoundedImageView ivIcon;
    private BottomMenuDialog mDialog;
    private TextView tvNameOrStatus;

    private TextView tvMyEntrust;
    private TextView tvMyIntention;
    private TextView tvCollect;
    private TextView tvBrowse;
    private TextView tvLvshi;
    private TextView tvZhengming;
    private TextView tvBiaozhun;
    private TextView tvDaikuan;
//    private TextView tvHuangkuan;
//    private TextView tvShuilv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_fragment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        initView();
        initData();
        initClick();
        return view;
    }

    private void initView() {
        tvBrowse = (TextView) view.findViewById(R.id.tv1);
        tvCollect = (TextView) view.findViewById(R.id.tv_fm_my_collect);
        rlBanner = (RelativeLayout) view.findViewById(R.id.rl_banner);
        ivSet = (ImageView) view.findViewById(R.id.iv_fm_my_set);
        ivIcon = (RoundedImageView) view.findViewById(R.id.riv_fm_my_icon);
        tvNameOrStatus = (TextView) view.findViewById(R.id.tv_fm_my_nickname);
        tvMyEntrust = (TextView) view.findViewById(R.id.tv_fm_my_weituo);
        tvMyIntention = (TextView) view.findViewById(R.id.tv_fm_my_yixiang);
        tvLvshi = (TextView) view.findViewById(R.id.tv_lvshi);
        tvZhengming = (TextView) view.findViewById(R.id.tv_zhengming);
        tvBiaozhun = (TextView) view.findViewById(R.id.tv_biaozhun);
        tvDaikuan = (TextView) view.findViewById(R.id.tv_daikuan);
//        tvHuangkuan = (TextView) view.findViewById(R.id.tv_huankuan);
//        tvShuilv = (TextView) view.findViewById(R.id.tv_shuilv);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
        if (!userInfo.getUuid().equals("")) {
            tvNameOrStatus.setText(userInfo.getName().equals("") ? "游客" : userInfo.getName());
            tvNameOrStatus.setEnabled(false);
            tvNameOrStatus.setClickable(false);
            if (!userInfo.getHeader().equals("")) {
                Glide.with(getActivity()).load(userInfo.getHeader()).into(ivIcon);
            }
        } else {
            tvNameOrStatus.setText("登录/注册");
            tvNameOrStatus.setEnabled(true);
            tvNameOrStatus.setClickable(true);
        }
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
        ivSet.setOnClickListener(this);
        ivIcon.setOnClickListener(this);
        tvNameOrStatus.setOnClickListener(this);
        tvMyEntrust.setOnClickListener(this);
        tvMyIntention.setOnClickListener(this);
        tvCollect.setOnClickListener(this);
        tvBrowse.setOnClickListener(this);
        tvBiaozhun.setOnClickListener(this);
        tvLvshi.setOnClickListener(this);
        tvZhengming.setOnClickListener(this);
        tvDaikuan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_fm_my_set://设置
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    break;
                case R.id.riv_fm_my_icon://设置头像
                    Intent intent = new Intent(getActivity(), PersonCenterActivity.class);
                    startActivityForResult(intent, 100);
                    break;
                case R.id.tv_fm_my_nickname:
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                case R.id.tv_fm_my_weituo:
                    if (SharePreUtil.getUserInfo(getActivity()).getName().equals("")){
                        ToastUtil.showShortToast(getActivity(),"您还没有登录");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyEntrustActivity.class));
                    break;
                case R.id.tv_fm_my_yixiang:
                    if (SharePreUtil.getUserInfo(getActivity()).getName().equals("")){
                        ToastUtil.showShortToast(getActivity(),"您还没有登录");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyIntentionActivity.class));
                    break;
                case R.id.tv_fm_my_collect:
                    if (SharePreUtil.getUserInfo(getActivity()).getName().equals("")){
                        ToastUtil.showShortToast(getActivity(),"您还没有登录");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyCollectActivity.class));
                    break;
                case R.id.tv1:
                    if (SharePreUtil.getUserInfo(getActivity()).getName().equals("")){
                        ToastUtil.showShortToast(getActivity(),"您还没有登录");
                        return;
                    }
                    startActivity(new Intent(getActivity(), MyBrowseActivity.class));
                    break;
                case R.id.tv_lvshi:
                    startActivity(new Intent(getActivity(), LawyerActivity.class));
                    break;
                case R.id.tv_zhengming:
                    startActivity(new Intent(getActivity(), ProveActivity.class));
                    break;
                case R.id.tv_biaozhun:
                    startActivity(new Intent(getActivity(), StandardActivity.class));
                    break;
                case R.id.tv_daikuan:
                    startActivity(new Intent(getActivity(), RefundActivity.class));
                    break;
                default:
                    break;
            }
        }
    }


    public void showPhotoDialog(final Context context) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        mDialog = new BottomMenuDialog(context);
        mDialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                takePhoto(1, getTakePhoto());
            }
        });
        mDialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                takePhoto(2, getTakePhoto());
            }
        });
        mDialog.show();
    }

    private void takePhoto(int i, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (i) {
            case 2:
                int limit = 1;//张数
                if (limit > 1) {
//                    takePhoto.onPickMultipleWithCrop(limit, getCropOptions());//裁剪
                    takePhoto.onPickMultiple(limit);//不裁剪
                    return;
                }
//                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());//裁剪出自相册
                takePhoto.onPickFromGallery();//出自相册不裁剪
                break;
            case 1:
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());//裁剪
//                takePhoto.onPickFromCapture(imageUri);//不裁剪
                break;
            default:
                break;
        }
    }

    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        boolean withWonCrop = true;//true为自带

        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 102400;//大小
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;//是否显示压缩进度条
        boolean enableRawFile = false;//是否保存原图
        //压缩工具
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);//自带相册
//        builder.setCorrectImage(true);//原生相册
        takePhoto.setTakePhotoOptions(builder.create());

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
//        Glide.with(this).load(new File(images.get(0).getCompressPath())).into(ivIcon);
        Glide.with(this).load(images.get(0).getCompressPath()).into(ivIcon);
//        Bitmap iconBitmap = BitmapFactory.decodeFile(images.get(0).getCompressPath());
//        CompressImageResult compressImageResult = BitmapUtil.compressBitmap(iconBitmap, getActivity());
//        File file = compressImageResult.getFile();
//        Glide.with(this).load(file).into(ivIcon);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 100 && resultCode == 101) {
            String path = data.getStringExtra("path");
            if (path != null && !path.equals("")) {
                Glide.with(this).load(path).into(ivIcon);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setLoadStatus(String login) {
        if (login.equals("login")) {
            LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
            tvNameOrStatus.setText(userInfo.getName().equals("") ? "游客" : userInfo.getName());
            tvNameOrStatus.setEnabled(false);
            tvNameOrStatus.setClickable(false);
            String header = userInfo.getHeader();
            if (header != null && !header.equals("")) {
                Glide.with(this).load(header).into(ivIcon);
            }
        } else if (login.equals("logout")) {
            tvNameOrStatus.setText("登录/注册");
            Glide.with(getActivity()).load(R.mipmap.icon_defoult).into(ivIcon);
            tvNameOrStatus.setEnabled(true);
            tvNameOrStatus.setClickable(true);
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
