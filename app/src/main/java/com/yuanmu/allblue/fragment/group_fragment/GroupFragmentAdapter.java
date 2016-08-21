package com.yuanmu.allblue.fragment.group_fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanmu.allblue.R;
import com.yuanmu.allblue.activity.GroupDetailActivity;
import com.yuanmu.allblue.view.EmbedGridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class GroupFragmentAdapter extends BaseAdapter {
    private Activity context;
    private List<GroupTopicsBean.TopicsBean> topicsBeanList;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    
    public GroupFragmentAdapter(Activity context, List<GroupTopicsBean.TopicsBean> topicsBeanList){
        this.context  = context;
        this.topicsBeanList = topicsBeanList;
        Calendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

    }
    @Override
    public int getCount() {
        return topicsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_group_item,parent,false);
            holder.textViewTitle = (TextView)convertView.findViewById(R.id.text_view_title);
            holder.textViewContent = (TextView) convertView.findViewById(R.id.text_view_content);
            holder.myGridView = (EmbedGridView) convertView.findViewById(R.id.embed_grid_view);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.text_view_date);
            holder.textViewLikeCount = (TextView) convertView.findViewById(R.id.text_view_like_count);
            holder.textViewCommentCount = (TextView) convertView.findViewById(R.id.text_view_comment_count);
            holder.textViewPhotoCount = (TextView) convertView.findViewById(R.id.text_view_photo_count);
            holder.imageViewLikeCount = (ImageView) convertView.findViewById(R.id.image_view_like_count);
            holder.imageViewCommentCount = (ImageView) convertView.findViewById(R.id.image_view_comment_count);
            holder.imageViewPhotoCount = (ImageView) convertView.findViewById(R.id.image_view_photo_count);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }


        final GroupTopicsBean.TopicsBean topicsBean = topicsBeanList.get(position);
        List<GroupTopicsBean.TopicsBean.PhotosBean> photoBeanList = topicsBean.getPhotos();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.TITLE,topicsBean.getTitle());
                intent.putExtra(GroupDetailActivity.CONTENT,topicsBean.getContent());

                ArrayList<String> seq_ids = new ArrayList<>();
                ArrayList<String> alts = new ArrayList<>();
                for(GroupTopicsBean.TopicsBean.PhotosBean photosBean : topicsBean.getPhotos()){
                    seq_ids.add(photosBean.getSeq_id());
                    alts.add(photosBean.getAlt());
                }
                intent.putStringArrayListExtra(GroupDetailActivity.SEQ_ID,seq_ids);
                intent.putStringArrayListExtra(GroupDetailActivity.ALT,alts);
                context.startActivity(intent);
            }
        });




        String dateString = topicsBean.getUpdated();
        int year = Integer.valueOf(dateString.substring(0,4));
        int month = Integer.valueOf(dateString.substring(5,7));
        int day = Integer.valueOf(dateString.substring(8,10));
        int hour = Integer.valueOf(dateString.substring(11,13));
        int minute = Integer.valueOf(dateString.substring(14,16));
        int second = Integer.valueOf(dateString.substring(17,19));


        if(this.year > year){
            holder.textViewDate.setText((this.year - year)+"年前");
        }else if(this.month + 1 > month){
            holder.textViewDate.setText((this.month + 1 - month)+"个月前");
        }else if(this.day > day){
            holder.textViewDate.setText((this.day - day)+"天前");
        }else if(this.hour > hour){
             holder.textViewDate.setText((this.hour - hour)+"个小时前");
        }else if(this.minute > minute){
             holder.textViewDate.setText((this.minute - minute)+"分钟前");
        }else if(this.second - second > 10){
             holder.textViewDate.setText((this.second - second)+"秒前");
        }else {
             holder.textViewDate.setText("刚刚");
        }

        int likeCount = topicsBean.getLike_count();
        if(likeCount < 10){
             holder.imageViewLikeCount.setImageResource(R.drawable.drawable_like_count_less_10);
        }else if(likeCount < 30){
             holder.imageViewLikeCount.setImageResource(R.drawable.drawable_like_count_less_30);
        }else if(likeCount < 70){
             holder.imageViewLikeCount.setImageResource(R.drawable.drawable_like_count_less_70);
        }else if(likeCount < 100){
             holder.imageViewLikeCount.setImageResource(R.drawable.drawable_like_count_less_100);
        }else {
             holder.imageViewLikeCount.setImageResource(R.drawable.drawable_like_count_more_100);
        }
        holder.textViewLikeCount.setText(String.valueOf(likeCount));

        int commentCount = topicsBean.getComments_count();
        if(commentCount < 10){
             holder.imageViewCommentCount.setImageResource(R.drawable.drawable_comment_count_less_10);
        }else if(commentCount < 30){
             holder.imageViewCommentCount.setImageResource(R.drawable.drawable_comment_count_less_30);
        }else if(commentCount < 70){
             holder.imageViewCommentCount.setImageResource(R.drawable.drawable_comment_count_less_70);
        }else if(commentCount < 100){
             holder.imageViewCommentCount.setImageResource(R.drawable.drawable_comment_count_less_100);
        }else {
             holder.imageViewCommentCount.setImageResource(R.drawable.drawable_comment_count_more_100);
        }
        holder.textViewCommentCount.setText(commentCount+"");

        String rawTitle = topicsBean.getTitle();
        holder.textViewTitle.setText(rawTitle);

        String rawContent = topicsBean.getContent().trim();
      //  StringBuilder sb = new StringBuilder();
      //  sb.ensureCapacity(rawContent.length());

        char[] chars = new char[rawContent.length()];
        int k = 0;

        for(int  i = 0; i < rawContent.length(); ){
            if(rawContent.charAt(i) == '<' && rawContent.charAt(i+1)=='图' && rawContent.charAt(i+2)=='片'){
                if(Character.isDigit(rawContent.charAt(i+3)) && Character.isDigit(rawContent.charAt(i+4))){
                    i = i + 6;
                    continue;
                }
                if(Character.isDigit(rawContent.charAt(i+3)) && !Character.isDigit(rawContent.charAt(i+4))){
                    i = i + 5;
                    continue;
                }

            }
            chars[k++] = rawContent.charAt(i);
            i++;
        }

        holder.textViewContent.setText(new String(chars));

        int photoCount = photoBeanList.size();
        if(photoCount == 0){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_0);
        }else if(photoCount == 1){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_1);
        }else if(photoCount == 3){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_3);
        }else if(photoCount == 4){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_4);
        }else if(photoCount == 5){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_5);
        }else if(photoCount == 6){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_6);
        }else if(photoCount < 10){
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_less_10);
        }else {
             holder.imageViewPhotoCount.setImageResource(R.drawable.drawable_photo_count_more_10);
        }

        holder.textViewPhotoCount.setText(photoCount+"");
       
        holder.myGridView.setAdapter(new EmbedGridViewAdapter(context,photoBeanList));

        return convertView;
    }
    
    
    class Holder{
          TextView textViewTitle;
          TextView textViewContent;
          EmbedGridView myGridView;
          TextView textViewDate;
          TextView  textViewLikeCount;
          TextView textViewCommentCount;
          TextView textViewPhotoCount;
          ImageView imageViewLikeCount;
          ImageView imageViewCommentCount;
          ImageView imageViewPhotoCount;
    }
}
