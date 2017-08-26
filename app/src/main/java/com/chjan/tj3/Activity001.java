package com.chjan.tj3;
/* 取得GPS */
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.*;

public class Activity001 extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_EXTERNAL_STORAGE = 123;
    private boolean getService = false;		//是否已開啟定位服務
    TextView tvLocation,tvLog;
    private LocationManager lms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout001);

        tvLocation = (TextView)findViewById(R.id.tvLocation);
        tvLog = (TextView)findViewById(R.id.tvLog);

        //取得系統定位服務
        //LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        lms = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) || lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            getService = true; //確認開啟定位服務
            locationServiceInitial(lms);
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
        }
    }

    protected void onResume(){
        super.onResume();
        if(getService){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //未取得權限，向使用者要求允許權限
                Toast.makeText(this, "未取得 ACCESS_FINE_LOCATION 權限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                        REQUEST_EXTERNAL_STORAGE
                );
            } else {
                //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
                lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            }

        }
    }

    protected  void onPause(){
        super.onPause();
        if(getService){
            lms.removeUpdates(this);
        }
    }

    private String bestProvider = LocationManager.GPS_PROVIDER;	//最佳資訊提供者
    private void locationServiceInitial(LocationManager lm) {
        //是否取得權限,參考  https://litotom.com/2016/05/15/android-6-permission/
        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            //未取得權限，向使用者要求允許權限
            Toast.makeText(this, "未取得 ACCESS_FINE_LOCATION 權限", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[] {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            //已有權限，可進行檔案存取

            Criteria criteria = new Criteria();	//資訊提供者選取標準
            bestProvider = lm.getBestProvider(criteria, true);	//選擇精準度最高的提供者
            Location location = lm.getLastKnownLocation(bestProvider);    //使用GPS定位座標
            getLocation(location);
        }
        //-----------------------------------------------------------------------------------------------------------------------------------------------

    }

    StringBuffer sbLog = new StringBuffer("");

    private void getLocation(Location location) {	//將定位資訊顯示在畫面中
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy

        if(location != null) {
            Double longitude = location.getLongitude();	//取得經度
            Double latitude = location.getLatitude();	//取得緯度
            tvLocation.setText(String.format("目前位置 %f , %f , (%s)",longitude,latitude,bestProvider));
            sbLog.insert(0,String.format("%s - %f , %f \n",sdfDate.format(new Date()),longitude,latitude));
            tvLog.setText(sbLog.toString());
        }
        else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //當地點改變時
        getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //status=OUT_OF_SERVICE 供應商停止服務
        //status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
    }

    @Override
    public void onProviderEnabled(String provider) {
        //當GPS或網路定位功能開啟
    }

    @Override
    public void onProviderDisabled(String provider) {
        //當GPS或網路定位功能關閉時
    }
}