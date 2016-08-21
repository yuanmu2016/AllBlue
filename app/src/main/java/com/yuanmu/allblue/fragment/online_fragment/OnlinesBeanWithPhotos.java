package com.yuanmu.allblue.fragment.online_fragment;

import java.io.Serializable;

public class OnlinesBeanWithPhotos implements Serializable{

    private OnlineOnlinesBean.OnlinesBean onlinesBean;
    private PhotosPhotosBean photosPhotosBean;

    public OnlineOnlinesBean.OnlinesBean getOnlinesBean() {
        return onlinesBean;
    }

    public void setOnlinesBean(OnlineOnlinesBean.OnlinesBean onlinesBean) {
        this.onlinesBean = onlinesBean;
    }

    public PhotosPhotosBean getPhotosPhotosBean() {
        return photosPhotosBean;
    }

    public void setPhotosPhotosBean(PhotosPhotosBean photosPhotosBean) {
        this.photosPhotosBean = photosPhotosBean;
    }
}
