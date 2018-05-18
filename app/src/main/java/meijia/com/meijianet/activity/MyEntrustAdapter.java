package meijia.com.meijianet.activity;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
        holder.setText(R.id.iv_ac_myentrust_price,"¥"+subZeroAndDot(entrustVo.getPrice())+"万");
        holder.setText(R.id.iv_ac_myentrust_address,entrustVo.getAddress());
        holder.setText(R.id.iv_ac_myentrust_village,entrustVo.getName());
        holder.setText(R.id.iv_ac_myentrust_name,entrustVo.getContactname());
        holder.setText(R.id.iv_ac_myentrust_phone,entrustVo.getContactphone());
        int status = entrustVo.getConsignationStatus();
        if (status == 0){
            holder.setText(R.id.iv_ac_myentrust_status, "审核中");
            holder.getView(R.id.iv_ac_myentrust_status).setBackgroundColor(Color.parseColor("#F4A361"));
        }else if (status == 1){
            holder.setText(R.id.iv_ac_myentrust_status, "已审核");
            holder.getView(R.id.iv_ac_myentrust_status).setBackgroundColor(Color.parseColor("#44D18B"));
        }else {
            holder.setText(R.id.iv_ac_myentrust_status, "已被拒");
            holder.getView(R.id.iv_ac_myentrust_status).setBackgroundColor(Color.parseColor("#999999"));
        }





    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    private onPicClickListener mListener;


    public void setOnPicClickListener(onPicClickListener listener) {
        mListener = listener;
    }

    public interface onPicClickListener{
        void onPicClick(int position);
    }
}
