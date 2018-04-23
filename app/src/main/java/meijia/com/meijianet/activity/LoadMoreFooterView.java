package meijia.com.meijianet.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by srd on 2017/10/8.
 */

public class LoadMoreFooterView extends TextView implements SwipeLoadMoreTrigger,SwipeTrigger {
    public LoadMoreFooterView(Context context) {
        super(context);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onLoadMore() {
        setText("数据加载中，请稍后");
    }

    @Override
    public void onPrepare() {
        setText("");
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        if (!b){
            if (i<=-getHeight()){
                setText("加载更多");
            }else {
                setText("松开加载更多");
            }
        }else {
            setText("数据加载中，请稍后");
        }
    }

    @Override
    public void onRelease() {
        setText("数据加载中，请稍后");
    }

    @Override
    public void onComplete() {
        setText("数据加载中，请稍后");
    }

    @Override
    public void onReset() {
        setText("");
    }
}
