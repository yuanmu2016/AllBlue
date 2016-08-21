package com.yuanmu.allblue.fragment.online_fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuanmu.allblue.R;

import java.util.ArrayList;
import java.util.List;


public class OnlinesFragmentAdapter extends BaseAdapter {

    private Context context;
    private List<OnlinesBeanWithPhotos> onlinesBeanWithPhotosList;

    public OnlinesFragmentAdapter(Context context, List<OnlinesBeanWithPhotos> onlinesBeanWithPhotosList){
        this.context = context ;
        this.onlinesBeanWithPhotosList = onlinesBeanWithPhotosList;
    }
    @Override
    public int getCount() {
        return onlinesBeanWithPhotosList.size();
    }

    @Override
    public Object getItem(int position) {
        return onlinesBeanWithPhotosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_onlines_item,parent,false);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.textViewParticipantCount = (TextView) convertView.findViewById(R.id.text_participant_count);
            holder.textViewPhotosCount = (TextView) convertView.findViewById(R.id.text_img_count);
            holder.textViewDesc = (TextView) convertView.findViewById(R.id.text_desc);
            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerview);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            convertView.setTag(holder);
        }


        holder = (Holder) convertView.getTag();
        final OnlineOnlinesBean.OnlinesBean onlineBean = onlinesBeanWithPhotosList.get(position).getOnlinesBean();
        holder.textViewTitle.setText(onlineBean.getTitle());
        holder.textViewParticipantCount.setText(onlineBean.getParticipant_count()+"");
        holder.textViewPhotosCount.setText(onlineBean.getPhoto_count()+"");
        holder.textViewDesc.setText(onlineBean.getDesc());
        holder.recyclerView.setAdapter(new MyRecyclerViewAdapter(context,onlinesBeanWithPhotosList.get(position).getPhotosPhotosBean().getPhotos()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> args = new ArrayList<String>();
                args.add(onlineBean.getTitle());
                args.add(onlineBean.getParticipant_count()+"");
                args.add(onlineBean.getPhoto_count()+"");
                args.add(onlineBean.getBegin_time());
                args.add(onlineBean.getEnd_time());
                args.add(onlineBean.getDesc());
                args.add(onlineBean.getImage());
                args.add(onlineBean.getAlbum_id());
                Intent intent = new Intent(context,OnlinesDetailActivity.class);
                intent.putStringArrayListExtra(OnlinesDetailActivity.ARGS,args);
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}

class Holder{
    TextView textViewTitle;
    TextView textViewParticipantCount;
    TextView textViewPhotosCount;
    TextView textViewDesc;
    RecyclerView recyclerView;
}


