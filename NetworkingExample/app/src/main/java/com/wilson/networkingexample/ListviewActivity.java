package com.wilson.networkingexample;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wilson.networkingexample.model.ChildListResponseModel;
import com.wilson.networkingexample.model.ChildModel;
import com.wilson.networkingexample.model.ChildResponseModel;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListviewActivity extends AppCompatActivity {

    private ChildAdapter adapter;
    private ArrayList<ChildModel> childList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        new HttpGetTask().execute();
    }

    class ChildAdapter extends ArrayAdapter<ChildModel> {
        Context mContext;
        int layoutResourceId;
        ArrayList<ChildModel> childList = null;

        public ChildAdapter(Context mContext, int layoutResourceId, ArrayList<ChildModel> childList) {
            super(mContext, layoutResourceId, childList);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.childList = childList;
        }

        @Override
        public int getCount() {
            return childList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                //inflate the layout
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);

            }
            ChildModel model = childList.get(position);
            TextView tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
            tvChildId.setText(String.valueOf(model.getId()));

            TextView tvChildName = (TextView) convertView.findViewById(R.id.tvChildName);
            tvChildName.setText(String.valueOf(model.getName()));

            TextView tvCreatedBy = (TextView) convertView.findViewById(R.id.tvCreatedBy);
            tvCreatedBy.setText(model.getCreatedBy());

            return convertView;
        }
    }

    class HttpGetTask extends AsyncTask<Void, Void, String> {
        private final String HTTP_PATH = "Http://uat.aimy.co.nz/Mobile/GetChildList";

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
            ChildListResponseModel responseModel = gson.fromJson(s,ChildListResponseModel.class);
            childList = responseModel.getChildModel();
            adapter = new ChildAdapter(ListviewActivity.this,R.layout.list_item,childList);
            ListView listView = (ListView) findViewById(R.id.child_list);
            listView.setAdapter(adapter);


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
