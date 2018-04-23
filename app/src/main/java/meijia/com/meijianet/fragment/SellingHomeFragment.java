package meijia.com.meijianet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.ui.PostHouseActivity;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SellingHomeFragment extends BaseFragment{
    private Toolbar toolbar;
    private TextView tvTitle;
    private TextView tvNotice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_seller_notice, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override
    protected void initView() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) view.findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("卖家须知");
        tvNotice = (TextView) view.findViewById(R.id.tv_notice);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {
        tvNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(),PostHouseActivity.class));
    }
}
