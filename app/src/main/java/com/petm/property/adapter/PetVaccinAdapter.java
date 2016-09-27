package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.model.PetVaccin;
import com.petm.property.model.UserGift;
import com.petm.property.utils.DateHelper;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class PetVaccinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PetVaccin> petVaccins;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PetVaccinAdapter(Context mContext, List<PetVaccin> petVaccins) {
        this.petVaccins = petVaccins;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView petVaccinInfo;
        public ContentViewHolder(View itemView) {
            super(itemView);
            petVaccinInfo = (TextView) itemView.findViewById(R.id.pet_vaccine_info);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_petvaccin_info,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder)holder).petVaccinInfo.setText(petVaccins.get(position).vaccin.vaccinname+"日期："+ DateHelper.getStrTime(petVaccins.get(position).vtime,"yyyy.MM.dd"));
    }

    @Override
    public int getItemCount() {
        return petVaccins.size();
    }
}
