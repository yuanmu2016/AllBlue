package com.yuanmu.allblue.fragment.activity_fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuanmu.allblue.activity.ActivityDetailActivity;
import com.yuanmu.allblue.R;

import java.util.ArrayList;
import java.util.List;

public class ChildActivityFragmentAdapter extends BaseAdapter {

    private List<ActivityEventBean.EventsBean> eventsBeanList;
    private Context context;

    public ChildActivityFragmentAdapter(Context context,List<ActivityEventBean.EventsBean> eventsBeanList){
        this.context = context;
        this.eventsBeanList = eventsBeanList;
    }
    @Override
    public int getCount() {
        return eventsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.child_fragment_item,parent,false);
            holder = new ItemHolder();
            holder.imageViewPoster = (ImageView) convertView.findViewById(R.id.image_poster);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.text_time);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.textViewPlace = (TextView) convertView.findViewById(R.id.text_place);
            holder.textViewPrice = (TextView) convertView.findViewById(R.id.text_price);
            holder.textViewPeople = (TextView) convertView.findViewById(R.id.text_people);
            convertView.setTag(holder);
        }
        holder = (ItemHolder) convertView.getTag();

        final ActivityEventBean.EventsBean eventsBean = eventsBeanList.get(position);
        Glide.with(context).load(eventsBean.getImage()).placeholder(R.drawable.bcg_image_view).centerCrop().into(holder.imageViewPoster);
        holder.textViewTitle.setText(eventsBean.getTitle());
        holder.textViewTime.setText(eventsBean.getBegin_time());
        holder.textViewPlace.setText(eventsBean.getAddress());
        holder.textViewPrice.setText(eventsBean.getPrice_range());
        holder.textViewPeople.setText(eventsBean.getParticipant_count()+"人参加  |  "+eventsBean.getWisher_count()+"人感兴趣");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ActivityDetailActivity.class);
                ArrayList<String> args = new ArrayList<String>();
                args.add(eventsBean.getTitle());
                args.add(eventsBean.getBegin_time());
                args.add(eventsBean.getAddress());
                args.add(eventsBean.getPrice_range());
                args.add(eventsBean.getContent());
                args.add(eventsBean.getImage_lmobile());
                args.add(eventsBean.getGeo());
                intent.putStringArrayListExtra(ActivityDetailActivity.ARGS,args);
                context.startActivity(intent);


            }
        });
        return convertView;
    }

    class  ItemHolder{
        ImageView imageViewPoster;
        TextView textViewTitle;
        TextView textViewTime;
        TextView textViewPlace;
        TextView textViewPrice;
        TextView textViewPeople;
    }
}
