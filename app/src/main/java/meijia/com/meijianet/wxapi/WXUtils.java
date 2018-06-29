package meijia.com.meijianet.wxapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import meijia.com.meijianet.api.LocalConfiguration;
import meijia.com.meijianet.bean.WechatPayVO;

/**
 * Created by wuliang on 2017/3/6.
 * <p>
 * 调用微信登陆，与微信支付的工具类
 * <p>
 * 在进行微信OAuth2.0授权登录接入之前，在微信开放平台注册开发者帐号，并拥有一个已审核通过的移动应用，
 * 并获得相应的AppID和AppSecret，申请微信登录且通过审核后，可开始接入流程。
 * <p>
 * 链接为：https://open.weixin.qq.com/cgi-bin/index?t=home/index&lang=zh_CN
 */

public class WXUtils {

    // 自己微信应用的 appId
    public static String WX_APP_ID = LocalConfiguration.WEIXIN_APP_ID;
    public static IWXAPI wxApi;
    private static String TAG = "asdfsadf";


    /**
     * 在程序入口处调用此方法注册微信授权
     *
     * @param context
     */
    public static void registerWX(Context context) {
        wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(WX_APP_ID);
    }


    /**
     * 调用此方法微信登陆
     *
     */
//    public static void loginWX() {
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        req.state = "wx_login_cinema";
//        wxApi.sendReq(req);
//
//    }


    /**
     * 调用此方法微信支付
     */
    public static void payWX(final WechatPayVO vo) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                PayReq request = new PayReq();
                request.appId = vo.getAppid();
                request.partnerId = vo.getPartnerid();   //微信支付分配的商户号
                request.prepayId = vo.getPrepayid();  //微信返回的支付交易ID
                request.packageValue = "Sign=WXPay";    //扩展字符串 ，暂填固定值"Sign=WXPay"
                request.nonceStr = vo.getNoncestr(); //随机字符串
                request.timeStamp = String.valueOf(vo.getTimestamp());    //时间戳
                request.sign = vo.getSign();
                wxApi.sendReq(request);
                return null;
            }
        }.execute();
    }

}
