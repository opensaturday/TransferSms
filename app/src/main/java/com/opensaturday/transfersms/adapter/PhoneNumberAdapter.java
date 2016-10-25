package com.opensaturday.transfersms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.opensaturday.transfersms.database.SmsTransferDb;

import java.util.ArrayList;

/**
 * Created by Kwontaejin on 2016. 10. 15..
 */

public class PhoneNumberAdapter extends BaseAdapter{

    private ArrayList list = new ArrayList();
    private Context context;
    private int layoutResourceId;
    private LayoutInflater inflater;
    private SmsTransferDb db;


    public PhoneNumberAdapter(Context context, int id, ArrayList list) {
        this.context = context;
        this.list = list;
        this.layoutResourceId = id;
        this.inflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = new SmsTransferDb(context);
    }

    public void add(String str) {
        list.add(str);
        db.insert(str);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhoneHolder holder;
        View rowView;
        if(convertView==null){
            rowView = inflater.inflate(layoutResourceId, parent,false);
            holder = new PhoneHolder();
            holder.number = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(holder);
        }
        else{
            rowView= convertView;
            holder = (PhoneHolder)rowView.getTag();
        }

        holder.number.setText(list.get(position).toString());
        return rowView;
    }

    private class PhoneHolder {
        TextView number;
    }

    public void removeItem(int i) {
        db.delete(list.get(i).toString());
        list.remove(i);
    }
}
