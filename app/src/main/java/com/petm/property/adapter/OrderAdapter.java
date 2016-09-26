package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.model.PetWorkOrder;
import com.petm.property.model.UserGift;
import com.petm.property.utils.DateHelper;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PetWorkOrder> workOrders;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    OnCancelOrderListener mOnCancelOrderListener;
    public OrderAdapter(Context mContext, List<PetWorkOrder> workOrders) {
        this.workOrders = workOrders;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setmOnCancelOrderListener(OnCancelOrderListener mOnCancelOrderListener) {
        this.mOnCancelOrderListener = mOnCancelOrderListener;
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView orderContent;
        Button cancel;
        public ContentViewHolder(View itemView) {
            super(itemView);
            orderContent = (TextView) itemView.findViewById(R.id.order_content);
            cancel = (Button) itemView.findViewById(R.id.order_cancel);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_order_info,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ContentViewHolder) holder).orderContent.setText(DateHelper.getStringTime(workOrders.get(position).btime,"yyyy年MM月dd日 HH:mm")+"点 在 \""+workOrders.get(position).beautician.petShopInfo.businessname+" \"预约美容师"+workOrders.get(position).beautician.user.username+"为"+workOrders.get(position).pet.petname+workOrders.get(position).petService.petservicename);
        ((ContentViewHolder) holder).cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCancelOrderListener.OnCancelOrder(workOrders.get(position).petworkorderid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }

    public interface OnCancelOrderListener{
        void OnCancelOrder(long workorderid);
    }
}
