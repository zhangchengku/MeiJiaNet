package meijia.com.meijianet.ui;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class SellerNoticeActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvNotice;
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_seller_notice);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("卖家须知");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);

        tvNotice = (TextView) findViewById(R.id.tv_notice);
    }

    @Override
    protected void initData() {
        if (getIntent()!=null){
            boolean isTrue = getIntent().getBooleanExtra("isTrue", false);
            if (isTrue){
                tvNotice.setVisibility(View.INVISIBLE);
                tvNotice.setEnabled(false);
            }else {
                tvNotice.setVisibility(View.VISIBLE);
                tvNotice.setEnabled(true);
            }
        }


    }

    @Override
    protected void initClick() {
        tvNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,PostHouseActivity.class));
        finish();
    }
}
