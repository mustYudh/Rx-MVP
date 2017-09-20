package com.htgames.rxmvp.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public abstract class BasicAdapter<T> extends BaseAdapter {
    protected ArrayList<T> list;
    BaseHolder<T> holder;


    public BasicAdapter(ArrayList<T> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = getHolder(parent.getContext());
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        holder.bindData(list.get(position));
        return holder.getHolderView();
    }

    protected abstract BaseHolder<T> getHolder(Context context);


    protected <V extends View> V findViewId(int id) {
        return (V) holder.getHolderView().findViewById(id);
    }

}
