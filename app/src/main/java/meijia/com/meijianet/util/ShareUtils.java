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
import meijia.com.meijianet.R;
import meijia.com.meijianet.bean.ShareBO;

/**
 * Created by wuliang on 2017/3/7.
 * <p>
 * 集成了分享到各大平台的方法
 * <p>
 * 去http://bbs.mob.com/forum.php?mod=viewthread&tid=8212&extra=page%3D1按照流程申请程序appid
 * <p>
 * 需要分享的平台都需要到各自平台申请appid 配置在assets中的配置文件中
 * <p>
 * 微信和qq的appid还需要配置在manifest中
 */

public class ShareUtils {


    /**
     * 分享到微信朋友圈
     */
    public static void shareWXCircle(Context context, ShareBO shareBO) {
        Platform weixin = ShareSDK.getPlatform(context, WechatMoments.NAME);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(shareBO.getShareurl());
        sp.setTitle(shareBO.getTitle());
        sp.setText(shareBO.getContent());
        sp.setImageUrl(shareBO.getPicture());
        weixin.share(sp);
    }

    /**
     * 分享到微信
     */
    public static void shareWX(Context context, ShareBO shareBO) {
        Platform weixin = ShareSDK.getPlatform(context, Wechat.NAME);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setUrl(shareBO.getShareurl());
        sp.setTitle(shareBO.getTitle());
        sp.setText(shareBO.getContent());
        sp.setImageUrl(shareBO.getPicture());
        weixin.share(sp);
    }

    /***
     * 分享到QQ
     */
    public static void shareQQ(Context context, ShareBO shareBO) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(shareBO.getTitle());
        sp.setTitleUrl(shareBO.getShareurl());
        sp.setText(shareBO.getContent());
        sp.setImageUrl(shareBO.getPicture());
        Platform qq = ShareSDK.getPlatform(context, QQ.NAME);
        qq.share(sp);
    }


}
