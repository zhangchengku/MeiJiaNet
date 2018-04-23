package meijia.com.meijianet.api;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public interface PermissionListener {
    //全部授权
    void onGranted();
    //没有授权的权限数组
    void onDenied(List<String> deniedPermission);
}
