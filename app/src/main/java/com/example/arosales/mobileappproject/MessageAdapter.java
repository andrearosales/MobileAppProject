package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aRosales on 28/05/2015.
 */
public class MessageAdapter extends BaseAdapter implements View.OnClickListener {


    private LayoutInflater inflater;
    private Activity activity;
    private List<Message> listmessages;


    public MessageAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listmessages = list;
    }


    @Override
    public int getCount() {
        return listmessages.size();
    }

    @Override
    public Object getItem(int position) {

        return listmessages.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vholder;
        View v = convertView;

        if (listmessages.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.message_preview, parent, false);
                vholder = new ViewHolder();
                vholder.textSubject = (TextView) v.findViewById(R.id.textSubject);
                vholder.textMessage = (TextView) v.findViewById(R.id.textMessagePreview);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }
            if (listmessages.get(position).getSubject() != null && !listmessages.get(position).getSubject().equals(""))
                vholder.textSubject.setText(listmessages.get(position).getSubject());
            else
                vholder.textSubject.setText("");

            if (listmessages.get(position).getMessage() != null && !listmessages.get(position).getMessage().equals("")) {
                if (listmessages.get(position).getMessage().length() > 78)
                    vholder.textMessage.setText(listmessages.get(position).getMessage().substring(0, 78) + "...");
                else
                    vholder.textMessage.setText(listmessages.get(position).getMessage());
            }
            else
                vholder.textMessage.setText("");

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, InboxDescription.class);
                    Bundle b = new Bundle();
                    b.putString(Inbox.INFO_FROM, listmessages.get(position).getFrom().getObjectId());
                    b.putString(Inbox.INFO_FROM_TYPE, listmessages.get(position).getFrom().getString("TypeUser"));
                    b.putString(Inbox.INFO_MESSAGE, listmessages.get(position).getMessage());
                    b.putString(Inbox.INFO_SUBJECT, listmessages.get(position).getSubject());
                    intent.putExtras(b);
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
        public TextView textSubject;
        public TextView textMessage;
    }
}

