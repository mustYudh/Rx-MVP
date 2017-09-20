package com.htgames.rxmvp;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.htgames.rxmvp.base.SwipeBackBaseActivity;
import com.htgames.rxmvp.dagger.component.DaggerActivityComponent;
import com.htgames.rxmvp.dagger.module.ActivityModule;
import com.htgames.rxmvp.dagger.module.ISetUserNameModule;
import com.htgames.rxmvp.presenter.GetUserNamePresenter;
import com.htgames.rxmvp.view.ISetUserName;

import javax.inject.Inject;

import butterknife.BindView;

public class NextActivity extends SwipeBackBaseActivity implements ISetUserName {
    @Inject
    GetUserNamePresenter presenter;
    @BindView(R.id.show)
    TextView show;


    @Override
    public void setUserName(String name) {
        show.setText(name);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, NextActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void clickRefreshView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initInjector() {
        DaggerActivityComponent
                .builder()
                .iSetUserNameModule(new ISetUserNameModule(this))
                .activityModule(new ActivityModule())
                .build()
                .inject(this);
        presenter.getUserName("1");

    }

    @Override
    protected int attachView() {
        return R.layout.activity_next;
    }

    @Override
    protected void initView() {

    }


}
