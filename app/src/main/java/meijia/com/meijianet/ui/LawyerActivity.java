package meijia.com.meijianet.ui;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

public class LawyerActivity extends BaseActivity {
    private TextView tvTitle;
    @Override
    protected void setContent() {
        setContentView(R.layout.activity_lawyer);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.statusColor));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("专属律师");

        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }
}