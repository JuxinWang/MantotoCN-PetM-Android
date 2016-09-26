package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.model.Pet;
import com.petm.property.model.UserGift;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class ConsumeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserGift> userGifts;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public ConsumeAdapter(Context mContext, List<UserGift> userGifts) {
        this.userGifts = userGifts;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView giftName,giftCount,giftUnit;
        public ContentViewHolder(View itemView) {
            super(itemView);
            giftName = (TextView) itemView.findViewById(R.id.giftName);
            giftCount = (TextView) itemView.findViewById(R.id.giftCount);
            giftUnit = (TextView) itemView.findViewById(R.id.giftUnit);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_consume_info,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).giftName.setText(userGifts.get(position).source+"剩余");
        ((ContentViewHolder) holder).giftCount.setText(""+(int)(userGifts.get(position).value/userGifts.get(position).gift.defaultvalue));
        ((ContentViewHolder) holder).giftUnit.setText("次");
    }

    @Override
    public int getItemCount() {
        return userGifts.size();
    }
}
