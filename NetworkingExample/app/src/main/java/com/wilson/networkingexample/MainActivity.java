package com.wilson.networkingexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.networkingexample.model.ChildResponseModel;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //private TextView mTextView;//this is for the first networking example
    private Button mButton;


    private TextView mTvChildId;
    private TextView mTvChildName;
    private TextView mTvCreatedBy;
    private TextView mTvErrorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTvChildId = (TextView) findViewById(R.id.tvChildId);
        mTvChildName = (TextView) findViewById(R.id.tvChildName);
        mTvCreatedBy = (TextView) findViewById(R.id.tvCreatedBy);
        mTvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);


        // mTextView = (TextView) findViewById(R.id.ivShowData);
        mButton = (Button) findViewById(R.id.btnShowData);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGetTask().execute();
            }
        });
    }

    class HttpGetTask extends AsyncTask<Void, Void, String> {
        private final String HTTP_PATH = "Http://uat.aimy.co.nz/Mobile/GetChildById?id=15";

        @Override
        protected String doInBackground(Void... params) {
            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(HTTP_PATH).openConnection();
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                data = readStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            } finally {
                if (null != httpURLConnection) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

        //        @Override
//        protected void onPostExecute(String s) {
//            mTextView.setText(s);
//        }
        @Override
        protected void onPostExecute(String s) {
            Gson gson = new GsonBuilder().create();
            ChildResponseModel responseModel = gson.fromJson(s, ChildResponseModel.class);
            if (responseModel.isSuccess()) {
                mTvChildId.setText(String.valueOf(responseModel.getChildModel().getId()));
                mTvChildName.setText(responseModel.getChildModel().getName());
                mTvCreatedBy.setText(responseModel.getChildModel().getCreatedBy());
                mTvErrorMsg.setVisibility(TextView.GONE);//using TextView.Gone will make it disappeared and give out the space in css, but if use invisible it will still take the space.
            } else {
                mTvErrorMsg.setText(responseModel.getErrorMessaage());
                mTvChildId.setVisibility(TextView.GONE);
                mTvChildName.setVisibility(TextView.GONE);

                mTvCreatedBy.setVisibility(TextView.GONE);
            }
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }

    }


}
