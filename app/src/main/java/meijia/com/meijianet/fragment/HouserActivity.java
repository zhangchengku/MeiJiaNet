package meijia.com.meijianet.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.controller.MQController;
import com.meiqia.meiqiasdk.controller.MessageReceiver;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.util.HashMap;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.base.BaseActivity;

import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.ui.WebViewActivity2;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;



/**
 * Created by Administrator on 2018/5/14.
 */
public class HouserActivity extends BaseActivity{
    private LinearLayout linear;
    private TextView tvTitle;
    private LinearLayout kefu;
    private TextView riq;
    private TextView massage;
    private MessageReceiver messageReceiver;
    private MessageReceiver mMessageReceiver;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_houser_list);
    }

    @Override
    protected void initView() {
        linear = (LinearLayout) findViewById(R.id.activity_my_entrust);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("客服");
        setSupportActionBar(toolbar);
        setNavigationFinish(toolbar);
        setNavigationHomeAsUp(true);
        kefu = (LinearLayout)findViewById(R.id.kefu);
        riq = (TextView)findViewById(R.id.riq);
        massage = (TextView)findViewById(R.id.massage);
        Time t=new Time();
        t.setToNow();
        riq.setText("今天 "+String.valueOf(t.hour)+"："+String.valueOf((t.minute < 10 ? "0"+t.minute : t.minute)));
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {

                        LoginVo userInfo = SharePreUtil.getUserInfo(HouserActivity.this);
                        Intent intent = new MQIntentBuilder(HouserActivity.this)
                                .setCustomizedId("de"+userInfo.getPhone()+"@dev.com") // 相同的 id 会被识别为同一个顾客
                                .build();
                        startActivity(intent);

                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        PromptUtil.showCommonDialog(HouserActivity.this, "请在设置中打开内存卡读写权限", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                });


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getmessage();
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            linear.post(new Runnable() {
                @Override
                public void run() {
                    StatusBarUtils.setStatusBarFontDark(HouserActivity.this,true);
                    StatusBarUtils.setStatusBarColor(HouserActivity.this, getResources().getColor(R.color.white));
                    linear.setPadding(0, BubbleUtils.getStatusBarHeight(HouserActivity.this), 0, 0);
                }
            });
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP||Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            StatusBarUtils.setStatusBarFontDark(HouserActivity.this,true);
            StatusBarUtils.setStatusBarColor(HouserActivity.this, getResources().getColor(R.color.color_black60));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }

        reisterReceiver();
    }
    private void reisterReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(MQController.ACTION_NEW_MESSAGE_RECEIVED);
        intentFilter.addAction(MQController.ACTION_CLIENT_IS_REDIRECTED_EVENT);
        intentFilter.addAction(MQController.ACTION_INVITE_EVALUATION);
        intentFilter.addAction(MQController.ACTION_AGENT_STATUS_UPDATE_EVENT);
        intentFilter.addAction(MQController.ACTION_BLACK_ADD);
        intentFilter.addAction(MQController.ACTION_BLACK_DEL);
        intentFilter.addAction(MQController.ACTION_QUEUEING_REMOVE);
        intentFilter.addAction(MQController.ACTION_QUEUEING_INIT_CONV);
        intentFilter.addAction(MQMessageManager.ACTION_END_CONV_AGENT);
        intentFilter.addAction(MQMessageManager.ACTION_END_CONV_TIMEOUT);
        intentFilter.addAction("socket_open");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);
    }
    @Override
    protected void initClick() {

    }
    private class MessageReceiver extends com.meiqia.meiqiasdk.controller.MessageReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }

        @Override
        public void receiveNewMsg(BaseMessage message) {
            getmessage();


        }

        @Override
        public void changeTitleToInputting() {


        }

        @Override
        public void addDirectAgentMessageTip(String agentNickname) {

        }

        @Override
        public void setCurrentAgent(Agent agent) {

        }

        @Override
        public void inviteEvaluation() {

        }

        @Override
        public void setNewConversationId(String newConversationId) {

        }

        @Override
        public void updateAgentOnlineOfflineStatus() {

        }

        @Override
        public void blackAdd() {

        }

        @Override
        public void blackDel() {

        }

        @Override
        public void removeQueue() {

        }

        @Override
        public void queueingInitConv() {

        }


    }

    private void getmessage() {
        MQManager.getInstance(HouserActivity.this).getUnreadMessages(new OnGetMessageListCallback() {
            @Override
            public void onFailure(int i, String s) {

            }

            @Override
            public void onSuccess(List<MQMessage> list) {

                if (list!=null){
                    Log.d("测试", "onSuccess: "+list.size());
                    if(list.size()>=1){
                        massage.setVisibility(View.VISIBLE);
                        massage.setText(String.valueOf(list.size()));
                    }else {
                        massage.setVisibility(View.GONE);
                    }
                }else {
                    massage.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }


    @Override
    public void onClick(View v) {

    }
}
