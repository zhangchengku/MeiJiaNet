package meijia.com.meijianet.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import meijia.com.meijianet.R;
import meijia.com.meijianet.dialog.BottomMenuDialog;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.activity.MyApplication;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

import static meijia.com.meijianet.api.URL.UPDATE_MSG;

public class PersonCenterActivity extends TakePhotoActivity implements View.OnClickListener {
    private MyApplication app;
    protected String TAG;
    private BottomMenuDialog mDialog;
    private ImageView ivFinish;
    private LinearLayout llIcon;
    private RoundedImageView ivIcon;
    private LinearLayout llName;
    private TextView tvName;
    private LinearLayout llAddress;
    private TextView tvAddress;
    private LinearLayout llPhone;
    private TextView tvPhone;
    private LinearLayout llEmail;
    private TextView tvEmail;
    private LinearLayout llNumber;
    private TextView tvNumber;
    private String path = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        app.activityList.add(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContent();
        initView();
        initData();
        initClick();
    }

    protected void setContent() {
        setContentView(R.layout.activity_personer_center);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    protected void initView() {
        ivFinish = (ImageView) findViewById(R.id.iv_finish);
        llIcon = (LinearLayout) findViewById(R.id.ll_ac_personercenter_icon);
        ivIcon = (RoundedImageView) findViewById(R.id.riv_ac_center_icon);
        llName = (LinearLayout) findViewById(R.id.ll_ac_center_name);
        tvName = (TextView) findViewById(R.id.tv_ac_center_name);
        llAddress = (LinearLayout) findViewById(R.id.ll_ac_center_address);
        tvAddress = (TextView) findViewById(R.id.tv_ac_center_address);
        llPhone = (LinearLayout) findViewById(R.id.ll_ac_center_phone);
        tvPhone = (TextView) findViewById(R.id.tv_ac_center_phone);
        llEmail = (LinearLayout) findViewById(R.id.ll_ac_center_youxinag);
        tvEmail = (TextView) findViewById(R.id.tv_ac_center_youxinag);
        llNumber = (LinearLayout) findViewById(R.id.ll_ac_center_idnumber);
        tvNumber = (TextView) findViewById(R.id.tv_ac_center_idnumber);
    }

    protected void initData() {
        LoginVo info = SharePreUtil.getUserInfo(PersonCenterActivity.this);
        if (info != null && !info.getName().equals("")) {
            String header = info.getHeader();
            String address = info.getLocation();
            String email = info.getEmail();
            String name = info.getName();
            String identification = info.getIdentification();
            String phone = info.getPhone();
            if (header!=null && !header.equals("")) {
                Glide.with(PersonCenterActivity.this).load(header).into(ivIcon);
            }
            if (address!=null && !address.equals("")) {
                tvAddress.setText(address);
            }
            if (email!=null && !email.equals("")) {
                tvEmail.setText(email);
            }
            if (identification!=null && !identification.equals("")) {
                tvNumber.setText(ToolUtil.getMosaicPhone(identification));
            }
            if (name!=null && !name.equals("")) {
                tvName.setText(name);
            }
            if (phone!=null && !phone.equals("")) {

                tvPhone.setText(ToolUtil.getMosaicPhone(phone));
            }
        }
    }

    protected void initClick() {
        ivFinish.setOnClickListener(this);
        llIcon.setOnClickListener(this);
        llName.setOnClickListener(this);
        llAddress.setOnClickListener(this);
//        llPhone.setOnClickListener(this);
        llNumber.setOnClickListener(this);
        llEmail.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.iv_finish:
                    if (!path.equals("")) {
                        Intent intent = new Intent();
                        intent.putExtra("path", path);
                        setResult(101, intent);
                    }
                    finish();
                    break;
                case R.id.ll_ac_personercenter_icon:
                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
                        return;
                    }
                    showPhotoDialog(PersonCenterActivity.this);
                    break;
                case R.id.ll_ac_center_name:
                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
                        return;
                    }
                    Intent nameIntent = new Intent(this, UpdateNameActivity.class);
                    startActivityForResult(nameIntent, 103);
                    break;
                case R.id.ll_ac_center_address:
                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
                        return;
                    }
                    Intent addressIntent = new Intent(this, AddressActivity.class);
                    startActivityForResult(addressIntent, 103);
                    break;
