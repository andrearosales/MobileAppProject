package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea Rosales on 15/06/2015.
 */
public class ReceiverAdapter extends BaseAdapter{

    public List<Student> selected;
    private LayoutInflater inflater;
    private Activity activity;
    private List<Student> listReceivers;


    public ReceiverAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listReceivers = list;
        this.selected = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return listReceivers.size();
    }

    @Override
    public Object getItem(int position) {

        return listReceivers.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vholder;
        View v = convertView;

        if (listReceivers.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.row_popup, parent, false);
                vholder = new ViewHolder();
                vholder.textTo = (TextView) v.findViewById(R.id.textStudent);
                vholder.check = (CheckBox) v.findViewById(R.id.checkbox);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }
            //vholder.textTo.setText(listReceivers.get(position));
            if(listReceivers.get(position).getName()!=null && !listReceivers.get(position).getName().equals("") &&  listReceivers.get(position).getSurname()!=null && !listReceivers.get(position).getSurname().equals(""))
            vholder.textTo.setText(listReceivers.get(position).getName()+" "+listReceivers.get(position).getSurname());



            vholder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!selected.contains(listReceivers.get(position))) {
                            selected.add(listReceivers.get(position));
                        }
                    } else {
                        if (selected.contains(listReceivers.get(position))) {
                            selected.remove(listReceivers.get(position));
                        }
                    }
                }
            });

        }
        return v;
    }


    public static class ViewHolder {
        public TextView textTo;
        public CheckBox check;
    }
}