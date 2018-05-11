package meijia.com.meijianet.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by wuliang on 2016/11/25.
 * <p>
 * 分享工具类
 */

public class ShareUtils {

    private static String CLASS = Wechat.NAME;   //分享微信
    private static String CLASSALL = WechatMoments.NAME;   //分享朋友圈
    private static String QQZoneSiteUrl = "http://www.best1.com/index.php/article-activity-870.html";  //app地址
    private static String QQZoneSite = "好易购商城";
    private static String shareTitle, shareContent, shareLogo, shareUrl;

    public static void setShareMessage(String title, String content, String logo, String url) {
        shareTitle = title;
        shareContent = content;
        shareLogo = logo;
        shareUrl = url;
    }


    /**
     * 分享到微信朋友圈
     */
    public static void shareWXCircle(Context context) {
        Platform weixin = ShareSDK.getPlatform(context, WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(shareUrl);
        sp.setTitle(shareTitle);
        sp.setText(shareContent);
        sp.setImageUrl(shareLogo);
        weixin.share(sp);
    }

    /**
     * 分享到微信
     */
    public static void shareWX(Context context) {
        Platform weixin = ShareSDK.getPlatform(context, Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(shareUrl);
        sp.setTitle(shareTitle);
        sp.setText(shareContent);
        sp.setImageUrl(shareLogo);
        weixin.share(sp);
    }

    /***
     * 分享到QQ
     */
    public static void shareQQ(Context context) {

        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(shareTitle);
        sp.setTitleUrl(shareUrl);
        sp.setText(shareContent);
        sp.setImageUrl(shareLogo);
        Platform qq = ShareSDK.getPlatform(context, QQ.NAME);
        qq.share(sp);
    }


}
