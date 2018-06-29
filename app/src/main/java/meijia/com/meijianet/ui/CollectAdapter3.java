package meijia.com.meijianet.ui;

import android.app.AlertDialog;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meijia.com.meijianet.R;
import meijia.com.meijianet.activity.CollectAdapter;
import meijia.com.meijianet.activity.CommonRecyclerAdapter;
import meijia.com.meijianet.activity.MyCollectInfo;
import meijia.com.meijianet.adpter.ViewHolder;
import meijia.com.meijianet.api.ResultCallBack;
import meijia.com.meijianet.base.BaseURL;
import meijia.com.meijianet.vo.intention.IntentionVo;
import static meijia.com.meijianet.api.URL.TUI_KUAN;
import static meijia.com.meijianet.api.URL.CANCLE_ORDER;
/**
 * Created by Administrator on 2018/4/28.
 */
public class CollectAdapter3 extends CommonRecyclerAdapter<IntentionVo> {
    private boolean isCheck ;
    private Map<Integer,Boolean> map = new HashMap<>();
    private CollectAdapter.onMyItemClickListener mListener;
    private AlertDialog dialog;


    public interface onMyItemClickListener{
        void onItemClick(int positon);
    }

    public void setOnMyItemClickListener(CollectAdapter.onMyItemClickListener listener){
        this.mListener = listener;
    }

    public CollectAdapter3(Context context, List<IntentionVo> data) {
        super(context, data, R.layout.item_rv_collect3);
        isCheck = false;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < mData.size(); i++) {
            map.put(i,false);
        }
    }



    @Override
    public void convert(final ViewHolder holder, List<IntentionVo> data, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onItemClick(position);
                }
            }
        });

        IntentionVo houseInfo = data.get(position);
        Glide.with(mContext)
                .load(houseInfo.getHouse().getPiclogo())
                .placeholder(R.mipmap.icon_fang_defout)
                .error(R.mipmap.icon_fang_defout)
                .into(((ImageView) holder.getView(R.id.iv_item_fangyuan)));//图
        holder.setText(R.id.tv_item_fangyuan_title, houseInfo.getHouse().getTitle());//标题
        holder.setText(R.id.tv_item_fangyuan_price, subZeroAndDot(String.valueOf(houseInfo.getHouse().getTotalprice())));//价格
        holder.setText(R.id.tv_item_fangyuan_msg,houseInfo.getHouse().getRoom()+"室"+houseInfo.getHouse().getHall()+
                "厅"+houseInfo.getHouse().getToilet()+"卫 | "+subZeroAndDot(String.valueOf(houseInfo.getHouse().getAcreage()))+"㎡|第"+houseInfo.getHouse().getMstorey()+"层/共"+houseInfo.getHouse().getSumfloor()+"层");
        holder.setText(R.id.tv_item_fangyuan_address,houseInfo.getHouse().getMemAddress());
        String pay = String.valueOf(houseInfo.getPay());
        switch (pay) {
            case "0":
//                holder.setImageResource(R.id.tv_type,R.mipmap.yixiang_wj);
                holder.setViewVisibility(R.id.quxiaoshouc,View.VISIBLE);
                holder.setText(R.id.quxiaoshouc,"取消预约");
                holder.getView(R.id.quxiaoshouc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeData(position);
                    }
                });
                break;
            case "1":
//                holder.setImageResource(R.id.tv_type,R.mipmap.yixiang_yj);
                holder.setViewVisibility(R.id.quxiaoshouc,View.VISIBLE);
                holder.setText(R.id.quxiaoshouc,"申请退款");
                holder.getView(R.id.quxiaoshouc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show(position,holder);
                    }
                });
                break;
            case "2":
//                holder.setImageResource(R.id.tv_type,R.mipmap.yixiang_tkcg);
                holder.setViewVisibility(R.id.quxiaoshouc,View.VISIBLE);
                holder.setText(R.id.quxiaoshouc,"取消预约");
                holder.getView(R.id.quxiaoshouc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeData(position);
                    }
                });
                break;
            case "-1":
                holder.setViewVisibility(R.id.tv_type,View.VISIBLE);
                holder.setImageResource(R.id.tv_type,R.mipmap.yixiang_sqz);
                holder.setViewVisibility(R.id.quxiaoshouc,View.GONE);
                break;


        }

    }
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    private void removeData(int position) {

        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + CANCLE_ORDER)
                .addParams("ordernum", String.valueOf(mData.get(position).getOrdernum()))
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        mData.remove(position);
                        //删除动画
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
    }
    private void show(int position,ViewHolder holder) {
        LayoutInflater factory = LayoutInflater.from(mContext);//提示框
        final View view = factory.inflate(R.layout.dialog_layout, null);//这里必须是final的
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        TextView texttt = (TextView) view.findViewById(R.id.queding);
        dialog = new AlertDialog.Builder(mContext).create();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        texttt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay(position,holder);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();

    }
    private void pay(int position,ViewHolder holder) {
        OkHttpUtils.post()
                .tag(this)
                .url(BaseURL.BASE_URL + TUI_KUAN)
                .addParams("ordernum", String.valueOf(mData.get(position).getOrdernum()))
                .build()
                .execute(new ResultCallBack() {
                    @Override
                    public void onSuccess(String body) {
                        holder.setImageResource(R.id.tv_type,R.mipmap.yixiang_sqz);
                        holder.setViewVisibility(R.id.quxiaoshouc,View.GONE);
                    }
                    @Override
                    public void onFail(int returnCode, String returnTip) {

                    }

                    @Override
                    public void onAfter(int id) {

                    }
                });
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

    public void setCheck(){
        isCheck = !isCheck;
        notifyDataSetChanged();
    }

    public Map<Integer,Boolean> getMap(){
        return map;
    }
}
