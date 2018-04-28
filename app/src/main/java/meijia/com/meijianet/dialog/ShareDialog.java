package meijia.com.meijianet.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.ShareBO;
import meijia.com.meijianet.util.ShareUtils;


/**
 * Created by wuliang on 2017/7/13.
 * <p>
 * 用于显示分享弹窗，哪里需要哪里调
 */

public class ShareDialog extends PopupWindow {

    private Activity context;
    private View dialogView;
    private LinearLayout weChat;   //微信
    private LinearLayout weChatQuan;   //朋友圈
    private LinearLayout qq;    //QQ


    ShareBO shareBO;

    public ShareDialog(Activity context, ShareBO shareBO) {
        this.context = context;
        this.shareBO = shareBO;
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_share_view, null);
        initvition(dialogView);
        setListener();
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setContentView(dialogView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.anim_menu_bottombar);
        //实例化一个ColorDrawable颜色为半透明
        // ColorDrawable dw = new ColorDrawable(0x808080);
        //设置SelectPicPopupWindow弹出窗体的背景
        // this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }


    /**
     * 初始化弹窗控件
     */
    private void initvition(View mView) {
        weChat = (LinearLayout) mView.findViewById(R.id.wechat_friend);
        weChatQuan = (LinearLayout) mView.findViewById(R.id.wechat_quan);
        qq = (LinearLayout) mView.findViewById(R.id.qq);

    }


    /**
     * 设置监听
     */
    private void setListener() {

        weChat.setOnClickListener(listener);
        weChatQuan.setOnClickListener(listener);
        qq.setOnClickListener(listener);

    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.wechat_friend:
                    Log.d("ListView", "点击了分享按钮"+shareBO.getId());
                    ShareUtils.shareWX(context, shareBO);
                    dismiss();
                    break;
                case R.id.wechat_quan:
                    ShareUtils.shareWXCircle(context, shareBO);
                    dismiss();
                    break;
                case R.id.qq:
                    ShareUtils.shareQQ(context, shareBO);
                    dismiss();
                    break;

            }
        }
    };


    /***
     * 显示时将屏幕置为透明
     */
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }
}