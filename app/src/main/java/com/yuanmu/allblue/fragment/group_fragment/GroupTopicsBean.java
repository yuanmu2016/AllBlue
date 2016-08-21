package com.yuanmu.allblue.fragment.group_fragment;

import java.io.Serializable;
import java.util.List;

public class GroupTopicsBean{

    private List<TopicsBean> topics;

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public static class TopicsBean implements Serializable{
        private String updated;
        private int like_count;
        private String id;
        private String title;
        private String share_url;
        private String content;
        private int comments_count;
        /**
         * size : {"width":500,"height":500}
         * alt : https://img1.doubanio.com/view/group_topic/large/public/p49232248.jpg
         * layout : C
         * topic_id : 87403208
         * seq_id : 1
         * author_id : 131755497
         * title :
         * id : 49232248
         * creation_date : 2016-06-14 03:57:48
         */

        private List<PhotosBean> photos;

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public List<PhotosBean> getPhotos() {
            return photos;
        }

        public void setPhotos(List<PhotosBean> photos) {
            this.photos = photos;
        }

        public static class PhotosBean implements Serializable{
            private String alt;
            private String seq_id;

            public String getSeq_id() {
                return seq_id;
            }

            public void setSeq_id(String seq_id) {
                this.seq_id = seq_id;
            }

            public String getAlt() {
                return alt;
            }

            public void setAlt(String alt) {
                this.alt = alt;
            }
        }
    }
}

