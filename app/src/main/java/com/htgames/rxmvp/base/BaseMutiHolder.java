package com.htgames.rxmvp.base;

import android.content.Context;
import android.view.View;


public abstract class BaseMutiHolder {
    public View holderView;

    public BaseMutiHolder(Context context, int type, int[] layoutId) {
        holderView = initHolderView(context, getHolder(type,layoutId));
        holderView.setTag(this);
    }

    protected abstract int getHolder(int type, int[] layoutId);

    private View initHolderView(Context context, int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    /**
     * 绑定数据
     */
    public abstract void bindData(Object data);


    protected View getHolderView() {
        return holderView;
    }


}
