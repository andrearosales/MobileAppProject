package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by aRosales on 28/05/2015.
 */
public class ApplicationAdapter extends BaseAdapter implements View.OnClickListener{

    public static final String APPLICATION = "com.example.arosales.mobileappproject.APPLICATION";
    private LayoutInflater inflater;
    private Activity activity;
    private List<Job> listApplications;

    public ApplicationAdapter(Activity activity, List<Job> listApplications) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listApplications = listApplications;
    }

    @Override
    public int getCount() {
        return listApplications.size();
    }

    @Override
    public Object getItem(int position) {
        return listApplications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        View v = convertView;

        if (listApplications.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.application_row, parent, false);
                vholder = new ViewHolder();
                vholder.textPosition = (TextView) v.findViewById(R.id.textPosition);
                vholder.textCompany = (TextView) v.findViewById(R.id.textCompany);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }
            vholder.textPosition.setText(listApplications.get(position).getPosition());

            if (listApplications.get(position).getCompany().getName().length() > 78)
                vholder.textCompany.setText(listApplications.get(position).getCompany().getName().substring(0, 78) + "...");
            else
                vholder.textCompany.setText(listApplications.get(position).getCompany().getName());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(activity,ApplicationDescription.class);
                    intent.putExtra(APPLICATION, listApplications.get(position));
                    activity.startActivity(intent);
                }
            });
        }
        return v;
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder {
        public TextView textPosition;
        public TextView textCompany;
    }

}
