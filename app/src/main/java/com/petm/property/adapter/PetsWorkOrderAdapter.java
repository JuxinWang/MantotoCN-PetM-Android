package com.petm.property.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.petm.property.R;
import com.petm.property.model.Pet;
import com.petm.property.model.PetWorkOrder;
import com.petm.property.utils.DateHelper;

import java.util.List;

/**
 * Created by Mr.liu
 * On 2016/9/18
 * At 9:47
 * PetM
 */
public class PetsWorkOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Pet> pets;
    private List<PetWorkOrder> workOrders;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public PetsWorkOrderAdapter(Context mContext, List<Pet> pets, List<PetWorkOrder> workOrders) {
        this.pets = pets;
        this.workOrders = workOrders;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView textView;
        LinearLayout workLL;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.petImg);
            textView = (TextView) itemView.findViewById(R.id.petName);
            workLL = (LinearLayout) itemView.findViewById(R.id.work_linear);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_work_order, parent, false));
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
        String imgPath = pets.get(position).media.path;
        if (workOrders!=null) {
            for (int i = 0; i < workOrders.size(); i++) {
                if (pets.get(position).petid == workOrders.get(i).petid) {
                    TextView mTv = new TextView(mContext);
                    mTv.setText("上次" + workOrders.get(i).petService.petservicename + "的时间为" + DateHelper.getStringTime(workOrders.get(i).btime, "yyyy.MM.dd") + "日。");
                    mTv.setSingleLine();
                    ((ContentViewHolder) holder).workLL.addView(mTv);
                }
            }
        }
        if (!imgPath.equals(((ContentViewHolder) holder).mImg.getTag())) {
            ((ContentViewHolder) holder).mImg.setTag(imgPath);
            imageLoader.displayImage(pets.get(position).media.path, ((ContentViewHolder) holder).mImg, options);
        }
        imageLoader.displayImage(pets.get(position).media.path, ((ContentViewHolder) holder).mImg, options);
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
