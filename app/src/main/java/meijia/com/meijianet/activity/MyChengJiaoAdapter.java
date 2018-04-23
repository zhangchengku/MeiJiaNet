package meijia.com.meijianet.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.adpter.ViewHolder;
import meijia.com.meijianet.bean.ChengJiaoVo;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/29
 */
public class MyChengJiaoAdapter extends CommonRecyclerAdapter<ChengJiaoVo> {

    public MyChengJiaoAdapter(Context context, List<ChengJiaoVo> data) {
        super(context, data, R.layout.item_rv_chengjiao);
    }

    @Override
    public void convert(ViewHolder holder, List<ChengJiaoVo> data, int position) {
        ChengJiaoVo entrustVo = data.get(position);
        String piclogo = entrustVo.getPiclogo();
        if (!piclogo.equals("")){
            holder.setImageByUrl(R.id.iv_item_chengjiao,piclogo);
        }
        holder.setText(R.id.tv_item_name,entrustVo.getName());
        holder.setText(R.id.tv_item_title,entrustVo.getTitle());
        holder.setText(R.id.tv_item_date,entrustVo.getTradetime());
        holder.getView(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onPicClick(position);
                }
            }
        });

    }

    private void setTextBigSize(TextView textView) {
        String text = textView.getText().toString().trim();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(20, true);
        builder.setSpan(sizeSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ffa64e"));
        builder.setSpan(colorSpan, 0, text.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
    private onPicClickListener mListener;


    public void setOnPicClickListener(onPicClickListener listener) {
        mListener = listener;
    }

    public interface onPicClickListener{
        void onPicClick(int position);
    }
}
