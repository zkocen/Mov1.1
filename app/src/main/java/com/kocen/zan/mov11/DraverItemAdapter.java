package com.kocen.zan.mov11;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by zan on 19/08/2016.
 */
public class DraverItemAdapter extends ArrayAdapter<Drawer> {
    Context mContext;
    int layoutResourceId;
    Drawer data[] = null;

    public DraverItemAdapter(Context mContext, int layoutResourceId, Drawer[] data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView mostPopularTxtView = (TextView)listItem.findViewById(R.id.mostPopularTxtId);

        Drawer folder = data[position];
        mostPopularTxtView.setText((CharSequence) folder.getMostPopular());

        return listItem;
    }
}