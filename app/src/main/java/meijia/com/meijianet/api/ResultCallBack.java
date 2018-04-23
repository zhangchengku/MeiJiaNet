package meijia.com.meijianet.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import meijia.com.meijianet.bean.BaseVO;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 自定义网络访问回调
 */
public abstract class ResultCallBack extends Callback<String> {

    private static final String TAG = "ResultCallBack";
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        String result = response.body().string();
        final BaseVO baseVO = JSON.parseObject(result, BaseVO.class);
        final int returnCode = baseVO.getStatus();
        Log.e("adsfasdfasd", "run: "+baseVO.getStatus() );
        if (returnCode == 1) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(baseVO.getData());
                }
            });
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onLoginOut(baseVO.getCode());
                    onFail(baseVO.getStatus(), baseVO.getMessage());
                }
            });
        }
        return result;
    }

    /**
     * 网络访问错误时回调
     *
     * @param call
     * @param e
     * @param id
     */
    @Override
    public void onError(Call call, Exception e, int id) {
        Log.i(TAG, "网络访问错误：" + e.toString());

    }

    /**
     * 总是回调
     *
     * @param response 网络访问返回的原始字符串
     * @param id
     */
    @Override
    public void onResponse(String response, int id) {

    }

    public void onLoginOut(String response) {

    }

    /**
     * errorCode为200时回调
     *
     * @param body
     */
    public abstract void onSuccess(String body);

    /**
     * errorCode不为0时回调
     *
     * @param returnCode
     * @param returnTip
     */

    public abstract void onFail(int returnCode, String returnTip);

    public abstract void onAfter(int id);

}
