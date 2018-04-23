package meijia.com.meijianet.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/23
 */
public class MyScrollView extends ScrollView{
    private onMyScrollListener mListener;
    public interface onMyScrollListener{
        void onScrollChange(int l, int t, int oldl, int oldt);
    }
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnMyScrollListener(onMyScrollListener listener) {
        mListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener!=null){
            mListener.onScrollChange(l,t,oldl,oldt);
        }
    }
}
