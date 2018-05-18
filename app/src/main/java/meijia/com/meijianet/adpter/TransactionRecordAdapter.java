package meijia.com.meijianet.adpter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CommonRecyclerAdapter;
import meijia.com.meijianet.activity.NewHouseInfo;

/**
 * Created by Administrator on 2018/4/26.
 */

public class TransactionRecordAdapter extends CommonRecyclerAdapter<NewHouseInfo> {
    public TransactionRecordAdapter(Context context, List<NewHouseInfo> data) {
        super(context, data, R.layout.item_transaction_record);
    }

    @Override
    public void convert(ViewHolder holder, List<NewHouseInfo> data, int position) {
        NewHouseInfo houseInfo = data.get(position);
        holder.setText(R.id.tv_item_fangyuan_title, houseInfo.getTitle());
        holder.setText(R.id.tv_item_fangyuan_price, subZeroAndDot(houseInfo.getTotalprice()));
        holder.setText(R.id.tv_item_fangyuan_msg, houseInfo.getRoom() + "室" + houseInfo.getHall() +
                "厅" + houseInfo.getToilet() + "卫 | " + subZeroAndDot(houseInfo.getAcreage()) + "㎡|第" + houseInfo.getMstorey() + "层/共" + houseInfo.getSumfloor() + "层");
        holder.setText(R.id.tv_item_fangyuan_address, "签约日期："+String.valueOf(houseInfo.getTradetime()));
        Glide.with(mContext)
                .load(houseInfo.getPiclogo())
                .placeholder(R.mipmap.icon_fang_defout)
                .error(R.mipmap.icon_fang_defout)
                .into(((ImageView) holder.getView(R.id.iv_item_fangyuan)));

    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

}
