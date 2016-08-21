package com.yuanmu.allblue.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheExecutive<T extends Serializable> {
    private  int cacheIndex = 0;
    private DiskLruCache diskLruCache;
    private int maxBeanListSize;
    private SharedPreferences sharedPreferences;
    private Activity activity;
    private String cacheDirName;

    private String preferencesKey;


    public CacheExecutive(Activity activity,String cacheDirName,String preferencesKey){
        this.cacheDirName = cacheDirName;
        this.preferencesKey = preferencesKey;
        this.activity = activity;
        sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void deleteCache(){
        if(diskLruCache != null){
            try {
                diskLruCache.delete();
                maxBeanListSize = 0;
                sharedPreferences.edit().putInt(preferencesKey,maxBeanListSize).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void closeCache(){
        if(diskLruCache != null  && !diskLruCache.isClosed()){
            try {
                diskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openCache(){

        diskLruCache = CacheUtil.openDiskLruCache(activity,cacheDirName);
    }

    public void flushCache(){
        if(diskLruCache != null  && !diskLruCache.isClosed()){
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }









    public   List<T> readBeanList() {
        List<T> beanList = new ArrayList<>();
        maxBeanListSize = activity.getPreferences(Context.MODE_PRIVATE).getInt(preferencesKey,100);
        if (diskLruCache != null) {
            for (int i = 0; i < maxBeanListSize && beanList.size() < Constants.COUNT_PER_DATA; i++) {
                try {
                    DiskLruCache.Snapshot snapShot = diskLruCache.get(cacheIndex + "");
                    if (snapShot != null) {
                        InputStream in = snapShot.getInputStream(0);
                        ObjectInputStream oin = new ObjectInputStream(in);
                        T bean = (T) oin.readObject();
                        beanList.add(bean);
                    }
                    cacheIndex++;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return beanList;
    }

    public  void writeBeanList(List<T> beanList, int writeStartIndex){
        OutputStream out;
        ObjectOutputStream oout;
        DiskLruCache.Editor diskEditor;
        String key;
        if(diskLruCache != null){
            for(int i = writeStartIndex; i < beanList.size() + writeStartIndex; i++){
                key = i + "";
                try {
                    diskEditor = diskLruCache.edit(key);
                    out = diskEditor.newOutputStream(0);
                    oout = new ObjectOutputStream(out);
                    oout.writeObject(beanList.get(i - writeStartIndex));
                    diskEditor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            maxBeanListSize += beanList.size();
            sharedPreferences.edit().putInt(preferencesKey,maxBeanListSize).commit();
        }
    }
}
