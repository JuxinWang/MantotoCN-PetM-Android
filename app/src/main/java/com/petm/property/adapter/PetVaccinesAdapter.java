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
import com.petm.property.views.TimePopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    OnDateChangedListener onDateChangedListener;
    private List<String> lists = new ArrayList<>();
    private Map<Integer,String> maps = new HashMap<>();
    public PetVaccinesAdapter(Context mContext, List<PetVaccines> petVaccines, Map<Integer,String> maps) {
        this.mContext = mContext;
        this.petVaccines = petVaccines;
        this.maps = maps;
        mLayoutInflate = LayoutInflater.from(mContext);
        onDateChangedListener = (OnDateChangedListener) mContext;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int temp = position;
        ((ContentViewHolder)holder).petVaccineName.setText("上次" + petVaccines.get(position).vaccinname);
        ((ContentViewHolder) holder).petVaccineDate.setText(maps.get(position));
        ((ContentViewHolder)holder).selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lists.add("" + petVaccines.get(temp).vaccinid);
                onDateChangedListener.OnDateChanged(""+petVaccines.get(temp).vaccinid, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petVaccines.size();
    }

    public static interface OnItemClickListener{
        void OnItemClick(View view,String data);
    }

    public interface OnDateChangedListener{
        void OnDateChanged(String string,int position);
    }
}
