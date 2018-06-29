package meijia.com.meijianet.ui;


import android.util.Log;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.TextView;


import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;

import static meijia.com.meijianet.R.id.textView;


/**
 * Created by Administrator on 2018/5/14.
 */

public class textone extends BaseActivity {



    private SwipeToLoadLayout swipeToLoadLayout;
    private TextView swipetarget;
    private TextView textViews;
    private LinearLayout adsf;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_textone);

    }

    @Override
    protected void initView() {
        swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.refresh_layout);
        textViews = (TextView)findViewById(R.id.swipe_refresh_header);
        swipeToLoadLayout.setRefreshHeaderView(textViews);
        swipetarget = (TextView)findViewById(R.id.swipe_target);


    }

    @Override
    protected void initData() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("dfasdfasdfasdf", "onRefresh: asdf");
                swipeToLoadLayout.setRefreshing(false);//收头
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