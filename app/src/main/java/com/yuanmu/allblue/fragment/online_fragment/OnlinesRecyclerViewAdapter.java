package com.yuanmu.allblue.fragment.online_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.activity.ImageDisplayActivity;
import com.yuanmu.allblue.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OnlinesRecyclerViewAdapter extends RecyclerView.Adapter<MyHolder> {

    private Context context;
    private List<PhotosPhotosBean.PhotosBean> photosBeanList;
    private List<String> args;

    MyHolder.ChildHolder childHolder;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG = 1;

    private ArrayList<String> imageUrls = new ArrayList<>();

    public OnlinesRecyclerViewAdapter(Context context, List<PhotosPhotosBean.PhotosBean> photosBeanList,List<String> args) {
        this.context = context;
        this.photosBeanList = photosBeanList;
        this.args = args;

        for(PhotosPhotosBean.PhotosBean photosBean:photosBeanList){
            imageUrls.add(photosBean.getImage());
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType == TYPE_IMG){
            view =  inflater.inflate(R.layout.my_recyclerview_adapter_item, parent, false);
        }else {
            view = inflater.inflate(R.layout.item_header, parent, false);
        }
        return new MyHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        if(position == 0){
           childHolder = holder.childHolder;
            childHolder.textViewTitle.setText(args.get(0));
            childHolder.textViewPeople.setText(args.get(1));
            childHolder.textViewImages.setText(args.get(2));
            long leftDay = (stringToDate(args.get(4)).getTime() - stringToDate(args.get(3)).getTime())/(24*60*60*1000);
            childHolder.textViewTime.setText(leftDay+"");
            childHolder.textViewContent.setText(args.get(5));
        }else {
            Glide.with(context).load(photosBeanList.get(position-1).getImage()).placeholder(R.drawable.bcg_image_view)
                    .centerCrop().into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ImageDisplayActivity.class);
                    intent.putExtra(ImageDisplayActivity.IMAGE_INDEX,position - 1);
                    intent.putExtra(ImageDisplayActivity.PHOTOBEAN_LIST,imageUrls);
                    context.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return photosBeanList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return TYPE_TEXT;
        else return TYPE_IMG;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final GridLayoutManager layoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                return position == 0 ? layoutManager.getSpanCount() : 1;
            }
        });

    }


    private Date stringToDate(String timeString){
        int year = Integer.valueOf(timeString.substring(0,4));
        int month = Integer.valueOf(timeString.substring(5,7));
        int day = Integer.valueOf(timeString.substring(8,10));
        int hour = Integer.valueOf(timeString.substring(11,13));
        int minute = Integer.valueOf(timeString.substring(14,16));
        return new Date(year,month,day,hour,minute);
    }

}

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ChildHolder childHolder;
        public MyHolder(View view,int viewType){
            super(view);
            if(viewType == OnlinesRecyclerViewAdapter.TYPE_IMG){
                imageView = (ImageView) view.findViewById(R.id.image);
            }else {
                childHolder = new ChildHolder(view);
            }

        }

        class ChildHolder{
            TextView textViewTitle;
            TextView textViewPeople;
            TextView textViewImages;
            TextView textViewTime;
            TextView textViewContent;
            public ChildHolder(View view){
                textViewTitle  = (TextView) view.findViewById(R.id.text_title);
                textViewPeople = (TextView) view.findViewById(R.id.text_people);
                textViewImages = (TextView) view.findViewById(R.id.image_count);
                textViewTime = (TextView) view.findViewById(R.id.text_time);
                textViewContent = (TextView) view.findViewById(R.id.text_content);
            }
        }



    }


