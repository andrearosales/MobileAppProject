package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardogarcia on 11/06/15.
 */
public class NoticeAdapter extends BaseAdapter {

    public static final String NOTICE = "com.example.arosales.mobileappproject.NOTICE";

    private LayoutInflater inflater;
    private Activity activity;
    private List<Notice> listNotices;
    private BaseAdapter adapter;


    public NoticeAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listNotices = list;
        this.adapter=this;
    }


    @Override
    public int getCount() {
        return listNotices.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vholder;
        View v = convertView;

        if (listNotices.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.notice_row, parent, false);
                vholder = new ViewHolder();
                vholder.textTitle = (TextView) v.findViewById(R.id.textTitle);
                vholder.textCategory = (TextView) v.findViewById(R.id.textCategory);
                vholder.textDescription = (TextView) v.findViewById(R.id.textDescription);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }

            vholder.textTitle.setText(listNotices.get(position).getTitle());
            vholder.textCategory.setText(listNotices.get(position).getCategory());
            vholder.textDescription.setText(listNotices.get(position).getDescription());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ViewNotice.class);
                    intent.putExtra(NOTICE, listNotices.get(position).getId());
                    activity.startActivity(intent);
                }
            });

        }

        return v;
    }

    public static class ViewHolder {
        public TextView textTitle;
        public TextView textCategory;
        public TextView textDescription;
    }

    public void modifySearch(String category){
        ArrayList<Notice> result_notices=new ArrayList<Notice>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notice");
        query.include("StudentId");
        if(!category.equals("-"))
            query.whereEqualTo("Category", category);
        try {
            List<ParseObject> results=query.find();
            for(ParseObject p:results){
                Notice notice = new Notice();
                notice.setId(p.getObjectId());
                notice.setTitle(p.getString("Title"));
                notice.setCategory(p.getString("Category"));
                notice.setDescription(p.getString("Description"));

                ParseObject student = p.getParseObject("StudentId");
                Student publisher = new Student();
                publisher.setId(student.getObjectId());
                if(student.getString("PhoneNumber")!=null)
                    publisher.setPhonenumber(student.getString("PhoneNumber"));
                if(student.getString("Email")!=null)
                    publisher.setEmail(student.getString("Email"));

                notice.setPublisher(publisher);

                if(p.getParseFile("Photo")!=null)
                    notice.setPhoto(p.getParseFile("Photo"));

                result_notices.add(notice);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        listNotices = result_notices;
        adapter.notifyDataSetChanged();
    }
}