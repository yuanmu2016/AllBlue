package com.yuanmu.allblue.fragment.online_fragment;

import java.io.Serializable;
import java.util.List;


public  class PhotosPhotosBean implements Serializable{



    private List<PhotosBean> photos;

    public List<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosBean> photos) {
        this.photos = photos;
    }

    public static class PhotosBean implements Serializable{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

