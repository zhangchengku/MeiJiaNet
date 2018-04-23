package meijia.com.meijianet.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.bean.LoginVo;

import static android.content.Context.MODE_PRIVATE;


public class SharePreUtil {
    private static final String SHARE_NAME = "meijiawang";

    /**
     * 是否是第一次点击卖房
     */
    public static void setFirstSell(Context context,boolean isSell){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isSell",isSell);
        edit.apply();
    }

    public static boolean isFisrtSell(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        return sp.getBoolean("isSell",false);
    }

    /**
     * 判断是否第一次打开应用
     *
     * @param context
     * @return
     */
    public static Boolean isFirstTimeOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        return !sp.getString("isFirstOpen","").equals(SystemUtils.getVersionName(context));
    }

    /**
     * 设置应用已打开过
     *
     * @param context Context
     */
    public static void setFirstTimeOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("isFirstOpen",SystemUtils.getVersionName(context));
        edit.apply();
    }


    public static void setUserType(Context context, String type) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("type", type);
        editor.apply();
    }

    public static String getUserType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        return sp.getString("type", "");
    }

    public static boolean setObjectToShare(Context context, Object object,String key) {
        SharedPreferences share = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (object == null) {
            SharedPreferences.Editor editor = share.edit().remove(key);
            return editor.commit();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 将对象放到OutputStream中
        // 将对象转换成byte数组，并将其进行base64编码
        String objectStr = new String(Base64.encode(baos.toByteArray(),
                Base64.DEFAULT));
        try {
            baos.close();
            oos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = share.edit();
        // 将编码后的字符串写到base64.xml文件中
        editor.putString(key, objectStr);
        return editor.commit();
    }



    public static Object getObjectFromShare(Context context, String key) {
        SharedPreferences sharePre = PreferenceManager
                .getDefaultSharedPreferences(context);
        try {
            String wordBase64 = sharePre.getString(key, "");
            // 将base64格式字符串还原成byte数组
            if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
                return null;
            }
            byte[] objBytes = Base64.decode(wordBase64.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            // 将byte数组转换成product对象
            Object obj = ois.readObject();
            bais.close();
            ois.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存打开应用时间
     */
    public static void setNowOpenTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(SystemUtils.getVersionName(context), time);
        editor.apply();
    }

    /**
     * 得到上次打开应用的时间
     */
    public static long getPreOpenTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        return sp.getLong(SystemUtils.getVersionName(context), 0L);
    }

    public static void setLoginOpenTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("loginTime", time);
        editor.apply();
    }

    public static long getLoginOpenTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        return sp.getLong("loginTime", 0L);
    }


    /**
     * 登录成功保存用户信息
     */
    public static void setUserInfo(Context context, LoginVo loginVo) {
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", loginVo.getName());
        editor.putString("header", loginVo.getHeader());
        editor.putString("uuid", loginVo.getUuid());
        editor.putString("identification", loginVo.getIdentification());
        editor.putString("location", loginVo.getLocation());
        editor.putString("email", loginVo.getEmail());
        editor.putString("phone",loginVo.getPhone());
        editor.apply();
    }

    /**
     * 获取用户信息
     */
    public static LoginVo getUserInfo(Context context) {
        LoginVo loginVo = new LoginVo();
        SharedPreferences sp = context.getSharedPreferences(SHARE_NAME, 0);
        loginVo.setName(sp.getString("name", ""));
        loginVo.setHeader(sp.getString("header", ""));
        loginVo.setUuid(sp.getString("uuid",""));
        loginVo.setLocation(sp.getString("location",""));
        loginVo.setEmail(sp.getString("email",""));
        loginVo.setIdentification(sp.getString("identification",""));
        loginVo.setPhone(sp.getString("phone",""));
        return loginVo;
    }

    public static void putListToShare(Context context,List<String> list){
        SharedPreferences.Editor editor = context.getSharedPreferences("EnvironDataList", MODE_PRIVATE).edit();
        editor.putInt("EnvironNums", list.size());
        for (int i = 0; i < list.size(); i++)
        {
            editor.putString("item_"+i, list.get(i));
        }
        editor.commit();
    }

    public static List<String> getListByShare(Context context){
        List<String> environmentList = new ArrayList<>();
        SharedPreferences preferDataList = context.getSharedPreferences("EnvironDataList", MODE_PRIVATE);
        int environNums = preferDataList.getInt("EnvironNums", 0);
        for (int i = 0; i < environNums; i++)
        {
            String environItem = preferDataList.getString("item_"+i, null);
            environmentList.add(environItem);
        }
        return environmentList;
    }

}
