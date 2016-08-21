package com.yuanmu.allblue.fragment.online_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.activity.ImageDisplayActivity;
import com.yuanmu.allblue.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyHolder> {

    private Context context;
    private List<PhotosPhotosBean.PhotosBean> photoBeanList;
    private ArrayList<String> imageUrls = new ArrayList<>();

    public MyRecyclerViewAdapter(Context context, List<PhotosPhotosBean.PhotosBean> photoBeanList){
        this.context = context;
        this.photoBeanList = photoBeanList;
        for(PhotosPhotosBean.PhotosBean photosBean:photoBeanList){
            imageUrls.add(photosBean.getImage());
        }
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_onlines_item_child,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Glide.with(context).load(photoBeanList.get(position).getImage()).placeholder(R.drawable.bcg_image_view).centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ImageDisplayActivity.class);
                intent.putExtra(ImageDisplayActivity.IMAGE_INDEX,position);
                intent.putExtra(ImageDisplayActivity.PHOTOBEAN_LIST,imageUrls);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoBeanList == null ? 0 :photoBeanList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_fragment_onlines);
        }
    }
}
