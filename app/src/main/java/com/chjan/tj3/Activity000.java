package com.chjan.tj3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity000 extends AppCompatActivity {

    Button butGPS;
    Button butPlayer;
    Button butInternet;
    Button butInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout000);

        butGPS = (Button)findViewById(R.id.butGPS);
        butPlayer = (Button)findViewById(R.id.butPlayer);
        butInternet = (Button)findViewById(R.id.butInternet);
        butInfo = (Button)findViewById(R.id.butInfo);

        butGPS.setOnClickListener(butClickListener);
        butPlayer.setOnClickListener(butClickListener);
        butInternet.setOnClickListener(butClickListener);
        butInfo.setOnClickListener(butClickListener);

    }

    private View.OnClickListener butClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            if(v == butGPS){
                Intent intent = new Intent();
                intent.setClass(Activity000.this,Activity001.class);
                startActivity(intent);
                //Activity000.this.finish();
            }
            if(v == butPlayer){
                Intent intent = new Intent();
                intent.setClass(Activity000.this,Activity002.class);
                startActivity(intent);
            }
            if(v == butInternet){
                Intent intent = new Intent();
                intent.setClass(Activity000.this,Activity003.class);
                startActivity(intent);
            }
            if(v == butInfo){
                Intent intent = new Intent();
                intent.setClass(Activity000.this,Activity011.class);
                startActivity(intent);
            }
        }
    };
}
