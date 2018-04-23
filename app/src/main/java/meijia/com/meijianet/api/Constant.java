package meijia.com.meijianet.api;

import android.os.Environment;

import java.io.File;

import meijia.com.meijianet.activity.MyApplication;

/**
 * Created by Administrator on 2017/9/2.
 */

public interface Constant {
    String CACHE_DIR = MyApplication.getApp().getCacheDir().getAbsolutePath();
    String PIC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "frog" + File.separator + "trip";
    int BUKET_SELECTOR_BY_NAME = 1;//按照文件夹名查询
    int BUKET_SELECTOR_ALL = 2;//查询所有图片
    int PIC_FROM_CAMERA = 3;//调用系统相机拍照返回
    int RESULT_CDOE_CUT = 4;//截图返回

}
