package com.kai.blocksms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvBlockSMS;
    private Button btnSettings;
    List<HistoryModel> historyModelList;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(MainActivity.this,SMSRecevierService.class);
        startService(i);

        initView();
        initEvent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        refreshList();
    }

    private void initEvent() {

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        lvBlockSMS.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);

                ab.setTitle("Alert");
                ab.setMessage("Are you sure to delete?");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HistoryModel model = historyModelList.get(position);

                        BlockDB db = new BlockDB(MainActivity.this);

                        db.deleteHistory(model.getId());

                        historyModelList.remove(position);

                        adapter.notifyDataSetChanged();
                    }
                });

                ab.setNegativeButton("Cancel", null);
                ab.show();

                return true;
            }
        });
    }

    private void initView() {

        lvBlockSMS = (ListView) findViewById(R.id.lvBlockSMS);

        btnSettings = (Button) findViewById(R.id.btnSettings);

        refreshList();
    }

    private void refreshList() {

        BlockDB db = new BlockDB(MainActivity.this);
        historyModelList = db.getAllHistoryDatas();

        adapter = new HistoryAdapter(this, historyModelList);
        lvBlockSMS.setAdapter(adapter);
    }
}
