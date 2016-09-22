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
import com.petm.property.model.PetService;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class ServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PetService> petServices;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int selectPosition = -1;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener)
    {
        this.mOnItemClickListener = mOnItemClickLitener;
    }
    public ServicesAdapter(Context mContext, List<PetService> petServices) {
        this.petServices = petServices;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.petName);
        }
    }

    public void selectPosition(int position){
        selectPosition = position;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_service,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ContentViewHolder) holder).textView.setText(petServices.get(position).petservicename);
        if (position == selectPosition){
            ((ContentViewHolder) holder).textView.setBackground(mContext.getResources().getDrawable(R.drawable.pet_service_main_bg));
        }else {
            ((ContentViewHolder) holder).textView.setBackground(mContext.getResources().getDrawable(R.drawable.pet_service_bg));
        }
        ((ContentViewHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petServices.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
