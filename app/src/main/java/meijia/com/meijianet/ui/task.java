package meijia.com.meijianet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import meijia.com.meijianet.R;

/**
 * Created by Administrator on 2018/6/22.
 */

public class task extends Activity{
    private static final String TAG = "JPush";
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "[MyReceiver] 进来了task");
        setContentView(R.layout.tasl);
    }
}