//                case R.id.ll_ac_center_phone:
//                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
//                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
//                        return;
//                    }
//                    Intent phoneIntent = new Intent(this, UpdatePhoenActivity.class);
//                    startActivityForResult(phoneIntent, 103);
//                    break;
                case R.id.ll_ac_center_youxinag:
                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
                        return;
                    }
                    Intent emailIntent = new Intent(this, YouxiangActivity.class);
                    startActivityForResult(emailIntent, 103);
                    break;
                case R.id.ll_ac_center_idnumber:
                    if (SharePreUtil.getUserInfo(PersonCenterActivity.this).getName().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"您还没有登录");
                        return;
                    }
                    if (!SharePreUtil.getUserInfo(PersonCenterActivity.this).getIdentification().equals("")){
                        ToastUtil.showShortToast(PersonCenterActivity.this,"身份证号码不能修改哦");
                        return;
                    }
                    Intent numberIntent = new Intent(this, NumberActivity.class);
                    startActivityForResult(numberIntent, 103);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        showImg(result.getImages());
        path = result.getImages().get(0).getCompressPath();
        updateMsg();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    private void showImg(ArrayList<TImage> images) {
//        Glide.with(this).load(new File(images.get(0).getCompressPath())).into(ivIcon);
        Glide.with(this).load(images.get(0).getCompressPath()).into(ivIcon);
//        Bitmap iconBitmap = BitmapFactory.decodeFile(images.get(0).getCompressPath());
//        CompressImageResult compressImageResult = BitmapUtil.compressBitmap(iconBitmap, getActivity());
//        File file = compressImageResult.getFile();
//        Glide.with(this).load(file).into(ivIcon);
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
    public void onBackPressed() {
        if (!path.equals("")) {
            Intent intent = new Intent();
            intent.putExtra("path", path);
            setResult(101, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 103) {
                switch (resultCode) {
                    case 104:
                        tvName.setText(data.getStringExtra("name"));
                        break;
                    case 105:
                        tvAddress.setText(data.getStringExtra("city"));
                        break;
                    case 106:
                        tvPhone.setText(ToolUtil.getMosaicPhone(data.getStringExtra("phone")));
                        break;
                    case 107:
                        tvEmail.setText(ToolUtil.getMosaicPhone(data.getStringExtra("email")));
                        break;
                    case 108:
                        tvNumber.setText(ToolUtil.getMosaicPhone(data.getStringExtra("number")));
                        break;
                }

            }
        }
    }

    private void updateMsg() {
        if (!NetworkUtil.checkNet(PersonCenterActivity.this)) {
            ToastUtil.showShortToast(PersonCenterActivity.this, "没有网了，请检查网络");
            return;
        }

        PromptUtil.showTransparentProgress(PersonCenterActivity.this, false);
        PostFormBuilder builder = OkHttpUtils.post();
        File file = new File(path);
        builder.addFile(
                "headImage",
                file.getName(),
                file);
        builder.url(BaseURL.BASE_URL + UPDATE_MSG)
                .build().execute(new ResultCallBack() {
            @Override
            public void onSuccess(String body) {
                try {
                    JSONObject object = new JSONObject(body);
                    String headurl = object.getString("headurl");
                    Glide.with(PersonCenterActivity.this).load(headurl).into(ivIcon);
                    LoginVo vo = SharePreUtil.getUserInfo(PersonCenterActivity.this);
                    vo.setHeader(headurl);
                    SharePreUtil.setUserInfo(PersonCenterActivity.this, vo);
                    EventBus.getDefault().post(headurl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int returnCode, String returnTip) {
                ToastUtil.showShortToast(PersonCenterActivity.this, returnTip);
                PromptUtil.closeTransparentDialog();
            }

            @Override
            public void onAfter(int id) {
                PromptUtil.closeTransparentDialog();
            }
        });

    }
}
