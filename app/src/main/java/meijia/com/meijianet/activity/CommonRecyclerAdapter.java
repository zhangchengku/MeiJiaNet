package meijia.com.meijianet.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import meijia.com.meijianet.adpter.ViewHolder;
import meijia.com.meijianet.api.OnItemClickListener;
import meijia.com.meijianet.api.OnLongClickListener;

//通用的Adapter
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    //数据
    protected List<T> mData;
    // 布局
    private int mLayoutId;


    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mLayoutId = layoutId;
    }


    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(mLayoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder.getAdapterPosition());
                }
            });
        }

        // 绑定数据
        convert(holder, mData,position);
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param
     * @param position
     */
    public abstract void convert(ViewHolder holder, List<T> data, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /***************
     * 给条目设置点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
