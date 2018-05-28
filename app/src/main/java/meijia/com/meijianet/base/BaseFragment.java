package meijia.com.meijianet.base;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.activity.MyApplication;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.api.URL;


public abstract class BaseFragment extends Fragment implements OnClickListener, URL {
    public static final int REQUEST_PERMISSTION_CODE = 1000;
    protected View view = null;//

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView();
        initData();
        initClick();
        return view;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initClick();

    protected void finishAllActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).finishAllActivity();
        }
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
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

}
