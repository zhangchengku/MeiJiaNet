package meijia.com.meijianet.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.util.ToolUtil;

/**
 * Created by dell on 2018/4/11.
 */

public class HouserFragment extends BaseFragment {
    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvFive;

    private String appStart = "mqqwpa://im/chat?chat_type=wpa&uin=";
    private String appEnd = "&version=1&src_type=web&web_src=oicqzone.com";
    private String webStart = "http://wpa.qq.com/msgrd?v=3&uin=";
    private String webEnd = "&site=qq&menu=yes";
    private String middle = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_talk_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {
        tvOne = (TextView) view.findViewById(R.id.tv_ac_tack_one);
        tvTwo = (TextView) view.findViewById(R.id.tv_ac_tack_two);
        tvThree = (TextView) view.findViewById(R.id.tv_ac_tack_three);
        tvFour = (TextView) view.findViewById(R.id.tv_ac_tack_four);
        tvFive = (TextView) view.findViewById(R.id.tv_ac_tack_five);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
        tvFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.tv_ac_tack_one:
                    appToQQ("2072462819");
                    break;
                case R.id.tv_ac_tack_two:
                    appToQQ("2491135190");
                    break;
                case R.id.tv_ac_tack_three:
                    appToQQ("2528992503");
                    break;
                case R.id.tv_ac_tack_four:
                    appToQQ("3537305350");
                    break;
                case R.id.tv_ac_tack_five:
                    appToQQ("3357717441");
                    break;
            }
        }
    }

    private void appToQQ(String middle){
        String str = "";
        //判断QQ是否安装
        if (ToolUtil.isQQClientAvailable(getActivity())) {
            //安装了QQ会直接调用QQ，打开手机QQ进行会话
            str = appStart+middle+appEnd;
        } else {
            //没有安装QQ会展示网页
            str = webStart+middle+webEnd;
        }
        Uri uri = Uri.parse(str);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }
}
