package meijia.com.meijianet.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import meijia.com.meijianet.R;


/**
 * Created by srd on 2017/10/8.
 */

public class RefreshHeaderView extends LinearLayout implements SwipeRefreshTrigger,SwipeTrigger {
    private ImageView ivCar;
    private AnimationDrawable animationDrawable;
    public RefreshHeaderView(Context context) {
        this(context,null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.header_view,this);
        ivCar = (ImageView) findViewById(R.id.iv_car);}

    @Override
    public void onRefresh() {
        animationDrawable = (AnimationDrawable) ivCar.getBackground();
        animationDrawable.start();
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        if (animationDrawable!=null){
            animationDrawable.stop();
        }
    }

    @Override
    public void onReset() {

    }
}
