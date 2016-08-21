package com.yuanmu.allblue.fragment.activity_fragment;

import java.io.Serializable;
import java.util.List;

public  class ActivityEventBean {




    private List<EventsBean> events;

    public List<EventsBean> getEvents() {
        return events;
    }

    public void setEvents(List<EventsBean> events) {
        this.events = events;
    }

    public static class EventsBean implements Serializable{
        private String image;
        private String image_lmobile;
        private String title;
        private int wisher_count;
        private String content;
        private int participant_count;
        private String begin_time;
        private String price_range;
        private String geo;
        private String end_time;
        private String address;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage_lmobile() {
            return image_lmobile;
        }

        public void setImage_lmobile(String image_lmobile) {
            this.image_lmobile = image_lmobile;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getWisher_count() {
            return wisher_count;
        }

        public void setWisher_count(int wisher_count) {
            this.wisher_count = wisher_count;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getPrice_range() {
            return price_range;
        }

        public void setPrice_range(String price_range) {
            this.price_range = price_range;
        }

        public String getGeo() {
            return geo;
        }

        public void setGeo(String geo) {
            this.geo = geo;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

