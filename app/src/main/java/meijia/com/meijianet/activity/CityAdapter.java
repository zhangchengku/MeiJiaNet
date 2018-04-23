package meijia.com.meijianet.activity;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.adpter.ViewHolder;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/2/24
 */
public class CityAdapter extends CommonRecyclerAdapter<String> {
    private Context mContext;
    public CityAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_search_city);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder holder, List<String> data, int position) {
        holder.setText(R.id.tv_item_city,data.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                ((TextView) holder.getView(R.id.tv_item_city)).setTextColor(mContext.getResources().getColor(R.color.zhu_color));
                holder.getView(R.id.tv_item_city).setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_city_selected));
            } else {
                ((TextView) holder.getView(R.id.tv_item_city)).setTextColor(mContext.getResources().getColor(R.color.hint_color));
                holder.getView(R.id.tv_item_city).setBackground(mContext.getResources().getDrawable(R.drawable.bg_item_city_normal));
            }
        }
    }
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }
}
