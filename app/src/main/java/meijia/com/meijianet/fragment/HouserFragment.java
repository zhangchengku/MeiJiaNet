package meijia.com.meijianet.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.core.MQMessageManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.activity.MQConversationActivity;
import com.meiqia.meiqiasdk.callback.MQActivityLifecycleCallback;
import com.meiqia.meiqiasdk.callback.MQSimpleActivityLifecyleCallback;
import com.meiqia.meiqiasdk.controller.MQController;
import com.meiqia.meiqiasdk.model.Agent;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.api.PermissionListener;
import meijia.com.meijianet.base.BaseFragment;
import meijia.com.meijianet.bean.LoginVo;
import meijia.com.meijianet.ui.LoginActivity;
import meijia.com.meijianet.ui.WebViewActivity;
import meijia.com.meijianet.util.BubbleUtils;
import meijia.com.meijianet.util.PromptUtil;
import meijia.com.meijianet.util.SharePreUtil;
import meijia.com.meijianet.util.ToolUtil;
import meijia.com.srdlibrary.myutil.StatusBarUtils;

/**
 * Created by dell on 2018/4/11.
 */

public class HouserFragment extends BaseFragment {


    private LinearLayout kefu;
    private LinearLayout llParent;
    private TextView riq;
    private TextView massage;
    private MessageReceiver mMessageReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_talk_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initView() {

        llParent=(LinearLayout)view.findViewById(R.id.activity_talk_list);
        kefu = (LinearLayout) view.findViewById(R.id.kefu);
        riq = (TextView)view.findViewById(R.id.riq);
        massage = (TextView)view.findViewById(R.id.massage);
        Time t=new Time();
        t.setToNow();
        riq.setText("今天 "+String.valueOf(t.hour)+"："+String.valueOf((t.minute < 10 ? "0"+t.minute : t.minute)));
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        if (SharePreUtil.getUserInfo(getActivity()).getUuid().equals("")){
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            return;
                        }
                        LoginVo userInfo = SharePreUtil.getUserInfo(getActivity());
                        Intent intent = new MQIntentBuilder(getActivity())
                                .setCustomizedId("de"+userInfo.getPhone()+"@dev.com") // 相同的 id 会被识别为同一个顾客
                                .build();
                        startActivity(intent);
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        PromptUtil.showCommonDialog(getActivity(), "请在设置中打开内存卡读写权限", new View.OnClickListener() {
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
    protected void initData() {
        llParent.post(new Runnable() {
            @Override
            public void run() {
                llParent.setPadding(0, BubbleUtils.getStatusBarHeight(getActivity()), 0, 0);
            }
        });
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, intentFilter);
    }
    @Override
    public void onResume() {
        super.onResume();
        getmassage();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {

        } else {

            getmassage();
        }
    }



    private void getmassage() {
        MQManager.getInstance(getContext()).getUnreadMessages(new OnGetMessageListCallback() {
            @Override
            public void onFailure(int i, String s) {
            }
            @Override
            public void onSuccess(List<MQMessage> list) {

                if (list!=null){
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
    protected void initClick() {

    }

    @Override
    public void onClick(View v) {

    }
    private class MessageReceiver extends com.meiqia.meiqiasdk.controller.MessageReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }

        @Override
        public void receiveNewMsg(BaseMessage message) {
            getmassage();


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }
}
