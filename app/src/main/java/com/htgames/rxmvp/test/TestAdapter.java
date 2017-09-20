package com.htgames.rxmvp.test;

import android.content.Context;
import android.widget.TextView;

import com.htgames.rxmvp.R;
import com.htgames.rxmvp.base.BaseHolder;
import com.htgames.rxmvp.base.BasicAdapter;

import java.util.ArrayList;

/**
 * Created by yudenghao on 2017/9/16.
 */

public class TestAdapter extends BasicAdapter<String> {

    public TestAdapter(ArrayList<String> list) {
        super(list);
    }
    @Override
    protected BaseHolder<String> getHolder(final Context context) {
        return new BaseHolder<String>(context, R.layout.activity_next) {
            @Override
            public void bindData(String data) {
                TextView show = findViewId(R.id.show);
                show.setText(data);
            }
        };
    }
}
