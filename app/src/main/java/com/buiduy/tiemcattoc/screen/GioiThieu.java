package com.buiduy.tiemcattoc.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.buiduy.tiemcattoc.R;

public class GioiThieu extends AppCompatActivity {

    Button btn_donggioithieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu);
        // Anh xa
        btn_donggioithieu = findViewById(R.id.btn_donggioithieu);
        // Bat su kien
        btn_donggioithieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}