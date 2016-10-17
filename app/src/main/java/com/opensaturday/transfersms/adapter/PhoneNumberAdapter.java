package com.opensaturday.transfersms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.opensaturday.transfersms.util.SmsTransferDb;

import java.util.ArrayList;

/**
 * Created by Kwontaejin on 2016. 10. 15..
 */

public class PhoneNumberAdapter extends BaseAdapter{

    private ArrayList mList = new ArrayList();
    private Context mContext;
    private int mLayoutResourceId;
    private LayoutInflater mInflater;
    private SmsTransferDb db;


    public PhoneNumberAdapter(Context context, int id, ArrayList list) {
        mContext = context;
        mList = list;
        mLayoutResourceId = id;
        mInflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new SmsTransferDb(mContext);
    }

    public void add(String str) {
        mList.add(str);
        db.insert(str);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
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
            rowView = mInflater.inflate(mLayoutResourceId, parent,false);
            holder = new PhoneHolder();
            holder.number = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(holder);
        }
        else{
            rowView= convertView;
            holder = (PhoneHolder)rowView.getTag();
        }

        holder.number.setText(mList.get(position).toString());
        return rowView;
    }

    private class PhoneHolder {
        TextView number;
    }

    public void removeItem(int i) {
        db.delete(mList.get(i).toString());
        mList.remove(i);
    }
}
