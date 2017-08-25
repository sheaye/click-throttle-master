package com.sheaye.sample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sheaye.sample.R;
import com.sheaye.throttle.ClickThrottle;

import throttle.BindClick;

/**
 * Created by yexinyan on 2017/5/26.
 */

public class ListAdapter extends BaseAdapter {

    private String[] stringList = {"1", "2", "3", "4", "5","6"};

    @Override
    public int getCount() {
        return stringList.length;
    }

    @Override
    public Object getItem(int position) {
        return stringList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mButton1.setText(stringList[position]+"button1");
        viewHolder.mButton2.setText(stringList[position]+"button2");
        return convertView;
    }

    static class ViewHolder{
        Button mButton1;
        Button mButton2;

        ViewHolder(View itemView) {
            ClickThrottle.bind(this,itemView);
            mButton1 = (Button) itemView.findViewById(R.id.item_button1);
            mButton2 = (Button) itemView.findViewById(R.id.item_button2);
        }

        @BindClick({R.id.item_button1, R.id.item_button2})
        public void onClick(View view){
            Toast.makeText(view.getContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
