package meijia.com.meijianet.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import meijia.com.meijianet.R;
import meijia.com.meijianet.base.BaseActivity;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.util.SharePreUtil;

/**
 * 首页的一个优惠活动弹窗
 */

public class PromotionsPopActivity extends Activity {

    private ImageView im_delete;
    private ImageView im_load;
    private boolean isYOUHUI = true;   //判断是否显示的优惠券弹窗，默认为优惠弹窗，false为活动弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_discounts_pop);
        im_delete = (ImageView) findViewById(R.id.delete_btn);
        im_load = (ImageView) findViewById(R.id.load_image);
        im_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        im_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("rediredt").equals("")){
                    return;
                }
                if(getIntent().getStringExtra("rediredt").substring(0,7).equals("houseid")){
                    Intent intent = new Intent(PromotionsPopActivity.this, WebViewActivity.class);
                    intent.putExtra("istatle", "房屋详情");
                    intent.putExtra("houseId", getIntent().getStringExtra("rediredt").substring(8,getIntent().getStringExtra("rediredt").length()));
                    startActivity(intent);
                }else {

                }
                if( getIntent().getStringExtra("newlogin").equals("0")){
                    Intent intent5 = new Intent(PromotionsPopActivity.this,WebViewActivity4.class);
                    intent5.putExtra("url", getIntent().getStringExtra("rediredt"));
                    startActivity(intent5);
                }else {
                    if (SharePreUtil.getUserInfo(PromotionsPopActivity.this).getUuid().equals("")){
                        startActivity(new Intent(PromotionsPopActivity.this, LoginActivity.class));
                        return;
                    }
                    LoginVo userInfo = SharePreUtil.getUserInfo(PromotionsPopActivity.this);
                    Intent intent5 = new Intent(PromotionsPopActivity.this,WebViewActivity4.class);
                    intent5.putExtra("url", getIntent().getStringExtra("rediredt")+"?uuid="+userInfo.getUuid());
                    startActivity(intent5);
                }

            }
        });
        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .placeholder(R.mipmap.icon_fang_defout)
                .error(R.mipmap.icon_fang_defout)
                .into(im_load);
    }


}
