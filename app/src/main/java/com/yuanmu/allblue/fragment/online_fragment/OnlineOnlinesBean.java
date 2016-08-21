package com.yuanmu.allblue.fragment.online_fragment;

import java.io.Serializable;
import java.util.List;

public class OnlineOnlinesBean implements Serializable{



    private List<OnlinesBean> onlines;

    public List<OnlinesBean> getOnlines() {
        return onlines;
    }

    public void setOnlines(List<OnlinesBean> onlines) {
        this.onlines = onlines;
    }

    public static class OnlinesBean implements Serializable{
        private String album_id;
        private String image;
        private String title;
        private String desc;
        private int photo_count;
        private int participant_count;
        private String begin_time;
        private String end_time;

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getPhoto_count() {
            return photo_count;
        }

        public void setPhoto_count(int photo_count) {
            this.photo_count = photo_count;
        }

        public int getParticipant_count() {
            return participant_count;
        }

        public void setParticipant_count(int participant_count) {
            this.participant_count = participant_count;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }
    }
}
