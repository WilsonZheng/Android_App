package com.kai.blocksms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AddActivity extends AppCompatActivity {

    private ListView lvBlockItem;
    private Button btnAdd;
    private int btnId;
    private SettingAdapter adapter;
    private String type;
    List<SettingModel> settingModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();
        initEvent();
    }

    private void initEvent() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                LayoutInflater inflater = AddActivity.this.getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_input_blockitem, null);

                builder.setView(dialogView);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText edInputBlockItem = (EditText) dialogView.findViewById(R.id.edInputBlockItem);
                        String strContent = edInputBlockItem.getText().toString();

                        if (TextUtils.isEmpty(strContent)) {

                            Toast.makeText(AddActivity.this,"Please type in", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SettingModel model = new SettingModel();
                        model.setType(type);
                        model.setContent(strContent);

                        BlockDB db = new BlockDB(AddActivity.this);
                        db.addSetting(model);

                        refreshList(type);
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        lvBlockItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder ab = new AlertDialog.Builder(AddActivity.this);
                ab.setTitle("Alert");
                ab.setMessage("Are you sure to delete？");
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SettingModel model = settingModelList.get(position);
                        BlockDB db = new BlockDB(AddActivity.this);

                        db.deleteSetting(model.getId());

                        settingModelList.remove(position);

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

        lvBlockItem = (ListView) findViewById(R.id.lvBlockItem);

        btnAdd = (Button) findViewById(R.id.btnAdd);

        Intent intent = getIntent();
        btnId = intent.getIntExtra("btnId",0);

        if (btnId == R.id.btnInputMessage) {
            type = getResources().getString(R.string.tag_message);
        } else {
            type = getResources().getString(R.string.tag_number);
        }

        refreshList(type);
    }

    private void refreshList(String type) {

        BlockDB db = new BlockDB(AddActivity.this);
        settingModelList = db.getAllSettingDatas(type);

        adapter = new SettingAdapter(this, settingModelList);
        lvBlockItem.setAdapter(adapter);
    }
}
