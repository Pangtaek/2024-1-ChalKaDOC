package com.example.chalkadoc.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chalkadoc.R;

import java.util.ArrayList;

public class CustomListView extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ListData> listViewData;
    private int count;

    public CustomListView(ArrayList<ListData> listData, Context context) {
        this.listViewData = listData;
        this.count = listViewData.size();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return listViewData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.customlistview, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView body_1 = convertView.findViewById(R.id.body_1);
        TextView body_2 = convertView.findViewById(R.id.body_2);

        title.setText(listViewData.get(position).title);
        body_1.setText(listViewData.get(position).body_1);
        body_2.setText(listViewData.get(position).body_2);

        return convertView;
    }
}
