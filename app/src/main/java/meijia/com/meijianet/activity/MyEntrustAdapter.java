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
import meijia.com.meijianet.vo.myentrust.MyEntrustInfo;
import meijia.com.meijianet.vo.myentrust.MyEntrustVo;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 * Create：2018/3/29
 */
public class MyEntrustAdapter extends CommonRecyclerAdapter<MyEntrustVo> {

    public MyEntrustAdapter(Context context, List<MyEntrustVo> data) {
        super(context, data, R.layout.item_rv_entrust);
    }

    @Override
    public void convert(ViewHolder holder, List<MyEntrustVo> data, int position) {
        MyEntrustVo entrustVo = data.get(position);
        MyEntrustInfo employee = entrustVo.getEmployee();
        if (entrustVo.getPiclogo()!=null && !entrustVo.getPiclogo().equals("")){
            holder.setImageByUrl(R.id.iv_ac_myentrust_house, entrustVo.getPiclogo());
        }
        holder.setText(R.id.tv_ac_myentrust_housename,entrustVo.getName());
        holder.setText(R.id.tv_ac_myentrust_price,"¥"+entrustVo.getPrice()+"万");
        holder.setText(R.id.tv_ac_myentrust_quyu,entrustVo.getAcreage());
        int status = entrustVo.getConsignationStatus();
        if (status == 0){
            holder.setText(R.id.tv_ac_myentrust_status, "待委托");
        }else if (status == 1){
            holder.setText(R.id.tv_ac_myentrust_status, "已委托");
        }else {
            holder.setText(R.id.tv_ac_myentrust_status, "拒委托");
        }

        if (employee!=null){
            holder.setText(R.id.tv_ac_myentrust_name,employee.getNickname());
            holder.setText(R.id.tv_ac_myentrust_phone,employee.getPhone());
        }else {
            holder.setViewVisibility(R.id.ll_fangxiaoer,View.GONE);
        }

        holder.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onPicClick(position);
                }
            }
        });
        holder.setText(R.id.tv_ac_myentrust_address,"服务区域："+entrustVo.getName());
        setTextBigSize(holder.getView(R.id.tv_ac_myentrust_price));
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
