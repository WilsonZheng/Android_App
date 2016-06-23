package com.wilson.loginexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button button = (Button) findViewById(R.id.btnResult);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.etResult);
                String msg = editText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(MainActivity.RESULT_MSG,msg);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
