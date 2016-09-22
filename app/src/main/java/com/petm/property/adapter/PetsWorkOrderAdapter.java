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

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class PetsWorkOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Pet> pets;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    public PetsWorkOrderAdapter(Context mContext, List<Pet> pets) {
        this.pets = pets;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView textView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.petImg);
            textView = (TextView) itemView.findViewById(R.id.petName);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_work_order,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).textView.setText(pets.get(position).petname);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        imageLoader.displayImage(pets.get(position).media.path,((ContentViewHolder)holder).mImg,options);
        ((ContentViewHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }
}
