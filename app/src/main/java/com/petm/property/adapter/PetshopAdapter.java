package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.petm.property.model.PetShop;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/13
 * At 12:52
 * PetM
 */
public class PetshopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PetShop> petShops;
    private Context mContext;

    public PetshopAdapter(List<PetShop> petShops, Context mContext) {
        this.petShops = petShops;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
