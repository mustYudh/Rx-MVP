package com.htgames.rxmvp.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public abstract class MutilBasicAdapter extends BaseAdapter {
    BaseMutiHolder holder;
    private Object mutilData;


    @Override
    public int getViewTypeCount() {
        viewTypeCount();
        return viewTypeCount();
    }

    protected abstract int viewTypeCount();

    @Override
    public int getItemViewType(int position) {
        return itemViewType(position);
    }

    protected abstract int itemViewType(int position);

    @Override
    public int getCount() {
        return getMutiCount();
    }

    @Override
    public Object getItem(int position) {
        return getMutilData();
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
            holder = (BaseMutiHolder) convertView.getTag();
        }
        holder.bindData(getMutilData());
        return holder.getHolderView();
    }

    protected abstract BaseMutiHolder getHolder(Context context);


    protected <V extends View> V findViewId(int id) {
        return (V) holder.getHolderView().findViewById(id);
    }

    protected abstract int getMutiCount();

    protected abstract Object getMutilData();

}
