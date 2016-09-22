package com.petm.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.activities.PetCenterActivity;
import com.petm.property.model.InfoPetShop;
import com.petm.property.model.PetShop;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/13
 * At 12:52
 * PetM
 */
public class PetshopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<InfoPetShop> petShops;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    public PetshopAdapter( Context mContext,List<InfoPetShop> petShops) {
        this.petShops = petShops;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView textView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.petshopImg);
            textView = (TextView) itemView.findViewById(R.id.petshopName);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_petshop,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ContentViewHolder) holder).textView.setText(petShops.get(position).businessname);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        String imgPath = petShops.get(position).media.path;
        if (!imgPath.equals(((ContentViewHolder) holder).mImg.getTag())){
            ((ContentViewHolder) holder).mImg.setTag(imgPath);
            imageLoader.displayImage(petShops.get(position).media.path,((ContentViewHolder)holder).mImg,options);
        }
        ((ContentViewHolder) holder).mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, PetCenterActivity.class);
                intent.putExtra("petshopid",petShops.get(position).petShop.petshopid);
                intent.putExtra("flag","petshop");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petShops.size();
    }
}
