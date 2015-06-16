package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardogarcia on 12/06/15.
 */
public class NotificationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Notification> listNotifications;

    public NotificationAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listNotifications = list;
    }

    @Override
    public int getCount() {
        return listNotifications.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ParseUser currentUser = ParseUser.getCurrentUser();
        String typeUser = currentUser.getString("TypeUser");

        ViewHolder vholder;
        View v = convertView;

        if (listNotifications.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.notification_row, parent, false);
                vholder = new ViewHolder();
                vholder.textCourse = (TextView) v.findViewById(R.id.textCourse);
                vholder.textTitle = (TextView) v.findViewById(R.id.textTitle);
                vholder.textMessage = (TextView) v.findViewById(R.id.textMessage);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }

            vholder.textCourse.setText(listNotifications.get(position).getCourse().getName());
            if (listNotifications.get(position).getTitle() != null && !listNotifications.get(position).getTitle().equals(""))
                vholder.textTitle.setText(listNotifications.get(position).getTitle());
            else
                vholder.textTitle.setText("");

            if (listNotifications.get(position).getMessage() != null && !listNotifications.get(position).getMessage().equals(""))
                vholder.textMessage.setText(listNotifications.get(position).getMessage());
            else
                vholder.textMessage.setText("");
        }

        return v;
    }


    public static class ViewHolder {
        public TextView textCourse;
        public TextView textTitle;
        public TextView textMessage;
    }

}
