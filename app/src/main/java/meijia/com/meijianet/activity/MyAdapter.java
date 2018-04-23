package meijia.com.meijianet.activity;

import android.content.Context;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.adpter.ViewHolder;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/23
 */
public class MyAdapter extends CommonRecyclerAdapter<String> {
    public MyAdapter(Context context, List<String> data) {
        super(context, data, R.layout.rv_item);
    }

    @Override
    public void convert(ViewHolder holder, List<String> data, int position) {
        holder.setText(R.id.tv,data.get(position));
    }
}
