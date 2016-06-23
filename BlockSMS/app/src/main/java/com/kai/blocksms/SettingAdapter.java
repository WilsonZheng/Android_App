package com.kai.blocksms;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class SettingAdapter extends ArrayAdapter<SettingModel>{

    private class ItemView {

        TextView tvInputBlockItem;
    }

    public SettingAdapter(Context context, List<SettingModel> models) {
        super(context, R.layout.tv_setting, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        SettingModel model = getItem(position);

        ItemView itemView = null;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tv_setting,parent,false);

            itemView = new ItemView();

            itemView.tvInputBlockItem = (TextView) convertView.findViewById(R.id.tvInputBlockItem);

            convertView.setTag(itemView);
        } else {

            itemView = (ItemView) convertView.getTag();
        }

        itemView.tvInputBlockItem.setText(model.getContent());

        return convertView;
    }
}
