package com.chjan.tj3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Activity000 extends AppCompatActivity {

    Button butInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout000);

        butInfo = (Button)findViewById(R.id.butInfo);

    }
}
