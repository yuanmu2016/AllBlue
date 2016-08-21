package com.yuanmu.allblue.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.yuanmu.allblue.R;


public class MapActivity extends AppCompatActivity {


    public static final String LATLNG = "com.yuanmu.allblue.activity.MapActivity.LATLNG";
    public static final String TITLE = "com.yuanmu.allblue.activity.MapActivity.TITLE";

    private MapView mapView;
    private AMap map;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        String title = getIntent().getStringExtra(TITLE);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation);



        String latlng = getIntent().getStringExtra(LATLNG);
        String[] lat_lng = latlng.split(" ");

        if (map == null) {
            map = mapView.getMap();
            LatLng latLng = new LatLng(Double.valueOf(lat_lng[0]),Double.valueOf(lat_lng[1]));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            map.addMarker(options);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public  void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
