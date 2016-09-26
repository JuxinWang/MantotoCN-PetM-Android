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
import com.petm.property.activities.PetInfoActivity;
import com.petm.property.model.Pet;
import com.petm.property.utils.ToastU;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class PetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Pet> pets;
    private Context mContext;
    private String flag;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private int selectPosition = -1;
    DisplayImageOptions options;
    OnDeletePetListener mOnDeletePetListener;
    OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void selectPosition(int position){
        selectPosition = position;
    }
    public PetsAdapter(Context mContext,List<Pet> pets,String flag) {
        this.pets = pets;
        this.mContext = mContext;
        this.flag = flag;
        mLayoutInflater = LayoutInflater.from(mContext);
        mOnDeletePetListener = (OnDeletePetListener) mContext;
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg,delete_pet;
        TextView textView;
        public ContentViewHolder(View itemView) {
            super(itemView);
            delete_pet = (ImageView) itemView.findViewById(R.id.delete_pet);
            mImg = (ImageView) itemView.findViewById(R.id.petImg);
            textView = (TextView) itemView.findViewById(R.id.petName);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_pet,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).textView.setText(pets.get(position).petname);
        if (selectPosition == position){
            ((ContentViewHolder) holder).textView.setBackground(mContext.getResources().getDrawable(R.drawable.pet_name_main_bg));
        }else {
            ((ContentViewHolder) holder).textView.setBackground(mContext.getResources().getDrawable(R.drawable.pet_name_bg));
        }
        if (flag.equals("petshop")){
            ((ContentViewHolder) holder).delete_pet.setVisibility(View.GONE);
            ((ContentViewHolder) holder).mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.OnItemClick(holder.itemView,holder.getLayoutPosition());
                }
            });
        }else {
            ((ContentViewHolder) holder).mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, PetInfoActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        String imgUrl = pets.get(position).media.path;
        if (!imgUrl.equals(((ContentViewHolder) holder).mImg.getTag())){
            ((ContentViewHolder) holder).mImg.setTag(imgUrl);
            imageLoader.displayImage(pets.get(position).media.path,((ContentViewHolder)holder).mImg,options);
        }
        ((ContentViewHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((ContentViewHolder) holder).delete_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDeletePetListener.OnDeletePet(pets.get(holder.getLayoutPosition()).petid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public interface OnDeletePetListener{
        void OnDeletePet(long petid);
    }

    public interface OnItemClickListener{
        void OnItemClick(View view,int position);
    }
}
