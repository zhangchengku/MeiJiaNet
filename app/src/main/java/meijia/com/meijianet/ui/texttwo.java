package meijia.com.meijianet.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/14.
 */

public class texttwo extends BaseActivity {


    private Button one;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_textone);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initClick() {
        one.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

}