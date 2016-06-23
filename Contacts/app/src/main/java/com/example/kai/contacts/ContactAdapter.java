package com.example.kai.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<ContactItem>{

    private static class ViewHolder{
        TextView name;
        TextView phoneNumber;
    }

    public ContactAdapter(Context context, ArrayList<ContactItem> contactItems){
        super(context,R.layout.contact_item,contactItems);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContactItem contactItem = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null){

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item,parent,false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.phoneNumber = (TextView) convertView.findViewById(R.id.tvPhone);
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(contactItem.getName());
        viewHolder.phoneNumber.setText(contactItem.getPhoneNumer());

        return convertView;
    }
}
