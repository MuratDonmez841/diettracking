package com.cakestudios.diettracking;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class customAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private List<Measurement> list;

    public customAdapter(Activity activity, List<Measurement> list) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View lineView;
        lineView = userInflater.inflate(R.layout.listview, null);
        TextView date = (TextView) lineView.findViewById(R.id.listDate);
        TextView var = (TextView) lineView.findViewById(R.id.listVar);

        Measurement measurement = list.get(i);

        date.setText(measurement.getDate());
        var.setText(measurement.getVarMesurement());

        return lineView;
    }
}