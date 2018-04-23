package meijia.com.meijianet.util;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static long getTimeMillis(String dateStr, String formatStr) {
        long millis = 0;
        if (!TextUtils.isEmpty(formatStr))
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
                Date date = dateFormat.parse(dateStr);
                millis = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return millis;
    }

    public static String formatCalendar(Calendar calendar, String format) {
        String result = null;
        if (calendar != null && !TextUtils.isEmpty(format)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date = new Date(calendar.getTimeInMillis());
            result = dateFormat.format(date);
        }
        return result;
    }

    /**
     * 格式化毫秒值形式的时间
     *
     * @param time    时间毫秒值
     * @return
     */
    public static String formatTimeInMillis(long time) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String result = "";
        if (time > 0 && !TextUtils.isEmpty(pattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
            Date date = new Date(time);
            result = dateFormat.format(date);
        }
        return result;
    }

    /**
     * 格式化毫秒值形式的时间
     *
     * @param time    时间毫秒值
     * @param pattern 目标格式
     * @return
     */
    public static String formatTimeInMillis(long time, String pattern) {
        String result = "";
        if (time > 0 && !TextUtils.isEmpty(pattern)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(time);
            result = dateFormat.format(date);
        }
        return result;
    }

    /**
     * 时间反算，小于1分钟显示刚刚，小于1小时显示xx分钟，小于6小时显示xx小时，其他显示格式化时间
     *
     * @param textView 要显示时间的textView
     * @param postTime 发布时间
     * @param nowTime  当前时间
     */
    @SuppressWarnings("unused")
    public static void setTime(TextView textView, long postTime, long nowTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long detaTime = nowTime - postTime;
        StringBuilder postDate = new StringBuilder();
        long hour = detaTime / (1000 * 60 * 60);
        if (hour <= 6) {
            long min = ((detaTime / (60 * 1000)) - hour * 60);
            long s = (detaTime / 1000 - hour * 60 * 60 - min * 60);
            if (hour >= 1)
                postDate.append(hour + "小时前");
            else if (min >= 1)
                postDate.append(min + "分钟前");
            else
                postDate.append("刚刚");
        } else {
            String formatDate = df.format(new Date(postTime));
            postDate.append(formatDate);
        }
        textView.setText(postDate.toString());
    }

    /**
     * 获取两个时间差值，6小时时间反算
     *
     * @param postTime
     * @param nowTime
     * @return
     */
    @SuppressWarnings("unused")
    public static String getDetaTime(long postTime, long nowTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long detaTime = nowTime - postTime;
        StringBuilder postDate = new StringBuilder();
        long hour = detaTime / (1000 * 60 * 60);
        if (hour <= 6) {
            long min = ((detaTime / (60 * 1000)) - hour * 60);
            long s = (detaTime / 1000 - hour * 60 * 60 - min * 60);
            if (hour >= 1)
                postDate.append(hour + "小时前");
            else if (min >= 1)
                postDate.append(min + "分钟前");
            else
                postDate.append("刚刚");
        } else {
            String formatDate = df.format(new Date(postTime));
            postDate.append(formatDate);
        }
        return postDate.toString();
    }

    public static String formatTimeToHHMMSS(long time) {
        String result = "";
        if(time > 0) {
            long second = (time / 1000) % 60;
            long minute = (time / (1000 * 60)) % 60;
            long hour = time / (1000 * 60 * 60);
            result = result + (hour > 9 ? hour + ":" : "0" + hour + ":");
            result = result + (minute > 9 ? minute + ":" : "0" + minute + ":");
            result = result + (second > 9 ? second : "0" + second);
        } else {
            result = "00:00:00";
        }
        return result;
    }

    //获取验证码按钮，进入倒计时
    public static void countDown(final TextView tv){

        new CountDownTimer(60*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText("重新获取"+"("+millisUntilFinished/1000+")");
                tv.setClickable(false);
                tv.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tv.setText("获取验证码");
                tv.setClickable(true);
                tv.setEnabled(true);
            }
        }.start();
    }


    public static String dataOne(String time,String p) {
        SimpleDateFormat sdr = new SimpleDateFormat(p,
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}
