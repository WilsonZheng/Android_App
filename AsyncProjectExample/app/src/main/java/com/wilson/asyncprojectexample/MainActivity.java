package com.wilson.asyncprojectexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mBtnLoadImage;
    private Button mBtnClickMe;
    private ProgressBar mProgressBar;
    private final int SEC_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mBtnLoadImage = (Button) findViewById(R.id.btnShowImage);
        mBtnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImageTask().execute(R.drawable.egimage);
            }
        });

        mBtnClickMe = (Button) findViewById(R.id.btnClickMe);
        mBtnClickMe.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               Toast.makeText(MainActivity.this,"Click Me",Toast.LENGTH_SHORT).show();
           }
        });

    }
    class LoadImageTask extends AsyncTask<Integer,Integer,Bitmap>{
        @Override
        protected void onPreExecute(){
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
        @Override
        protected Bitmap doInBackground(Integer... params){

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),params[0]);
            for (int i = 1;i<11;i++){//sleep
                try{
                    Thread.sleep(SEC_DELAY);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress(i*10);
            }
            return bitmap;
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            mProgressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            mImageView.setImageBitmap(bitmap);
        }
    }
}
