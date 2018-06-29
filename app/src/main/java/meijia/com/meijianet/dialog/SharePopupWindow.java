package meijia.com.meijianet.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.util.ShareUtils;

/**
 * Created by wuliang on 2016/9/18.
 * 分享弹出的弹窗
 */
public class SharePopupWindow extends PopupWindow {

    private View mView;
    private ImageView btnwechatbutton, btnwechatfriendbutton;
    private ImageView qqbutton;
    private Button btnsharecancle;
    private Activity context;
    private String url;
    private String copyUrl;
    private String[] images;   //图片集合
    private File[] files;

    public SharePopupWindow(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.sharepopupwindow, null);
        btnwechatbutton = (ImageView) mView.findViewById(R.id.share_wechat_button);
        btnsharecancle = (Button) mView.findViewById(R.id.share_cancle);
        btnwechatfriendbutton = (ImageView) mView.findViewById(R.id.share_wechatfriend_button);
        qqbutton = (ImageView) mView.findViewById(R.id.share_qq_button);


        btnsharecancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnwechatbutton.setOnClickListener(listener);
        btnwechatfriendbutton.setOnClickListener(listener);
        qqbutton.setOnClickListener(listener);
        this.setBackgroundDrawable(new ColorDrawable(0));
        this.setContentView(mView);
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
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.share_wechat_button:    //微信
                    ShareUtils.shareWX(context);
                    dismiss();
                    break;
                case R.id.share_wechatfriend_button:   //朋友圈
                    ShareUtils.shareWXCircle(context);
                    dismiss();
                    break;
                case R.id.share_qq_button:   //QQ
                    ShareUtils.shareQQ(context);
                    dismiss();
                    break;
            }
        }
    };

    /**
     * 设置分享内容
     */
    public void setShareMessage(String title, String logo, String content, String url) {
        this.url = url;
        ShareUtils.setShareMessage(title, content, logo, url);
    }



    /***
     * 检测微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }

}
