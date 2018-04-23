package meijia.com.meijianet.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by dell on 2018/4/10.
 *
 */

public class MyScollerLinearlayoutManager extends LinearLayoutManager {
    private boolean isVerticalScroll = true;
    public MyScollerLinearlayoutManager(Context context) {
        super(context);
    }

    public MyScollerLinearlayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MyScollerLinearlayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVerticalScrollFlag(boolean isVerticalScroll){
        this.isVerticalScroll = isVerticalScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isVerticalScroll && super.canScrollVertically();
    }
}
