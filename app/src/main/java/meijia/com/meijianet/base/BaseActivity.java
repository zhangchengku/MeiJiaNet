package meijia.com.meijianet.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.api.Constant;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.activity.MyApplication;
import meijia.com.meijianet.ui.ContentActivity;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.api.URL;


public abstract class BaseActivity extends AppCompatActivity implements OnClickListener, Constant, URL {
    protected MyApplication app;
    protected Toolbar toolbar;
    protected String TAG;
    public static final int REQUEST_PERMISSTION_CODE = 10000;

    protected abstract void setContent();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		CrashHandler.getInstance().init(this);  //初始化捕获异常线程
        TAG=this.getClass().getSimpleName();
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();//获取app实例
        app.activityList.add(this);//添加到集合中，安全退出时要用
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContent();
        initView();
        initData();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        app.activityList.remove(this); //摧毁时从集合移除
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }

    public void finishAllActivity() {
        for (Activity ac : app.activityList) {
            if (ac != null)
                ac.finish();
        }
    }
    /**
     * 退回首页，除了首页其余页面都关闭
     */
    public void goBackMain() {
        for (int i = 0, size = app.activityList.size(); i < size; i++) {
            if (null != app.activityList.get(i) && !(app.activityList.get(i) instanceof ContentActivity)) {
                app.activityList.get(i).finish();
            }
        }
    }
    /**
     * 快速获取当前activity的引用，用在匿名内部类
     *
     * @return
     */
    protected BaseActivity getThisContext() {
        return BaseActivity.this;
    }

    /**
     * 设置toolbar的返回键为finish
     *
     * @param toolbar
     */
    protected void setNavigationFinish(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent();
                    setResult(4,i);
                    finish();
                }
            });
        }
    }

    /**
     * 设置toolbar的Navigation Icon为返回键
     *
     * @param flag
     */
    protected void setNavigationHomeAsUp(boolean flag) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(flag);
        }
    }

    private static PermissionListener permissionListener;

    /**
     * 权限申请的方法，static修饰，随处可用
     * @param permissions 要申请的权限数组
     * @param listener 结果回调接口
     */
    public static void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        //获取栈顶Activity
        Activity topActivity = MyApplication.getTopActivity();
        if (topActivity==null){
            return;
        }
        permissionListener = listener;
        //申请权限的容器
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(topActivity,permission)!= PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }
        //有需要申请的权限
        if (!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(topActivity,permissionList
                    .toArray(new String[permissionList.size()]),REQUEST_PERMISSTION_CODE);
        }else {
            permissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSTION_CODE:
                if (grantResults.length>0){
                    //被拒绝的权限集合
                    List<String> deniedPermissionList = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult!=PackageManager.PERMISSION_GRANTED){
                            deniedPermissionList.add(permission);
                        }
                    }
                    if (deniedPermissionList.isEmpty()){
                        permissionListener.onGranted();
                    }else {
                        permissionListener.onDenied(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void appoutAll() {
        //检查网络
        if (!NetworkUtil.checkNet(this)){
            ToastUtil.showShortToast(this,"没网啦，请检查网络");
            return;
        }
        PromptUtil.showTransparentProgress(this,false);
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + LOGIN_OUT)
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
//                        ToastUtil.showShortToast(SettingActivity.this,"退出成功");
                        LoginVo vo = new LoginVo();
                        vo.setName("");
                        vo.setHeader("");
                        vo.setUuid("");
                        SharePreUtil.setUserInfo(BaseActivity.this,vo);
                        EventBus.getDefault().post("logout");
                        finish();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {
                        ToastUtil.showShortToast(BaseActivity.this,returnTip);
                        PromptUtil.closeTransparentDialog();
                        finish();
                    }

                    @Override
                    public void onAfter(int id) {
                        PromptUtil.closeTransparentDialog();
                    }
                });
    }
}
