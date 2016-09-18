package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petm.property.R;
import com.petm.property.model.PetVaccines;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 14:05
 * PetM
 */
public class PetVaccinesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PetVaccines> petVaccines;
    private LayoutInflater mLayoutInflate;

    public PetVaccinesAdapter(Context mContext, List<PetVaccines> petVaccines) {
        this.mContext = mContext;
        this.petVaccines = petVaccines;
        mLayoutInflate = LayoutInflater.from(mContext);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder{
        TextView petVaccineName,petVaccineDate;
        ImageView selectImg;
        public ContentViewHolder(View itemView) {
            super(itemView);
            petVaccineName = (TextView) itemView.findViewById(R.id.pet_vaccine_name);
            petVaccineDate = (TextView) itemView.findViewById(R.id.pet_vaccine_date);
            selectImg = (ImageView) itemView.findViewById(R.id.pet_vaccine_date_select);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflate.inflate(R.layout.item_petvaccines,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder)holder).petVaccineName.setText("上次" + petVaccines.get(position).vaccinname);
    }

    @Override
    public int getItemCount() {
        return petVaccines.size();
    }

    public static interface OnItemClickListener{
        void OnItemClick(View view,String data);
    }
}
