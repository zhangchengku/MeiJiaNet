package meijia.com.meijianet.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.ToolUtil;

/**
 * Created by dell on 2018/4/11.
 */

public class HouserFragment extends BaseFragment {


    private LinearLayout kefu;
    private LinearLayout llParent;
    private TextView riq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_talk_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        llParent=(LinearLayout)view.findViewById(R.id.activity_talk_list);
        kefu = (LinearLayout) view.findViewById(R.id.kefu);
        riq = (TextView)view.findViewById(R.id.riq);
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
//        int year = t.year;
//        int month = t.month;
//        int date = t.monthDay;
//        int hour = t.hour; // 0-23
//        int minute = t.minute;
//        int second = t.second;
        riq.setText("今天 "+String.valueOf(t.hour)+"："+String.valueOf(t.minute));
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new MQIntentBuilder(getActivity())
                        .setCustomizedId("2676923017@qq.com") // 相同的 id 会被识别为同一个顾客
                        .build();
                startActivity(intent);

            }
        });

    }

    @Override
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity()), 0, 0);
            }
        });
    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }


}
