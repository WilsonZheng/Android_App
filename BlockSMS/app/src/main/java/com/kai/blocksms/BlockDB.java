package com.kai.blocksms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Contacts;
import android.support.v4.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BlockDB extends SQLiteOpenHelper {

    public BlockDB(Context context) {
        super(context, "BlockDB", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE setting(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT," +
                "content TEXT)");

        db.execSQL("CREATE TABLE history(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT," +
                        "message TEXT," +
                        "createon TEXT)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists setting");
        db.execSQL("drop table if exists history");

        onCreate(db);
    }

    public void addSetting(SettingModel model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query;
        query = "DELETE FROM setting WHERE type = '" + model.getType() + "' and content = '" + model.getContent() + "'";
        db.execSQL(query);

        values.put("type", model.getType());

        values.put("content", model.getContent());

        db.insert("setting", null, values);

        db.close();
    }

    public void deleteSetting(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("setting", "_id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public List<SettingModel> getAllSettingDatas(String type) {

        List<SettingModel> datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        query = "SELECT _id,type,content FROM setting WHERE type = '" + type + "' order by _id desc";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            SettingModel model = new SettingModel();

            model.setId(cursor.getInt(0));
            model.setType(cursor.getString(1));
            model.setContent(cursor.getString(2));

            datas.add(model);
        }

        cursor.close();
        db.close();

        return datas;
    }

    public void addHistory(HistoryModel model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("number", model.getNumber());

        values.put("message", model.getMessage());

        values.put("createon", model.getCreateon());

        db.insert("history", null, values);

        db.close();
    }

    public void deleteHistory(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("history", "_id=?", new String[]{String.valueOf(id)});

        db.close();
    }

    public List<HistoryModel> getAllHistoryDatas() {

        List<HistoryModel> datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query;
        query = "SELECT _id,number,message,createon FROM history order by createon desc";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {

            HistoryModel model = new HistoryModel();

            model.setId(cursor.getInt(0));
            model.setNumber(cursor.getString(1));
            model.setMessage(cursor.getString(2));
            model.setCreateon(cursor.getString(3));

            datas.add(model);
        }

        cursor.close();
        db.close();

        return datas;
    }

    public boolean isNumberBlocked(String type, String number) {

        String query = "select _id from setting where type = '" + type + "' and content = '" + number + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        boolean isBlocked = false;
        while (cursor.moveToNext()) {
            isBlocked = true;
            break;
        }

        cursor.close();
        db.close();

        return isBlocked;
    }

    public boolean isContentBlocked(String type, String content) {

        String query = "select content from setting where type = '" + type + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<String> contentList = new ArrayList<>();

        while (cursor.moveToNext()) {

            contentList.add(cursor.getString(0));
        }

        boolean isBlocked = false;

        for (int i = 0; i < contentList.size(); i++) {

            String value = contentList.get(i);

            if (content.contains(value)) {

                isBlocked = true;
                break;
            }
        }

        cursor.close();
        db.close();

        return isBlocked;
    }
}


