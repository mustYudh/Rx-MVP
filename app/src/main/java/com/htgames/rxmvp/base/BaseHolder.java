package com.htgames.rxmvp.base;

import android.content.Context;
import android.view.View;


public abstract class BaseHolder<T> {
    public View holderView;

    public BaseHolder(Context context, int layoutId) {
        holderView = initHolderView(context, layoutId);
        holderView.setTag(this);
    }

    private View initHolderView(Context context, int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    /**
     * 绑定数据
     */
    public abstract void bindData(T data);


    protected View getHolderView() {
        return holderView;
    }
}
