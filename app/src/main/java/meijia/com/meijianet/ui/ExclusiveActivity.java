package meijia.com.meijianet.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.MyEntrustAdapter;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.NetworkUtil;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToastUtil;
import meijia.com.meijianet.vo.myentrust.MyEntrustVo;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by Administrator on 2018/5/10.
 */
public class ExclusiveActivity extends BaseActivity {
    private LinearLayout linear;
    private TextView tvTitle;
    private TextView jxfb;
    private TextView wdwt;

    @Override
    protected void setContent() {
        StatusBarUtils.setStatusBarFontDark(this, true);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_my_exclusive);
    }

    @Override
    protected void initView() {
        linear = (LinearLayout) findViewById(R.id.activity_my_entrust);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("发布房源");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        jxfb = (TextView) findViewById(R.id.jxfb);
        wdwt = (TextView) findViewById(R.id.wdwt);
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            linear.post(new Runnable() {
                @Override
                public void run() {
                    linear.setPadding(0, BubbleUtils.getStatusBarHeight(ExclusiveActivity.this), 0, 0);
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

    }
    @Override
    protected void initClick() {
        jxfb.setOnClickListener(this);
        wdwt.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.jxfb:
                    startActivity(new Intent(ExclusiveActivity.this,PostHouseActivity.class));
                    break;
                case R.id.wdwt:
                    goBackMain();
                    break;
            }
        }

    }
}