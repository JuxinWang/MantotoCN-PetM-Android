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
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.petm.property.R;
import com.petm.property.model.Beautician;
import com.petm.property.model.Pet;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class BeauticianAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Beautician> beauticians;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    OnItemClickLister mOnItemClickListener;
    private int selectposition = -1;
    public void setOnItemClickLitener(OnItemClickLister mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public BeauticianAdapter(Context mContext, List<Beautician> beauticians) {
        this.beauticians = beauticians;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void selectPosition(int position){
        selectposition = position;
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
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_beautician,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).textView.setText(beauticians.get(position).user.truename);
        if (selectposition == position){
            ((ContentViewHolder) holder).textView.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
        }else {
            ((ContentViewHolder) holder).textView.setBackgroundColor(mContext.getResources().getColor(R.color.whiter));
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .build();//构建完成
        String imgUrl = beauticians.get(position).user.headshot;
        if (imgUrl==null){
            imageLoader.displayImage(beauticians.get(position).user.headshot, ((ContentViewHolder) holder).mImg, options);
        }else {
            if (!imgUrl.equals(((ContentViewHolder) holder).mImg.getTag())){
                ((ContentViewHolder) holder).mImg.setTag(imgUrl);
                ImageAware imageAware = new ImageViewAware(((ContentViewHolder) holder).mImg, false);
                imageLoader.displayImage(beauticians.get(position).user.headshot,imageAware , options);
            }
        }
        ((ContentViewHolder) holder).mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.OnItemClick(holder.itemView,pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beauticians.size();
    }
    public interface OnItemClickLister{
        void OnItemClick(View view,int position);
    }
}
