package meijia.com.meijianet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.PostHouseActivity;
import meijia.com.meijianet.ui.SearchMoreActivity;
import meijia.com.meijianet.ui.TransactionRecordActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.SharePreUtil;

/**
 * Created by Administrator on 2018/4/20.
 * sellinghome
 */

public class SellingHomeFragment extends BaseFragment{
    private Toolbar toolbar;
    private TextView tvTitle;
    private LinearLayout llParent;
    private ImageView ivMenu;
    private LinearLayout add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_seller_notice, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    protected void initView() {
        llParent=(LinearLayout)view.findViewById(R.id.activity_talk_list);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) view.findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("卖房委托");
        add = (LinearLayout) view.findViewById(R.id.add);

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
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.add:
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("istatle", "卖家须知");
                    intent.putExtra("url", BaseURL.BASE_URL+"/api/salerNotice");
                    startActivity(intent);

                    break;
                default:
                    break;
            }
        }

    }
}
