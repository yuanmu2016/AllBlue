package com.yuanmu.allblue.fragment.group_fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.R;
import com.yuanmu.allblue.activity.ImageDisplayActivity;

import java.util.ArrayList;
import java.util.List;


public class EmbedGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<GroupTopicsBean.TopicsBean.PhotosBean> photoBeanList;
    private ArrayList<String> imageUrls;
    public EmbedGridViewAdapter(Context context, List<GroupTopicsBean.TopicsBean.PhotosBean> photoBeanList){
        this.context = context;
        this.photoBeanList = photoBeanList;

        imageUrls = new ArrayList<>();
        for(int i = 0;i<photoBeanList.size();i++){
            imageUrls.add(photoBeanList.get(i).getAlt());
        }
        imageUrls.trimToSize();
    }
    @Override
    public int getCount() {
        if(imageUrls.size() > 9){
            return 9;
        }else {
            return imageUrls.size();
        }
    }

    @Override
    public GroupTopicsBean.TopicsBean.PhotosBean getItem(int position) {
        return photoBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.my_grid_view_item,parent,false);
        }

        ImageView imageViewGridViewItem = (ImageView) convertView.findViewById(R.id.image_view_grid_view_item);
        Glide.with(context).load(imageUrls.get(position)).placeholder(R.drawable.bcg_image_view)
                .centerCrop().into(imageViewGridViewItem);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ImageDisplayActivity.class);
                intent.putExtra(ImageDisplayActivity.IMAGE_INDEX,position);
                intent.putExtra(ImageDisplayActivity.PHOTOBEAN_LIST,imageUrls);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
