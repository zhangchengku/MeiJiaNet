package meijia.com.meijianet.activity;

import android.content.Context;
import android.view.View;

import java.util.List;

import meijia.com.meijianet.R;
import meijia.com.meijianet.adpter.ViewHolder;
import meijia.com.meijianet.vo.intention.IntentionInfo;
import meijia.com.meijianet.vo.intention.IntentionVo;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/22
 */
public class IntentionFragmentAdapter extends CommonRecyclerAdapter<IntentionVo> {
    public IntentionFragmentAdapter(Context context, List<IntentionVo> data) {
        super(context, data, R.layout.item_rv_intention);
    }

    @Override
    public void convert(ViewHolder holder, List<IntentionVo> data, int position) {
        IntentionVo intentionVo = data.get(position);
        IntentionInfo house = intentionVo.getHouse();
        holder.setImageByUrl(R.id.iv_item_fangyuan, house.getPiclogo());
        holder.setText(R.id.tv_item_weituo_name,house.getTitle());
        holder.setText(R.id.tv_item_weituo_quyu,"意向日期："+ intentionVo.getCreateTime());
        String pay = "";
        if (intentionVo.getPay() == 0){
            pay = "意向金：未缴纳";
            holder.setText(R.id.tv_item_weituo_look,"去支付");
            holder.setText(R.id.tv_item_weituo_tuikuan,"取消");
        }else if (intentionVo.getPay() == 1){
            pay = "意向金：已缴纳";
            holder.setText(R.id.tv_item_weituo_look,"查看");
            holder.setText(R.id.tv_item_weituo_tuikuan,"退款");
        }else if (intentionVo.getPay() == -1){
            pay = "意向金：已缴纳";
            holder.setText(R.id.tv_item_weituo_look,"查看");
            holder.setText(R.id.tv_item_weituo_tuikuan,"退款");
        }else {
            pay = "意向金：待退款";
            holder.setText(R.id.tv_item_weituo_look,"查看");
            holder.setViewVisibility(R.id.tv_item_weituo_tuikuan, View.GONE);
        }
        holder.setText(R.id.tv_item_weituo_status,pay);
    }
}
