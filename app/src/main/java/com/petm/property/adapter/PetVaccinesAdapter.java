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
import com.petm.property.utils.DateHelper;
import com.petm.property.views.TimePopupWindow;

import org.json.JSONArray;
import org.json.JSONException;

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
    private Map<Integer, String> maps = new HashMap<>();
    // private Map<String, String> editMaps = new HashMap<>();
    private String[] vaccinTime;
    private String[] vaccinids;
    private String[] petvaccinids;
    private JSONArray vaccin_array;

    public PetVaccinesAdapter(Context mContext, List<PetVaccines> petVaccines, Map<Integer, String> maps,String[] vaccinTime,String[] vaccinids,String[] petvaccinids,JSONArray vaccin_array) {
        this.mContext = mContext;
        this.petVaccines = petVaccines;
        this.maps = maps;
        this.vaccinTime = vaccinTime;
        this.vaccinids = vaccinids;
        this.petvaccinids = petvaccinids;
        this.vaccin_array = vaccin_array;
        mLayoutInflate = LayoutInflater.from(mContext);
        onDateChangedListener = (OnDateChangedListener) mContext;
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView petVaccineName, petVaccineDate;
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
        return new ContentViewHolder(mLayoutInflate.inflate(R.layout.item_petvaccines, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int temp = position;
        ((ContentViewHolder) holder).petVaccineName.setText("上次" + petVaccines.get(position).vaccinname);
        if (vaccinids!=null&&vaccinids.length > 0) {
            for (int i =0 ;i<vaccinids.length;i++){
                if (vaccinids[i].equals(""+petVaccines.get(position).vaccinid)){
                    ((ContentViewHolder) holder).petVaccineDate.setText(DateHelper.getStringTime(vaccinTime[i], "yyyy.MM.dd"));
                }
            }
        }
        if (maps!=null&&maps.size()>0){
            ((ContentViewHolder) holder).petVaccineDate.setText(maps.get(position));
        }
        ((ContentViewHolder) holder).selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String petvaccinid = "";
                if (vaccinids!=null&&vaccinids.length > 0) {
                    for (int i =0 ;i<vaccinids.length;i++){
                        if (vaccinids[i].equals(""+petVaccines.get(position).vaccinid)){
                            petvaccinid = petvaccinids[i];
                        }
                    }
                }
                lists.add("" + petVaccines.get(temp).vaccinid);
                onDateChangedListener.OnDateChanged("" + petVaccines.get(temp).vaccinid, position,vaccin_array,maps,petvaccinid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petVaccines.size();
    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, String data);
    }

    public interface OnDateChangedListener {
        void OnDateChanged(String string, int position,JSONArray vaccin_array,Map<Integer, String> maps,String petvaccinid);
    }
}
