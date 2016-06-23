package com.kai.blocksms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class HistoryAdapter extends ArrayAdapter<HistoryModel>{

    private class ItemView {

        TextView tvPhoneNumber;
        TextView tvCreateOn;
        TextView tvMessage;
    }

    public HistoryAdapter(Context context,List<HistoryModel> models) {
        super(context, R.layout.tv_history, models);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HistoryModel model = getItem(position);

        ItemView itemView = null;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tv_history,parent,false);

            itemView = new ItemView();

            itemView.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvPhoneNumber);
            itemView.tvCreateOn = (TextView) convertView.findViewById(R.id.tvCreateOn);
            itemView.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);

            convertView.setTag(itemView);
        } else {

            itemView = (ItemView) convertView.getTag();
        }

        itemView.tvPhoneNumber.setText(model.getNumber());
        itemView.tvCreateOn.setText(model.getCreateon());
        itemView.tvMessage.setText(model.getMessage());

        return convertView;
    }
}
