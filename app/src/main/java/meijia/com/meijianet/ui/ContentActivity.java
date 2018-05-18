package meijia.com.meijianet.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import meijia.com.meijianet.R;
import meijia.com.meijianet.base.UpdateManager;
import meijia.com.meijianet.bean.BaseVO;
import meijia.com.meijianet.fragment.BuyHomeFragment;
import meijia.com.meijianet.fragment.FirstFragment;
import meijia.com.meijianet.fragment.HouserFragment;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.fragment.MyFragment;
import meijia.com.meijianet.fragment.SellingHomeFragment;
import meijia.com.meijianet.fragment.TestFragment;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;
import okhttp3.Call;
import okhttp3.Response;

public class ContentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final int FIRST = 0; //首页
    private static final int SECOND = 1;//我的
    private static final int THIRD = 2;//房小二
    private static final int FOUR = 3;//买房
    private static final int FIVE = 4;//卖房
    private TestFragment mHomeFragment;
    private MyFragment mMeFragment;
    private HouserFragment houserFragment;
    private BuyHomeFragment buyHomeFragment;
    private SellingHomeFragment sellingHomeFragment;
    private RadioGroup rgMenu;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_content);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setActivityTranslucent(ContentActivity.this);
        rgMenu = (RadioGroup) findViewById(R.id.rg_menu);

    }
    @Override
    protected void initData() {
            rgMenu.check(R.id.tv_home);
            showFragment(FIRST);
        new UpdateManager(this, "main").checkUpdate();   //检查更新
    }
    //自动登录



    @Override
    protected void initClick() {
        rgMenu.setOnCheckedChangeListener(this);
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) return;
        super.onBackPressed();
    }

    /**
     * 显示相应的fragment
     *
     * @param index
     */
    public void showFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);

        switch (index) {
            case FIRST:
                if (mHomeFragment == null) {
                    mHomeFragment = new TestFragment();
                    ft.add(R.id.frame_layout, mHomeFragment, "homeFragment");
                } else {
                    ft.show(mHomeFragment);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StatusBarUtils.setStatusBarColor(ContentActivity.this, Color.TRANSPARENT);
                }
                StatusBarUtils.setStatusBarFontDark(ContentActivity.this,true);
                break;
            case SECOND:
                if (mMeFragment == null) {
                    mMeFragment = new MyFragment();
                    ft.add(R.id.frame_layout, mMeFragment, "meFragment");
                } else {
                    ft.show(mMeFragment);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StatusBarUtils.setStatusBarColor(ContentActivity.this, getResources().getColor(R.color.color_black60));
                }
                StatusBarUtils.setStatusBarFontDark(ContentActivity.this,true);
                break;
            case THIRD:
                if (houserFragment == null) {
                    houserFragment = new HouserFragment();
                    ft.add(R.id.frame_layout, houserFragment, "houserFragment");
                } else {
                    ft.show(houserFragment);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StatusBarUtils.setStatusBarColor(ContentActivity.this, getResources().getColor(R.color.color_black60));
                }
                StatusBarUtils.setStatusBarFontDark(ContentActivity.this,true);
                break;
            case FOUR:
                if (buyHomeFragment == null) {
                    buyHomeFragment = new BuyHomeFragment();
                    ft.add(R.id.frame_layout, buyHomeFragment, "houserFragment");
                } else {
                    ft.show(buyHomeFragment);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StatusBarUtils.setStatusBarColor(ContentActivity.this, getResources().getColor(R.color.color_black60));
                }
                StatusBarUtils.setStatusBarFontDark(ContentActivity.this,true);
                break;
            case FIVE:
                if (sellingHomeFragment == null) {
                    sellingHomeFragment = new SellingHomeFragment();
                    ft.add(R.id.frame_layout, sellingHomeFragment, "houserFragment");
                } else {
                    ft.show(sellingHomeFragment);
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StatusBarUtils.setStatusBarColor(ContentActivity.this, getResources().getColor(R.color.color_black60));
                }
                StatusBarUtils.setStatusBarFontDark(ContentActivity.this,true);
                break;
        }
        ft.commit();
    }


    /**
     * 隐藏相应的fragment
     *
     * @param ft
     */
    private void hideFragment(FragmentTransaction ft) {
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mMeFragment != null) {
            ft.hide(mMeFragment);
        }
        if (houserFragment!=null){
            ft.hide(houserFragment);
        }
        if (buyHomeFragment!=null){
            ft.hide(buyHomeFragment);
        }
        if (sellingHomeFragment!=null){
            ft.hide(sellingHomeFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tv_home:
                showFragment(FIRST);
                break;
            case R.id.tv_fanger:
                showFragment(THIRD);
                break;
            case R.id.tv_personal:
                showFragment(SECOND);
                break;
            case R.id.tv_buy_home:
                showFragment(FOUR);
                break;
            case R.id.tv_selling_home:
                showFragment(FIVE);
                break;
        }
    }
}
