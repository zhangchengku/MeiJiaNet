package meijia.com.meijianet.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import meijia.com.meijianet.activity.MyApplication;
import meijia.com.meijianet.api.LocalConfiguration;
import meijia.com.meijianet.ui.ContentActivity;

/**
 * Created by wuliang on 2017/3/6.
 * <p>
 * 微信支付回调的activity类
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "com.myp.meiyuan.wxapi.WXPayEntryActivity";

	private IWXAPI api;
	private MyApplication app;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
		api = WXAPIFactory.createWXAPI(this, LocalConfiguration.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);
		api.registerApp(LocalConfiguration.WEIXIN_APP_ID);
		app = (MyApplication) getApplication();//获取app实例
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq req) {

	}


	@Override
	public void onResp(BaseResp resp) {

		int errCode = resp.errCode;
		if (errCode == 0) {
			// 0成功 展示成功页面
			Toast.makeText(this, "支付成功！", Toast.LENGTH_SHORT).show();
			for (int i = 0, size = app.activityList.size(); i < size; i++) {
				if (null != app.activityList.get(i) && !(app.activityList.get(i) instanceof ContentActivity)) {
					app.activityList.get(i).finish();
				}
			}

		} else if (errCode == -1) {
			//-1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//            LogUtils.showToast("支付失败！");
			Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show();
			finish();
		} else if (errCode == -2) {
			//-2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
//            LogUtils.showToast("取消支付！");
			Toast.makeText(this, "取消支付！", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
}


