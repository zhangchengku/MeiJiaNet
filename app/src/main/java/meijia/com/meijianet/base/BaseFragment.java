package meijia.com.meijianet.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhy.http.okhttp.OkHttpUtils;

import meijia.com.meijianet.api.URL;


public abstract class BaseFragment extends Fragment implements OnClickListener, URL {

    protected View view = null;//

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView();
        initData();
        initClick();
        return view;
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initClick();

    protected void finishAllActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).finishAllActivity();
        }
    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();
    }

}
