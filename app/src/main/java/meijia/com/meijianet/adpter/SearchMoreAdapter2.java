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

public class SearchMoreAdapter2 extends CommonRecyclerAdapter<NewHouseInfo> {
    public SearchMoreAdapter2(Context context, List<NewHouseInfo> data) {
        super(context, data, R.layout.item_rv_fangyuan);
    }

    @Override
    public void convert(ViewHolder holder, List<NewHouseInfo> data, int position) {
        NewHouseInfo houseInfo = data.get(position);
        holder.setText(R.id.tv_item_fangyuan_title, houseInfo.getTitle());
        holder.setText(R.id.tv_item_fangyuan_price, subZeroAndDot(houseInfo.getTotalprice()));
        holder.setText(R.id.tv_item_fangyuan_msg,houseInfo.getRoom()+"室"+houseInfo.getHall()+
                "厅"+houseInfo.getToilet()+"卫 | "+subZeroAndDot(houseInfo.getAcreage())+"㎡|第"+houseInfo.getStorey()+"层/共"+houseInfo.getSumfloor()+"层");
        holder.setText(R.id.tv_item_fangyuan_address,houseInfo.getAddress());
        holder.setText(R.id.tv2,houseInfo.getBrowse_count()+"");
        holder.setText(R.id.tv1,houseInfo.getIntentionCount()+"");
        Glide.with(mContext)
                .load(houseInfo.getPiclogo())
                .placeholder(R.mipmap.icon_fang_defout)
                .error(R.mipmap.icon_fang_defout)
                .into(((ImageView) holder.getView(R.id.iv_item_fangyuan)));
//        setTextBigSize(((TextView) holder.getView(R.id.tv_item_fangyuan_price)));
//        setM2(((TextView) holder.getView(R.id.tv_item_fangyuan_msg)));

        String application = houseInfo.getApplication();
        String type = "";
        switch (application) {
            case "2":
                type = "单体别墅";
                break;
            case "5":
                type = "排屋";
                break;
            case "7":
                type = "多层";
                break;
            case "1":
                type = "复式";
                break;
            case "4":
                type = "小高楼";
                break;
            case "3":
                type = "写字楼";
                break;
            case "6":
                type = "店面";
                break;
        }
        holder.setText(R.id.tv_type,type);
    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    private void setTextBigSize(TextView textView) {
        String text = textView.getText().toString().trim();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(20, true);
        builder.setSpan(sizeSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F44848"));
        builder.setSpan(colorSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    private void setM2(TextView tv){
        SpannableString m2 = new SpannableString("m2");
        m2.setSpan(new RelativeSizeSpan(0.7f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tv.getText().toString().trim());
        spannableStringBuilder.append(m2);
        tv.setText(spannableStringBuilder);
    }
}
