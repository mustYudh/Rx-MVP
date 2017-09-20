package com.htgames.rxmvp.base;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.htgames.rxmvp.weight.SwipeBackLayout;

/**
 * https://github.com/ikew0ng/SwipeBackLayout
 */
public abstract class SwipeBackBaseActivity extends BaseActivity {
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mSwipeBackLayout = new SwipeBackLayout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackLayout.attachToActivity(this, SwipeBackLayout.EDGE_LEFT);
        // 触摸边缘变为屏幕宽度的1/2
        mSwipeBackLayout.setEdgeSize(getResources().getDisplayMetrics().widthPixels / 3);
    }
}
