package meijia.com.meijianet.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.srdlibrary.liushibuju.BaseAdapter;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/24
 */
public class SearchRefreshAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mDatas;
    private int type = 0;

    public SearchRefreshAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public int getCount() {
        if (mDatas == null || mDatas.size()<=0){
            throw new RuntimeException("数据集合不能为空");
        }
        return mDatas.size();
    }

    @Override
    public View getView(int position, ViewGroup parent) {
        TextView tagTv = (TextView) LayoutInflater.from(mContext)
                .inflate(R.layout.item_tag, parent, false);
        tagTv.setText(mDatas.get(position));
        tagTv.setTextColor(Color.parseColor("#408c56"));
        return tagTv;
    }
}
