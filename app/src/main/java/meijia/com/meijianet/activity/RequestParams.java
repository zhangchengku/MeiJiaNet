/**
 * ----------------------------------------------------------
 * Copyright © 北京博文卡特科技有限公司
 * ----------------------------------------------------------
 * Author：yujingbin
 * Create：2016/3/26 16:13
 */

package meijia.com.meijianet.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络访问参数封装
 */
public class RequestParams {

    private Map<String, String> map;
    private Context context;

    public RequestParams() {
        map = new HashMap<>();
    }

    public RequestParams(Context context) {
        map = new HashMap<>();
        this.context = context;
//        UserVo loginVO = SharePreUtil.getUserInfo(context);
//        if(loginVO != null) {
////            add("userCode", loginVO.getUserCode());
////            add("token", loginVO.getToken());
//        } else {
////            add("userCode", "");
////            add("token", "");
//        }
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "map=" + map.toString() +
                '}';
    }

    /**
     * 获取应用版本信息
     * @param context Context
     * @return PackagesInfo
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void add(String key, String value) {
        map.put(key, value);
    }

    public void add(String key, int value) {
        map.put(key, String.valueOf(value));
    }

    public void add(String key, Long value) {
        map.put(key, String.valueOf(value));
    }

    public void add(String key, boolean value) {
        map.put(key, String.valueOf(value));
    }

    public void add(String key, float value) {
        map.put(key, String.valueOf(value));
    }

    public void add(String key, double value) {
        map.put(key, String.valueOf(value));
    }
}
