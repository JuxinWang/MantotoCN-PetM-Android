package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.petm.property.R;
import com.petm.property.model.PetWorkOrder;
import com.petm.property.utils.DateHelper;
import com.petm.property.utils.LogU;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class RemindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PetWorkOrder> workOrders;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public RemindAdapter(Context mContext, List<PetWorkOrder> workOrders) {
        this.workOrders = workOrders;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView remindDate, remindContent;

        public ContentViewHolder(View itemView) {
            super(itemView);
            remindDate = (TextView) itemView.findViewById(R.id.remind_date);
            remindContent = (TextView) itemView.findViewById(R.id.remind_content);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_remind, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position > 0) {
            LogU.i("MyLog",DateHelper.getStringTime(workOrders.get(position).btime.substring(0,10)));
            if (DateHelper.getStringTime(workOrders.get(position).btime.substring(0,10), "yyyy年MM月dd日 HH:mm").equals(DateHelper.getStringTime(workOrders.get(position-1).btime.substring(0,10), "yyyy年MM月dd日 HH:mm"))) {
                ((ContentViewHolder) holder).remindDate.setVisibility(View.GONE);
            } else {
                ((ContentViewHolder) holder).remindDate.setText(DateHelper.getStringTime(workOrders.get(position).btime, "yyyy年MM月dd日 HH:mm"));
            }
        } else {
            ((ContentViewHolder) holder).remindDate.setText(DateHelper.getStringTime(workOrders.get(position).btime, "yyyy年MM月dd日 HH:mm"));
        }
        ((ContentViewHolder) holder).remindContent.setText("您的宠物\" "+ workOrders.get(position).pet.petname+"\" 在" + workOrders.get(position).beautician.petShopInfo.businessname+"预约的" +DateHelper.getStringTime(workOrders.get(position).btime, "yyyy年MM月dd日 HH:mm") + "点的" +workOrders.get(position).petService.petservicename+",还差30分钟就要到时，请及时将"+ workOrders.get(position).pet.petname + "送达店铺，谢谢！");
    }

    @Override
    public int getItemCount() {
        return workOrders.size();
    }
}
