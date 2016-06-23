package com.kai.blocksms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    private Button btnInputMessage;
    private Button btnInputNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

        initEvent();
    }


    private void initEvent() {

        btnInputMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this,AddActivity.class);

                int btnId = v.getId();

                intent.putExtra("btnId",btnId);

                startActivity(intent);
            }
        });

        btnInputNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this,AddActivity.class);

                int btnId = v.getId();

                intent.putExtra("btnId",btnId);

                startActivity(intent);
            }
        });
    }


    private void initView() {

        btnInputMessage = (Button) findViewById(R.id.btnInputMessage);

        btnInputNumber = (Button) findViewById(R.id.btnInputNumber);
    }
}
