package com.petm.property.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.activities.PetCenterActivity;
import com.petm.property.model.InfoPetShop;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/13
 * At 12:52
 * PetM
 */
public class AddPetshopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<InfoPetShop> petShops;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    OnDeletePetshopListener mOnDeletePetshopListener;
    public AddPetshopAdapter(Context mContext, List<InfoPetShop> petShops) {
        this.petShops = petShops;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mOnDeletePetshopListener = (OnDeletePetshopListener) mContext;
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView textView;
        Button delete;
        public ContentViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.petImg);
            textView = (TextView) itemView.findViewById(R.id.petName);
            delete = (Button) itemView.findViewById(R.id.delete);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_add_petshop,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
        ((ContentViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeletePetshopListener.OnDeletePetshop(petShops.get(holder.getLayoutPosition()).petShop.petshopid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petShops.size();
    }

    public interface OnDeletePetshopListener{
        void OnDeletePetshop(long petshopid);
    }
}
