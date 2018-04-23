/**
 * ----------------------------------------------------------
 * Copyright © 北京博文卡特科技有限公司
 * ----------------------------------------------------------
 * Author：yujingbin
 * Create：2016/3/24 16:01
 */

package meijia.com.meijianet.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 字符串相关处理方法
 */
public class StringUtil {
    /**
     * 字符串拼接，逗号隔开
     *
     * @param strings 字符串集合
     * @return 拼接后的字符串
     */
    public static String linkStrings(ArrayList<String> strings) {
        StringBuffer stringBuffer = new StringBuffer();
        if (strings != null && strings.size() > 0) {
            int size = strings.size();
            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(strings.get(i));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 拆分字符串，以逗号分割
     *
     * @param string 目标字符串
     * @return 拆分得到的字符串集合
     */
    public static ArrayList<String> breakString(String string) {
        ArrayList<String> result = new ArrayList<>();
        if (string != null) {
            String[] array = string.split(",");
            List<String> list = Arrays.asList(array);
            for (String str : list) {
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 随机生成文件名
     *
     * @return 当前时间加随机数生成的字符串
     */
    public static String getRandomFileName() {
        StringBuffer result = new StringBuffer();
        // 以当前时间开头
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        result.append(dateFormat.format(date));
        // 生成五随机数
        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        long ran = random.nextInt(99999);
        result.append(ran);
        int lenth = 5 - (String.valueOf(ran)).length();
        for (int i = 0; i < lenth; i++) {
            result.append("0");
        }
        return result.toString();
    }

    /**
     * 获取压缩图片暂存目录
     *
     * @param context 上下文
     * @return 图片暂存目录
     */
    public static String getImageCachePath(Context context) {
        return context.getCacheDir() + "/imgCache/";
    }

    /**
     * 拼接url和网络请求参数
     *
     * @param url    接口地址
     * @param params 参数
     * @return
     */
    public static String getFileNameForUrl(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
